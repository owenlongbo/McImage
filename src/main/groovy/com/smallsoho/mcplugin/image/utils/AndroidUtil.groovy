package com.smallsoho.mcplugin.image.utils

import org.gradle.api.Project

class AndroidUtil {

    static int getMinSdkVersion(Project project) {
        return project.android.defaultConfig.minSdkVersion.apiLevel
    }

}
