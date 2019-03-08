package com.smallsoho.mcplugin.image.webp

import com.smallsoho.mcplugin.image.Const
import com.smallsoho.mcplugin.image.Config
import com.smallsoho.mcplugin.image.utils.AndroidUtil
import com.smallsoho.mcplugin.image.utils.ImageUtil
import com.smallsoho.mcplugin.image.utils.LogUtil
import com.smallsoho.mcplugin.image.utils.Tools
import org.gradle.api.Project
import java.io.File

class WebpUtils {

    companion object {
        private const val VERSION_SUPPORT_WEBP = 14
        private const val VERSION_SUPPORT_TRANSPARENT_WEBP = 18
        private const val TAG = "Webp"

        private fun isPNGConvertSupported(project: Project): Boolean {
            return AndroidUtil.getMinSdkVersion(project) >= VERSION_SUPPORT_WEBP
        }

        private fun isTransparentPNGSupported(project: Project): Boolean {
            return AndroidUtil.getMinSdkVersion(project) >= VERSION_SUPPORT_TRANSPARENT_WEBP
        }

        private fun formatWebp(imgFile: File) {
            if (ImageUtil.isImage(imgFile)) {
                val webpFile = File("${imgFile.path.substring(0, imgFile.path.lastIndexOf("."))}.webp")
                Tools.cmd("cwebp", "${imgFile.path} -o ${webpFile.path} -quiet")
                if (webpFile.length() < imgFile.length()) {
                    LogUtil.log(TAG, imgFile.path, imgFile.length().toString(), webpFile.length().toString())
                    if (imgFile.exists()) {
                        imgFile.delete()
                    }
                } else {
                    //如果webp的大的话就抛弃
                    if (webpFile.exists()) {
                        webpFile.delete()
                    }
                }
            }
        }

        fun securityFormatWebp(imgFile: File, config: Config, project: Project) {
            if (ImageUtil.isImage(imgFile)) {
                //png
                if (imgFile.name.endsWith(Const.PNG)) {
                    if (isPNGConvertSupported(project)) {
                        if (ImageUtil.isAlphaPNG(imgFile)) {
                            if (isTransparentPNGSupported(project)) {
                                formatWebp(imgFile)
                            }
                        } else {
                            formatWebp(imgFile)
                        }
                    }
                    //jpg
                } else if (imgFile.name.endsWith(Const.JPG) || imgFile.name.endsWith(Const.JPEG)) {
                    if (config.isJPGConvert) {
                        formatWebp(imgFile)
                    }
                    //other
                } else {
                    LogUtil.log(TAG, imgFile.path, "don't convert")
                }
            }

        }

    }

}