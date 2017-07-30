package com.smallsoho.mcplugin.image.utils

import com.smallsoho.mcplugin.image.Const

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class ImageUtil {

    public static final String SIZE_TAG = 'SizeCheck'

    static boolean isImage(File file) {
        return (file.getName().endsWith(Const.JPG) ||
                file.getName().endsWith(Const.PNG) ||
                file.getName().endsWith(Const.JPEG)
        ) && !file.getName().endsWith(Const.DOT_9PNG)
    }

    static boolean isJPG(File file) {
        return file.getName().endsWith(Const.JPG) || file.getName().endsWith(Const.JPEG)
    }

    static boolean isAlphaPNG(File filePath) {
        if (filePath.exists()) {
            try {
                BufferedImage img = ImageIO.read(filePath)
                return img.getColorModel().hasAlpha()
            } catch (Exception e) {
                LogUtil.log(e.getMessage())
                return false
            }
        } else {
            return false
        }
    }

    static boolean isBigImage(File imgFile, int maxSize) {
        if (isImage(imgFile)) {
            if (imgFile.length() >= maxSize) {
                LogUtil.log(SIZE_TAG, imgFile.getPath(), true as String)
            }
        }
        return false
    }
}