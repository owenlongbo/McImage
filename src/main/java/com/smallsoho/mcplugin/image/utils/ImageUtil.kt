package com.smallsoho.mcplugin.image.utils

import com.smallsoho.mcplugin.image.Const
import java.io.File
import java.io.FileInputStream
import javax.imageio.ImageIO

class ImageUtil {

    companion object {
        private const val SIZE_TAG = "SizeCheck"

        fun isImage(file: File): Boolean {
            return (file.name.endsWith(Const.JPG) ||
                    file.name.endsWith(Const.PNG) ||
                    file.name.endsWith(Const.JPEG)
                    ) && !file.name.endsWith(Const.DOT_9PNG)
        }

        fun isJPG(file: File): Boolean {
            return file.name.endsWith(Const.JPG) || file.name.endsWith(Const.JPEG)
        }

        fun isAlphaPNG(filePath: File): Boolean {
            return if (filePath.exists()) {
                try {
                    val img = ImageIO.read(filePath)
                    img.colorModel.hasAlpha()
                } catch (e: Exception) {
                    LogUtil.log(e.message!!)
                    false
                }
            } else {
                false
            }
        }

        fun isBigSizeImage(imgFile: File, maxSize: Float): Boolean {
            if (isImage(imgFile)) {
                if (imgFile.length() >= maxSize) {
                    LogUtil.log(SIZE_TAG, imgFile.path, true.toString())
                    return true
                }
            }
            return false
        }

        fun isBigPixelImage(imgFile: File, maxWidth: Int, maxHeight: Int): Boolean {
            if (isImage(imgFile)) {
                val sourceImg = ImageIO.read(FileInputStream(imgFile))
                if (sourceImg.height > maxHeight || sourceImg.width > maxWidth) {
                    return true
                }
            }
            return false
        }
    }
}