package com.smallsoho.mcplugin.image

import com.android.build.gradle.AppPlugin
import com.smallsoho.mcplugin.image.compress.CompressUtil
import com.smallsoho.mcplugin.image.models.Config
import com.smallsoho.mcplugin.image.utils.FileUtil
import com.smallsoho.mcplugin.image.utils.ImageUtil

import com.smallsoho.mcplugin.image.webp.WebpUtils
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class ImagePlugin implements Plugin<Project> {

    Project mProject
    Config mConfig

    @Override
    void apply(Project project) {

        FileUtil.instance.setRootDir(project.rootDir)

        mProject = project

        //判断是library还是application
        def hasAppPlugin = project.plugins.withType(AppPlugin)
        def variants = hasAppPlugin ? project.android.applicationVariants : project.android.libraryVariants

        //set config
        project.extensions.create('McImageConfig', Config)
        mConfig = project.McImageConfig

        def taskNames = project.gradle.startParameter.taskNames
        def isDebugTask = false
        def isContainAssembleTask = false
        for (int index = 0; index < taskNames.size(); ++index) {
            def taskName = taskNames[index]
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
            variants.all { variant ->

                if (!FileUtil.instance.getToolsDir().exists()) {
                    throw new GradleException('You need put the mctools dir in project root')
                }

                def imgDir
                if (variant.productFlavors.size() == 0) {
                    imgDir = 'merged'
                } else {
                    imgDir = "merged/${variant.productFlavors[0].name}"
                }

                //debug enable
                if (isDebugTask && !mConfig.enableWhenDebug) {
                    println 'Debug not run !'
                    return
                }

                def processResourceTask = project.tasks.findByName("process${variant.name.capitalize()}Resources")
                def mcPicPlugin = "McImage${variant.name.capitalize()}"
                project.task(mcPicPlugin) {
                    doLast {

                        println '---- McImage Plugin Start ----'

                        String resPath = "${project.projectDir}/build/intermediates/res/${imgDir}/"

                        def dir = new File("${resPath}")

                        ArrayList<String> bigImgList = new ArrayList<>()

                        dir.eachDir() { channelDir ->
                            channelDir.eachDir { drawDir ->
                                def file = new File("${drawDir}")
                                if (file.name.contains('drawable') || file.name.contains('mipmap')) {
                                    file.eachFile { imgFile ->
                                        if (mConfig.whiteList.contains("${file.getName()}/${imgFile.getName()}".toString())) {
                                            return
                                        }
                                        if (mConfig.isCheck &&
                                                ImageUtil.isBigImage(imgFile, mConfig.maxSize)) {
                                            bigImgList.add(imgFile.getAbsolutePath())
                                        }
                                        if (mConfig.isCompress) {
                                            CompressUtil.compressImg(imgFile)
                                        }
                                        if (mConfig.isWebpConvert) {
                                            WebpUtils.securityFormatWebp(imgFile, mConfig, mProject)
                                        }

                                    }
                                }
                            }
                        }

                        if (bigImgList.size() != 0) {
                            StringBuffer stringBuffer = new StringBuffer("You have big Img!!!! \n")
                            for (int i = 0; i < bigImgList.size(); i++) {
                                stringBuffer.append(bigImgList.get(i))
                                stringBuffer.append("\n")
                            }
                            throw new GradleException(stringBuffer.toString())
                        }

                        println '---- McImage Plugin End ----'
                    }
                }

                //inject plugin
                project.tasks.findByName(mcPicPlugin).dependsOn processResourceTask.taskDependencies.getDependencies(processResourceTask)
                processResourceTask.dependsOn project.tasks.findByName(mcPicPlugin)
            }
        }
    }

}