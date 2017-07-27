package com.smallsoho.mcplugin.image.utils

import com.smallsoho.mcplugin.image.Const

/**
 * Created by longlong on 2017/4/15.
 */
class NormalUtil {
    def static isImage(File file) {
        return (file.getName().endsWith(Const.JPG) || file.getName().endsWith(Const.PNG) || file.getName().endsWith(Const.JPEG)) && !file.getName().contains(Const.DOT_9PNG)
    }

    def static getToolsDir(File rootDir) {
        return new File("${rootDir.getParentFile().getPath()}/mctools/")
    }

    def static cmd(def cmd) {
        def proc = cmd.execute()
        proc.waitFor()
    }
}
