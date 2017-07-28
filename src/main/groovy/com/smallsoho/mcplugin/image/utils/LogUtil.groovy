package com.smallsoho.mcplugin.image.utils

class LogUtil {

    static void log(def stage, def filePath, def oldInfo, def newInfo) {
        println "[${stage}][${filePath}][oldInfo: ${oldInfo}][newInfo: ${newInfo}]"
    }

    static void log(def stage, def info, def result) {
        println "[${stage}][Info: ${info}][Result: ${result}]"
    }

    static void log(def str) {
        println str
    }

}
