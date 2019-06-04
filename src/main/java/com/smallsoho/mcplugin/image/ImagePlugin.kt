package com.smallsoho.mcplugin.image;

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.api.BaseVariantImpl
import com.smallsoho.mcplugin.image.`interface`.IBigImage
import com.smallsoho.mcplugin.image.utils.*
import com.smallsoho.mcplugin.image.webp.WebpUtils
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import java.io.File
import java.util.concurrent.*

class ImagePlugin : Plugin<Project> {

    private lateinit var mcImageProject: Project
    private lateinit var mcImageConfig: Config
    private var oldSize: Long = 0
    private var newSize: Long = 0
    val bigImgList = ArrayList<String>()
    override fun apply(project: Project) {

        mcImageProject = project

        //判断是library还是application
        val hasAppPlugin = project.plugins.hasPlugin("com.android.application")
        val variants = if (hasAppPlugin) {
            (project.property("android") as AppExtension).applicationVariants
        } else {
            (project.property("android") as LibraryExtension).libraryVariants
        }

        //set config
        project.extensions.create("McImageConfig", Config::class.java)
        mcImageConfig = project.property("McImageConfig") as Config

        val taskNames = project.gradle.startParameter.taskNames
        var isDebugTask = false
        var isContainAssembleTask = false
        for (index: Int in 0 until taskNames.size) {
            val taskName = taskNames[index]
            if (taskName.contains("assemble") || taskName.contains("resguard") || taskName.contains("bundle")) {
                if (taskName.toLowerCase().endsWith("debug") &&
                        taskName.toLowerCase().contains("debug")) {
                    isDebugTask = true
                }
                isContainAssembleTask = true
                break
            }
        }

        //export build clean
        if (!isContainAssembleTask) {
            return
        }

        project.afterEvaluate { _ ->

            variants.all { variant ->

                variant as BaseVariantImpl

                checkMcTools(project)

                //debug enable
                if (isDebugTask && !mcImageConfig.enableWhenDebug) {
                    println("Debug not run !")
                    return@all
                }

                val mergeResourcesTask = project.tasks.findByName("merge${variant.name.capitalize()}Resources")
                val mcPicTask = project.task("McImage${variant.name.capitalize()}")

                mcPicTask.doLast { _ ->
                    println("---- McImage Plugin Start ----")
                    println(mcImageConfig.toString())

                    val dir = variant.mergeResources.computeResourceSetList0() //强行调用一下

                    val cacheList = ArrayList<String>()

                    val imageFileList = ArrayList<File>()

                    if (dir != null) {
                        for (channelDir: File in dir) {
                            traverseResDir(channelDir, imageFileList, cacheList, object : IBigImage {
                                override fun onBigImage(file: File) {
                                    bigImgList.add(file.absolutePath)
                                }
                            })
                        }
                    }

                    checkBigImage()

                    val start = System.currentTimeMillis()

                    mtDispathOptimizeTask(imageFileList)
                    println(sizeInfo())
                    println("---- McImage Plugin End ----, Total Time(ms) : ${System.currentTimeMillis() - start}")
                }

                val chmodTaskName = "chmod${variant.name.capitalize()}"
                val chmodTask = project.task(chmodTaskName)
                chmodTask.doLast { _ ->
                    //chmod if linux
                    if (Tools.isLinux()) {
                        Tools.chmod()
                    }
                }

                //inject task
                (project.tasks.findByName(chmodTask.name) as Task).dependsOn(mergeResourcesTask!!.taskDependencies.getDependencies(mergeResourcesTask))
                (project.tasks.findByName(mcPicTask.name) as Task).dependsOn(project.tasks.findByName(chmodTask.name) as Task)
                mergeResourcesTask.dependsOn(project.tasks.findByName(mcPicTask.name))

            }
        }
    }

    private fun traverseResDir(file: File, imageFileList: ArrayList<File>, cacheList: ArrayList<String>, iBigImage: IBigImage) {
        if (cacheList.contains(file.absolutePath)) {
            return
        } else {
            cacheList.add(file.absolutePath)
        }
        if (file.isDirectory) {
            file.listFiles().forEach {
                if (it.isDirectory) {
                    traverseResDir(it, imageFileList, cacheList, iBigImage)
                } else {
                    filterImage(it, imageFileList, iBigImage)
                }
            }
        } else {
            filterImage(file, imageFileList, iBigImage)
        }
    }

