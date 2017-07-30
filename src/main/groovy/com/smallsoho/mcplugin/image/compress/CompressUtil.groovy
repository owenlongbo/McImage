package com.smallsoho.mcplugin.image.compress

import com.smallsoho.mcplugin.image.utils.ImageUtil
import com.smallsoho.mcplugin.image.utils.LogUtil
import com.smallsoho.mcplugin.image.utils.Tools

class CompressUtil {

    public static final TAG = 'Compress'

    static void compressImg(File imgFile) {

        if (ImageUtil.isImage(imgFile)) {
            long oldSize = imgFile.length()
            if (ImageUtil.isJPG(imgFile)) {
                Tools.cmd("guetzli ${imgFile.getPath()} ${imgFile.getPath()}")
            } else {
                Tools.cmd("pngquant --skip-if-larger --speed 3 --force --output ${imgFile.getPath()} -- ${imgFile.getPath()}")
            }
            long newSize = imgFile.length()
            LogUtil.log(TAG, imgFile.getPath(), oldSize, newSize)
        }

    }
}