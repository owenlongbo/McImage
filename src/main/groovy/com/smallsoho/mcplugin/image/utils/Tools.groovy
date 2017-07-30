package com.smallsoho.mcplugin.image.utils
/**
 * Created by longlong on 2017/4/15.
 */
class Tools {

    def static cmd(def cmd) {
        String system = System.getProperty("os.name")
        switch (system) {
            case "MAC OS X":
                cmd = FileUtil.instance.getToolsDirPath() + "/mac/"
                break
            case "Linux":
                cmd = FileUtil.instance.getToolsDirPath() + "/linux/"
                break
            case "Windows":
                cmd = FileUtil.instance.getToolsDirPath() + "/linux/"
                break
            default:
                LogUtil.log("McImage Not support this system")
                return
        }
        def proc = cmd.execute()
        proc.waitFor()
    }

}
