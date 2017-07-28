package com.smallsoho.mcplugin.image.compress

import com.smallsoho.mcplugin.image.utils.FileUtil
import com.smallsoho.mcplugin.image.utils.ImageUtil
import com.smallsoho.mcplugin.image.utils.LogUtil
import com.smallsoho.mcplugin.image.utils.Tools

class CompressUtil {

    public static final TAG = 'Compress'

    static void compressImg(File imgFile) {

        if (ImageUtil.isImage(imgFile)) {
            long oldSize = imgFile.length()
            Tools.cmd("${FileUtil.instance.getToolsDirPath()}/pngquant --skip-if-larger --speed 11 --force --output ${imgFile.getPath()} -- ${imgFile.getPath()}")
            long newSize = imgFile.length()
            LogUtil.log(TAG, imgFile.getPath(), oldSize, newSize)
        }

    }
}