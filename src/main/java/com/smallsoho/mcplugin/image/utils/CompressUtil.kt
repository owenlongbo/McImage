package com.smallsoho.mcplugin.image.utils

import java.io.File

class CompressUtil {

    companion object {
        private const val TAG = "Compress"
        fun compressImg(imgFile: File) {
            if (ImageUtil.isImage(imgFile)) {
                val oldSize = imgFile.length()
                if (ImageUtil.isJPG(imgFile)) {
                    Tools.cmd("guetzli ${imgFile.path} ${imgFile.path}")
                } else {
                    Tools.cmd("pngquant --skip-if-larger --speed 3 --force --output ${imgFile.path} -- ${imgFile.path}")
                }
                val newSize = imgFile.length()
                LogUtil.log(TAG, imgFile.path, oldSize.toString(), newSize.toString())
            }
        }
    }

}