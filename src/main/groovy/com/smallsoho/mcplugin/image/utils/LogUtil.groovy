package com.smallsoho.mcplugin.image.utils

class LogUtil {

    def static log(def stage, def filePath, def oldInfo, def newInfo) {
        println "[${stage}][oldInfo: ${oldInfo}][newInfo: ${newInfo}]"
    }

    def static log(def stage, def info, def result) {
        println "[${stage}][Info: ${info}][Result: ${result}]"
    }

    def static log(def str) {
        println str
    }

}
