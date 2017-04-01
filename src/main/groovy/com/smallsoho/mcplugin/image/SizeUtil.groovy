package com.smallsoho.mcplugin.image

class SizeUtil {
    static boolean checkImgSize(File imgFile, int maxSize) {
        if ((imgFile.getName().endsWith('.jpg') || imgFile.getName().endsWith('.png'))
                && !imgFile.getName().contains('.9')) {
            println 'Start Check ' + imgFile.getPath()
            if (imgFile.length() >= maxSize) {
                return true
            }
        }
        return false
    }
}