    private fun filterImage(file: File, imageFileList: ArrayList<File>, iBigImage: IBigImage) {
        if (mcImageConfig.whiteList.contains(file.name) || !ImageUtil.isImage(file)) {
            return
        }
        if (((mcImageConfig.isCheckSize && ImageUtil.isBigSizeImage(file, mcImageConfig.maxSize))
                || (mcImageConfig.isCheckPixels
                        && ImageUtil.isBigPixelImage(file, mcImageConfig.maxWidth, mcImageConfig.maxHeight)))
                && !mcImageConfig.bigImageWhiteList.contains(file.name)) {
            iBigImage.onBigImage(file)
        }
        imageFileList.add(file)
    }

    private fun mtDispathOptimizeTask(imageFileList: ArrayList<File>) {
        if (imageFileList == null || imageFileList.size == 0 || !bigImgList.isEmpty()) {
            return
        }
        val coreNum = Runtime.getRuntime().availableProcessors()
        if (imageFileList.size < coreNum || !mcImageConfig.multiThread) {
            for (file in imageFileList) {
                optimizeImage(file)
            }
        } else {
            val results = ArrayList<Future<Unit>>()
            val pool = Executors.newFixedThreadPool(coreNum)
            val part = imageFileList.size / coreNum
            for (i in 0 until coreNum) {
                val from = i * part
                val to = if (i == coreNum - 1) imageFileList.size - 1 else (i + 1) * part - 1
                results.add(pool.submit(Callable<Unit> {
                    for (index in from..to) {
                        optimizeImage(imageFileList[index])
                    }
                }))
            }
            for (f in results) {
                try {
                    f.get()
                } catch (ignore: Exception) {
                }
            }
        }
    }

    private fun optimizeImage(file: File) {
        var path: String = file.path
        if(File(path).exists()) {
            oldSize += File(path).length()
        }
        when (mcImageConfig.optimizeType) {
            Config.OPTIMIZE_WEBP_CONVERT ->
                WebpUtils.securityFormatWebp(file, mcImageConfig, mcImageProject)
            Config.OPTIMIZE_COMPRESS_PICTURE ->
                CompressUtil.compressImg(file)
        }
        countNewSize(path)
    }

    private fun countNewSize(path: String) {
        if(File(path).exists()) {
            newSize += File(path).length()
        } else {
            //转成了webp
            val indexOfDot = path.lastIndexOf(".")
            val webpPath = path.substring(0, indexOfDot) + ".webp"
            if(File(webpPath).exists()) {
                newSize += File(webpPath).length()
            } else {
                LogUtil.log("McImage: optimizeImage have some Exception!!!")
            }
        }
    }

    private fun checkBigImage() {
        if (bigImgList.size != 0) {
            val stringBuffer = StringBuffer("You have big Imgages with big size or large pixels," +
                    "please confirm whether they are necessary or whether they can to be compressed. " +
                    "If so, you can config them into bigImageWhiteList to fix this Exception!!!\n")
            for (i: Int in 0 until bigImgList.size) {
                stringBuffer.append(bigImgList[i])
                stringBuffer.append("\n")
            }
            throw GradleException(stringBuffer.toString())
        }
    }


    private fun checkMcTools(project: Project) {
        if (mcImageConfig.mctoolsDir.isBlank()) {
            FileUtil.setRootDir(project.rootDir.path)
        } else {
            FileUtil.setRootDir(mcImageConfig.mctoolsDir)
        }

        if (!FileUtil.getToolsDir().exists()) {
            throw GradleException("You need put the mctools dir in project root")
        }
    }

    private fun sizeInfo(): String {
        return "->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n" +
                "before McImage optimize: " + oldSize / 1024 + "KB\n" +
                "after McImage optimize: " + newSize / 1024 + "KB\n" +
                "McImage optimize size: " + (oldSize - newSize) / 1024 + "KB\n" +
                "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<-"


    }
}