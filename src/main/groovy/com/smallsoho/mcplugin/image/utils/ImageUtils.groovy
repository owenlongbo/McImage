package com.smallsoho.mcplugin.image.utils

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class ImageUtils {

    def static boolean isAlphaPNG(File filePath) {
        if (filePath.exists()) {
            try {
                BufferedImage img = ImageIO.read(filePath);
                return img.getColorModel().hasAlpha()
            } catch (Exception e) {
                e.printStackTrace()
            }
        }
        return true;
    }
}