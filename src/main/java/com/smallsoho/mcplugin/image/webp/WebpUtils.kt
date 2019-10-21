package com.smallsoho.mcplugin.image.webp

import com.smallsoho.mcplugin.image.Const
import com.smallsoho.mcplugin.image.Config
import com.smallsoho.mcplugin.image.utils.*
import org.gradle.api.Project
import java.io.File

class WebpUtils {

    companion object {
        private const val VERSION_SUPPORT_WEBP = 14 //api>=14设设备支持webp
        private const val TAG = "Webp"

        private fun isPNGConvertSupported(project: Project): Boolean {
            return AndroidUtil.getMinSdkVersion(project) >= VERSION_SUPPORT_WEBP
        }

        private fun formatWebp(imgFile: File) {
            if (ImageUtil.isImage(imgFile)) {
                val webpFile = File("${imgFile.path.substring(0, imgFile.path.lastIndexOf("."))}.webp")
                Tools.cmd("cwebp", "${imgFile.path} -o ${webpFile.path} -m 6 -quiet")
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
                    LogUtil.log("[${TAG}][${imgFile.name}] do not convert webp because the size become larger!")
                }
            }
        }

        fun securityFormatWebp(imgFile: File, config: Config, project: Project) {
            if(!isPNGConvertSupported(project)) {
                throw Exception("minSDK < 14, Webp is not Support! Please choose other optimize Type!")
            }
            if (ImageUtil.isImage(imgFile)) {
                if(config.isSupportAlphaWebp) {
                    formatWebp(imgFile)
                } else {
                    if(imgFile.name.endsWith(Const.JPG) || imgFile.name.endsWith(Const.JPEG)) {
                        //jpg
                        formatWebp(imgFile)
                    } else if(imgFile.name.endsWith(Const.PNG) ){
                        //png
                        if(!ImageUtil.isAlphaPNG(imgFile)) {
                            //不包含透明通道
                            formatWebp(imgFile)
                        } else {
                            //包含透明通道的png，进行压缩
                            CompressUtil.compressImg(imgFile)
                        }
                    }
                }
            }

        }

    }

}