package com.smallsoho.mcplugin.image.utils

class SizeUtil {

    def static final TAG = 'SizeCheck'

    def static isBigImage(File imgFile, int maxSize) {
        if (NormalUtil.isImage(imgFile)) {
            if (imgFile.length() >= maxSize) {
                LogUtil.log(TAG, imgFile.getPath(), true as String)
            }
        }
        return false
    }
}