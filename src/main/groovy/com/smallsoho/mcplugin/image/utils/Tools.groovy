package com.smallsoho.mcplugin.image.utils
/**
 * Created by longlong on 2017/4/15.
 */
class Tools {

    def static cmd(def cmd) {
        def proc = cmd.execute()
        proc.waitFor()
    }

}
