package com.smallsoho.mcplugin.image.utils

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by longlong on 2017/4/15.
 */
class Tools {

    companion object {
        fun cmd(cmd: String, params: String) {
            val cmdStr = if (isCmdExist(cmd)) {
                "$cmd $params"
            } else {
                when {
                    isMac() ->
                        FileUtil.getToolsDirPath() + "mac/" + "$cmd $params"
                    isLinux() ->
                        FileUtil.getToolsDirPath() + "linux/" + "$cmd $params"
                    isWindows() ->
                        FileUtil.getToolsDirPath() + "windows/" + "$cmd $params"
                    else -> ""
                }
            }
            if (cmdStr == "") {
                LogUtil.log("McImage Not support this system")
                return
            }
            outputMessage(cmdStr)
        }

        fun isLinux(): Boolean {
            val system = System.getProperty("os.name")
            return system.startsWith("Linux")
        }

        fun isMac(): Boolean {
            val system = System.getProperty("os.name")
            return system.startsWith("Mac OS")
        }

        fun isWindows(): Boolean {
            val system = System.getProperty("os.name")
            return system.toLowerCase().contains("win")
        }

        fun chmod() {
            outputMessage("chmod 755 -R ${FileUtil.getRootDirPath()}")
        }

        private fun outputMessage(cmd: String) {
            val process = Runtime.getRuntime().exec(cmd)
            process.waitFor()
        }

        private fun isCmdExist(cmd: String): Boolean {
            val result = if (isMac() || isLinux()) {
                executeCmd("which $cmd")
            } else {
                executeCmd("where $cmd")
            }
            return result != null && !result.isEmpty()
        }

        private fun executeCmd(cmd: String): String? {
            val process = Runtime.getRuntime().exec(cmd)
            process.waitFor()
            val bufferReader = BufferedReader(InputStreamReader(process.inputStream))
            return try {
                bufferReader.readLine()
            } catch (e: Exception) {
                LogUtil.log(e)
                null
            }
        }
    }
}
