package com.smallsoho.mcplugin.image.utils

class CompressUtil {

    def static final TAG = 'Compress'

    def static compressImg(File imgFile, File rootDir) {

        File toolsPath = NormalUtil.getToolsDir(rootDir)

        if (!toolsPath.exists()) {
            LogUtil.log('You need put the mctools dir in project root')
            return
        }

        if (NormalUtil.isImage(imgFile)) {
            long oldSize = imgFile.length()
            NormalUtil.cmd("${toolsPath.getPath()}/pngquant --skip-if-larger --speed 11 --force --output ${imgFile.getPath()} -- ${imgFile.getPath()}")
            long newSize = imgFile.length()
            LogUtil.log(TAG, imgFile.getPath(), oldSize, newSize)
        }

    }
}