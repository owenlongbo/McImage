package com.smallsoho.mcplugin.image.utils

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import org.gradle.api.Project

class AndroidUtil {

    static BaseExtension getAndroidExtension(Project project) {
        AppPlugin plugin = project.plugins.getPlugin(AppPlugin)
        return plugin.extension
    }


    static int getMinSdkVersion(Project project) {
        return getAndroidExtension(project).defaultConfig.minSdkVersion.apiLevel
    }

}
