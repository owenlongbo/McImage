package com.smallsoho.mcplugin.image.utils

import org.gradle.api.Project

class WebpUtils {
    def static final int VERSION_SUPPORT_WEBP = 14;
    def static final int VERSION_SUPPORT_TRANSPARENT_WEBP = 18;

    def static boolean isPNGConvertSupported(Project project) {
        return Utils.getMinSdkVersion(project) >= VERSION_SUPPORT_WEBP
    }

    def static boolean isTransparentPNGSupported(Project project) {
        return Utils.getMinSdkVersion(project) >= VERSION_SUPPORT_TRANSPARENT_WEBP
    }


    def static final TAG = 'Webp'

    def static formatWebp(File imgFile, File rootDir, int quality) {

        File toolsPath = NormalUtil.getToolsDir(rootDir)

        if (!toolsPath.exists()) {
            LogUtil.log('You need put the mctools dir in project root')
            return
        }

        if (NormalUtil.isImage(imgFile)) {
            File webpFile = new File("${imgFile.getPath().substring(0, imgFile.getPath().indexOf("."))}.webp")
            NormalUtil.cmd("${toolsPath.getPath()}/cwebp -q ${quality}  ${imgFile.getPath()} -o ${webpFile.getPath()}")
            LogUtil.log(TAG, imgFile.getPath(), imgFile.length(), webpFile.length())
            if (imgFile.exists()) {
                imgFile.delete()
            }
        }

    }

}