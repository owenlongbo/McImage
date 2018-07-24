package com.smallsoho.mcplugin.image.utils

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project

class AndroidUtil {

    companion object {
        fun getMinSdkVersion(project: Project): Int {
            return (project.property("android") as BaseExtension).defaultConfig.minSdkVersion.apiLevel
        }
    }

}
