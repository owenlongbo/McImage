package com.smallsoho.mcplugin.image.utils
/**
 * Created by longlong on 2017/4/15.
 */
class Tools {

    def static cmd(def cmd) {
        String system = System.getProperty("os.name")
        switch (system) {
            case "Mac OS X":
                cmd = FileUtil.instance.getToolsDirPath() + "mac/" + cmd
                break
            case "Linux":
                cmd = FileUtil.instance.getToolsDirPath() + "linux/" + cmd
                break
            case "Windows":
                cmd = FileUtil.instance.getToolsDirPath() + "windows/" + cmd
                break
            default:
                LogUtil.log("McImage Not support this system")
                return
        }

        outputMessage(cmd)
    }

    def static boolean isLinux() {
        String system = System.getProperty("os.name")
        return "Linux" == system
    }

    def static boolean chmod() {
        boolean isSuccess = outputMessage("chmod 777 -R ${FileUtil.instance.getRootDirPath()}")
        if (isSuccess) {
            LogUtil.log("chmod success")
        } else {
            LogUtil.log("chmod failed")
        }

        return isSuccess
    }

    def static outputMessage(def cmd) {
        def proc = cmd.execute()
        def out = new StringBuilder(), err = new StringBuilder()
        proc.consumeProcessOutput(out, err)
        proc.waitFor()
        if (out.toString() != "" || err.toString() != "") {
            println "out> $out err> $err"
        }
    }

}
