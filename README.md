# McImage

[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/mcimage/McImage)

[中文文档](README-CN.md)

[Android优雅的打包时自动化获取全部res资源](https://smallsoho.com/android/2018/07/26/Android-Android%E4%BC%98%E9%9B%85%E7%9A%84%E6%89%93%E5%8C%85%E6%97%B6%E8%87%AA%E5%8A%A8%E5%8C%96%E8%8E%B7%E5%8F%96%E5%85%A8%E9%83%A8res%E8%B5%84%E6%BA%90/)

McImage is a Non-invasive plugin for compress all res in your project.

Include

- The img in Jar
- The img in AAR
- The img in Module

Used algorithm

- [pngquant](https://github.com/pornel/pngquant) compress png
- [guetzli](https://github.com/google/guetzli) compress jpg
- [cwebp](https://developers.google.com/speed/webp/) compress webp

### Release Success!

The version 1.0.1 now support all build.gradle version!

### Feature

- Compress all png and jpg, every img can save %70 size.
- As far as possible to convert img to webp (after v0.0.3 support)
- Auto match the system which you build your project.Include Linux,Mac OS X and Windows (after v0.0.4 support)
- Use this plugin only need one line code.

### Update Log

> The user use v0.0.2 update plugin need update your mctools dir together.
- 1.5.1 : Fix windows support
- 1.5.0 : Update gradle plugin to 3.5.0 , change the get android resources way，fix not support abbreviation（such as assembleDebug use aD) bug.
- 1.4.0 : Feature, Support for selecting different optimization types，"ConvertWebp" or "Compress" can be chosen.Default "Compress". "CompressWebp" is a better compression ratio but it don't support api < 18
- 1.3.0 : Feature, Support multi-thread processing
- 1.2.0 : Feature, get compress command from system environment prior to local file
- 1.0.1 : Bug fix, fix maxSize float error
- 1.0.0 : Support AAPT2 , now don't need to close aapt2 with "android.enableAapt2=false", you can delete this line in gradle.properties.
- 0.1.4 : Bug fix, add the white list feature, add the img width and height check feature.
- 0.1.2 : Bug fix(Fix the problem that check image size not work)
- 0.1.1 : Bug fix(Fix the problem not work for module and fix the problem of enableWhenDebug not work)
- 0.0.4 : Add auto choose system future.Remove webpQuality config (Set inappropriate will result the img lossless)
- 0.0.3 : Add webp ! It will auto convert your png (without alpha in min API < 18 and not work in min API < 14) and jpg to webp if it will become more small.
- 0.0.2 : Improve the log.

### Who is using

I can put your icon with one link at here if you use McImage. My email b3069741@gmail.com

### Use

The first, add the plugin in your project root build.gradle.

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.smallsoho.mobcase:McImage:1.5.1'
    }
}
```

Then, apply the plugin in your every module.PS: If you have one more Module, you need apply it in every one.

```groovy
apply plugin: 'McImage'
```

Last, put the mctools dir in your project root dir.Download it [here](https://github.com/Mobcase/McImage/releases)


```
mctools
```

### Config

You can set the config in build.gradle.If you not set this,all config will use default.

```groovy
McImageConfig {
    isCheckSize true //Whether to detect image size，default true
    optimizeType "Compress" //Optimize Type，"ConvertWebp" or "Compress"，default "Compress", "CompressWebp" is a better compression ratio but it don't support api < 18
    maxSize 1*1024*1024 //big image size threshold，default 1MB
    enableWhenDebug false //switch in debug build，default true
    isCheckPixels true // Whether to detect image pixels of width and height，default true
    maxWidth 1000 //default 1000
    maxHeight 1000 //default 1000
    whiteList = [ //do not do any optimization for the images who in the list 
              "icon_launcher.png"
    ]
    mctoolsDir "$rootDir"
    isSupportAlphaWebp false  //Whether support convert the Image with Alpha chanel to Webp，default false, the images with alpha chanels will be compressed.if config true, its need api level >=18 or do some compatible measures 
    multiThread true  //Whether open multi-thread processing，default true
    bigImageWhiteList = [] //do not detect big size or large pixels for the images who in the list
}
```

### Thanks

[pngquant](https://github.com/pornel/pngquant)

[guetzli](https://github.com/google/guetzli)

[cwebp](https://developers.google.com/speed/webp/)

License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
