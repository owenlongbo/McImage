# McImage

[中文文档](README-CN.md)

[Mc插件原理解析](http://smallsoho.com/android/2017/04/07/McImage%E6%8F%92%E4%BB%B6%E8%A7%A3%E6%9E%90/)

McImage is an Android Gradle Plugin.It can help you check the big image in your res and compress your all image in your res.

Include

- The Image in Jar res
- The Image in aar res
- The Image in Module res

The Plugin use [pngquant](https://github.com/pornel/pngquant) to compress image,it can save 70% size.

### Update Log

- 0.0.3 : Add webp ! It will auto convert your png (without alpha in min API < 18 and not work in min API < 14) and jpg to webp if it will become more small.
- 0.0.2 : Improve the log.

### Use

First,change your root build.gradle.

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.smallsoho.mobcase:McImage:0.0.3'
    }
}
```

Then, apply this plugin in the module build.gradle which you want to compress.

```groovy
apply plugin: 'McImage'
```

Last, put the dir in your project root.Download from [here](https://github.com/Mobcase/McImage/releases)

```
mctools
```

PS: the plugin is default in MAC OSX, if you want to use in other platform,please change the pngquant bintray in mctools.

### Config

You can config the plugin in the build.gradle you apply this plugin.Include isCheck,isCompress and the max size of check.

```groovy
McImageConfig {
  isCheck true //default true
  isCompress true //default true
  maxSize 1*1024*1024 //default 1MB 
  isWebpConvert true //default true
  webpQuality 75 //default 75
  isJPGConvert true //default true
  enableWhenDebug true //default true
}
```

### Thanks

[pngquant](https://github.com/pornel/pngquant)




