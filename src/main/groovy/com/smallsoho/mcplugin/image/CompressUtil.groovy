package com.smallsoho.mcplugin.image

class CompressUtil {
    def static compressImg(File imgFile, File rootDir) {

        File toolsPath = new File(rootDir.getParentFile().getPath() + '/mctools/')

        if (!toolsPath.exists()) {
            println 'You need put mctools in project root'
            return
        }

        if ((imgFile.getName().endsWith('.png') || imgFile.getName().endsWith('.jpg')) && !imgFile.getName().contains('.9')) {
            println 'Start Compress PNG' + imgFile.getPath()
            println 'old size:' + imgFile.length()
            def command = "${rootDir.getParentFile().getPath() + '/mctools/'}pngquant --skip-if-larger --speed 11 --force --output ${imgFile.getPath()} -- ${imgFile.getPath()}"
            def proc = command.execute()
            proc.waitFor()
            println 'new size:' + imgFile.length()
        }

    }
}