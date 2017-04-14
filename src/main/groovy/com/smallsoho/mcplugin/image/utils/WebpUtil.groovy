package com.smallsoho.mcplugin.image.utils

/**
 * Created by longlong on 2017/4/15.
 */
class WebpUtil {

    def static final TAG = 'Webp'

    def static formatWebp(File imgFile, File rootDir) {

        File toolsPath = NormalUtil.getToolsDir(rootDir)

        if (!toolsPath.exists()) {
            LogUtil.log('You need put the mctools dir in project root')
            return
        }

        if (NormalUtil.isImage(imgFile)) {
            LogUtil.log("Start Compress PNG ${imgFile.getPath()}")
            File webpFile = new File("${imgFile.getPath().substring(imgFile.getPath().length() - 5, imgFile.getPath().length() - 1)}.webp")
            NormalUtil.cmd("${toolsPath.getPath()}/cwebp ${imgFile.getPath()} -o ${webpFile.getPath()}")
            LogUtil.log(TAG, imgFile.getPath(), imgFile.length(), webpFile.length())
        }

    }

}
