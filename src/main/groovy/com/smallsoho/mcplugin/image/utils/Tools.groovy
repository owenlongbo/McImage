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
                LinuxInit()
                cmd = FileUtil.instance.getToolsDirPath() + "linux/" + cmd
                break
            case "Windows":
                cmd = FileUtil.instance.getToolsDirPath() + "windows/" + cmd
                break
            default:
                LogUtil.log("McImage Not support this system")
                return
        }
        def proc = cmd.execute()
        proc.waitFor()
    }

    def static LinuxInit() {
        def proc = "chmod 755 -R ${FileUtil.instance.getToolsDirPath() + "/linux/"}".execute()
        proc.waitFor()
    }

}
