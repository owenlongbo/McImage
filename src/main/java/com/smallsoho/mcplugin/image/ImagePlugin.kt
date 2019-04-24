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

class ImagePlugin : Plugin<Project> {

    private lateinit var mcImageProject: Project
    private lateinit var mcImageConfig: Config


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

                if (mcImageConfig.mctoolsDir.isBlank()) {
                    FileUtil.setRootDir(project.rootDir.path)
                } else {
                    FileUtil.setRootDir(mcImageConfig.mctoolsDir)
                }

                if (!FileUtil.getToolsDir().exists()) {
                    throw GradleException("You need put the mctools dir in project root")
                }

                //debug enable
                if (isDebugTask && !mcImageConfig.enableWhenDebug) {
                    println("Debug not run !")
                    return@all
                }

                val mergeResourcesTask = project.tasks.findByName("merge${variant.name.capitalize()}Resources")
                val mcPicTask = project.task("McImage${variant.name.capitalize()}")

                mcPicTask.doLast { _ ->
                    println("---- McImage Plugin Start ----")

                    val dir = variant.mergeResources.computeResourceSetList0() //强行调用一下

                    val bigImgList = ArrayList<String>()

                    val cacheList = ArrayList<String>()

                    if (dir != null) {
                        for (channelDir: File in dir) {
                            listDir(channelDir, cacheList, object : IBigImage {
                                override fun onBigImage(file: File) {
                                    bigImgList.add(file.absolutePath)
                                }
                            })
                        }
                    }


                    if (bigImgList.size != 0) {
                        val stringBuffer = StringBuffer("You have big Img!!!! \n")
                        for (i: Int in 0 until bigImgList.size) {
                            stringBuffer.append(bigImgList[i])
                            stringBuffer.append("\n")
                        }
                        throw GradleException(stringBuffer.toString())
                    }

                    println("---- McImage Plugin End ----")
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

    private fun listDir(file: File, cacheList: ArrayList<String>, iBigImage: IBigImage) {
        if (cacheList.contains(file.absolutePath)) {
            return
        } else {
            cacheList.add(file.absolutePath)
        }
        if (file.isDirectory) {
            file.listFiles().forEach {
                if (it.isDirectory) {
                    listDir(it, cacheList, iBigImage)
                } else {
                    rawCompress(it, iBigImage)
                }
            }
        } else {
            rawCompress(file, iBigImage)
        }
    }

    private fun rawCompress(file: File, iBigImage: IBigImage) {
        if (mcImageConfig.whiteList.contains(file.name)) {
            return
        }
        if (mcImageConfig.isCheck &&
                ImageUtil.isBigImage(file, mcImageConfig.maxSize)) {
            iBigImage.onBigImage(file)
        }
        if (mcImageConfig.isCompress) {
            CompressUtil.compressImg(file)
        }
        if (mcImageConfig.isCheckSize && ImageUtil.isBigSizeImage(file, mcImageConfig.maxWidth, mcImageConfig.maxHeight)) {
            iBigImage.onBigImage(file)
        }
        if (mcImageConfig.isWebpConvert) {
            WebpUtils.securityFormatWebp(file, mcImageConfig, mcImageProject)
        }
    }

}