package com.smallsoho.mcplugin.image.utils

/**
 * Created by longlong on 2017/4/15.
 */
class Tools {

    companion object {
        fun cmd(cmd: String) {
            val system = System.getProperty("os.name")
            val cmdStr = when (system) {
                "Mac OS X" ->
                    FileUtil.getToolsDirPath() + "mac/" + cmd
                "Linux" ->
                    FileUtil.getToolsDirPath() + "linux/" + cmd
                "Windows" ->
                    FileUtil.getToolsDirPath() + "windows/" + cmd
                else -> ""
            }
            if (cmdStr == "") {
                LogUtil.log("McImage Not support this system")
                return
            }
            outputMessage(cmdStr)
        }

        fun isLinux(): Boolean {
            val system = System.getProperty("os.name")
            return "Linux" == system
        }

        fun chmod() {
            outputMessage("chmod 755 -R ${FileUtil.getRootDirPath()}")
        }

        private fun outputMessage(cmd: String) {
            val process = Runtime.getRuntime().exec(cmd)
            process.waitFor()
        }
    }
}
