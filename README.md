# McImage

[中文文档](README-CN.md)

McImage is an Android Gradle Plugin.It can help you check the big image in your res and compress your all image in your res.

Include

- The Image in Jar res
- The Image in aar res
- The Image in Module res

The Plugin use [pngquant](https://github.com/pornel/pngquant) to compress image,it can save 70% size.

### Use

First,change your root build.gradle.

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.smallsoho.mobcase:McImage:0.0.1'
    }
}
```

Then, apply this plugin in the module build.gradle which you want to compress.

```groovy
apply plugin: 'McImage'
```

Last, put the dir in your project root.

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
}
```

### Thanks

[pngquant](https://github.com/pornel/pngquant)




