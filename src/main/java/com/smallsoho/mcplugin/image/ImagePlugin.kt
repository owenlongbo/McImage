package com.smallsoho.mcplugin.image;

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.android.build.gradle.internal.api.BaseVariantImpl
import com.android.builder.model.Variant
import com.smallsoho.mcplugin.image.compress.CompressUtil
import com.smallsoho.mcplugin.image.utils.FileUtil
import com.smallsoho.mcplugin.image.utils.ImageUtil
import com.smallsoho.mcplugin.image.utils.Tools
import com.smallsoho.mcplugin.image.webp.WebpUtils
import groovy.lang.Closure
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import java.io.File
import java.util.*

class ImagePlugin : Plugin<Project> {

    private lateinit var mProject: Project
    private lateinit var mConfig: Config


    override fun apply(project: Project) {

        FileUtil.setRootDir(project.rootDir.path)

        mProject = project

        //判断是library还是application
        val hasAppPlugin = project.plugins.hasPlugin("com.android.application")
        val variants = if (hasAppPlugin) {
            (project.property("android") as AppExtension).applicationVariants
        } else {
            (project.property("android") as LibraryExtension).libraryVariants
        }

        //set config
        project.extensions.create("McImageConfig", Config::class.java)
        mConfig = project.property("McImageConfig") as Config

        val taskNames = project.gradle.startParameter.taskNames
        var isDebugTask = false
        var isContainAssembleTask = false
        for (index: Int in 0 until taskNames.size) {
            val taskName = taskNames[index]
            if (taskName.contains("assemble") || taskName.contains("resguard")) {
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

        project.afterEvaluate {

            variants.forEach {

                it as BaseVariantImpl

                val variant = it

                if (!FileUtil.getToolsDir().exists()) {
                    throw GradleException("You need put the mctools dir in project root")
                }

                val imgDir = if (variant.productFlavors.size == 0) {
                    "merged"
                } else {
                    "merged/${variant.productFlavors[0]}"
                }

                //debug enable
                if (isDebugTask && !mConfig.enableWhenDebug) {
                    println("Debug not run !")
                    return@afterEvaluate
                }

                val processResourceTask = project.tasks.findByName("process${variant.name.capitalize()}Resources")
                val mcPicTaskName = "McImage${variant.name.capitalize()}"

                val mcPicTask = project.task(mcPicTaskName)

                mcPicTask.doLast {
                    println("---- McImage Plugin Start ----")
                    val resPath = "${project.projectDir}/build/intermediates/res/$imgDir/"
                    val dir = File(resPath)
                    val bigImgList = ArrayList<String>()

                    dir.listFiles().forEach { channelDir ->
                        channelDir.listFiles().forEach { drawDir ->
                            val file = File("$drawDir")
                            if (file.name.contains("drawable") || file.name.contains("mipmap")) {
                                file.listFiles().forEach { imgFile ->
                                    if (mConfig.whiteList.contains("${file.getName()}/${imgFile.getName()}".toString())) {
                                        return@doLast
                                    }
                                    if (mConfig.isCheck &&
                                            ImageUtil.isBigImage(imgFile, mConfig.maxSize)) {
                                        bigImgList.add(imgFile.getAbsolutePath())
                                    }
                                    if (mConfig.isCompress) {
                                        CompressUtil.compressImg(imgFile)
                                    }
                                    if (mConfig.isCheckSize && ImageUtil.isBigSizeImage(imgFile, mConfig.maxWidth, mConfig.maxHeight)) {
                                        bigImgList.add(imgFile.absolutePath)
                                    }
                                    if (mConfig.isWebpConvert) {
                                        WebpUtils.securityFormatWebp(imgFile, mConfig, mProject)
                                    }

                                }
                            }
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
                chmodTask.doLast {
                    //chmod if linux
                    if (Tools.isLinux()) {
                        Tools.chmod()
                    }
                }

                //inject task
                (project.tasks.findByName(chmodTask.name) as Task).dependsOn(processResourceTask!!.taskDependencies.getDependencies(processResourceTask))
                (project.tasks.findByName(mcPicTask.name) as Task).dependsOn(project.tasks.findByName(chmodTask.name) as Task)
                processResourceTask.dependsOn(project.tasks.findByName(mcPicTask.name))

            }
        }
    }

}