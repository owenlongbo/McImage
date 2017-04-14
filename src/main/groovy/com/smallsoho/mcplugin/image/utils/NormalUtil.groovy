package com.smallsoho.mcplugin.image.utils

/**
 * Created by longlong on 2017/4/15.
 */
class NormalUtil {
    def static isImage(File file) {
        return (file.getName().endsWith('.jpg') || file.getName().endsWith('.png')) && !file.getName().contains('.9')
    }

    def static getToolsDir(File rootDir) {
        return new File("${rootDir.getParentFile().getPath()}/mctools/")
    }

    def static cmd(def cmd) {
        def proc = cmd.execute()
        proc.waitFor()
    }
}
