package com.smallsoho.mcplugin.image.utils

@Singleton
class FileUtil {

    private String rootDir

    void setRootDir(rootDir) {
        this.rootDir = rootDir
    }

    def getToolsDir() {
        return new File("${rootDir}/mctools/")
    }

    def getToolsDirPath() {
        return rootDir + "/mctools/"
    }
}
