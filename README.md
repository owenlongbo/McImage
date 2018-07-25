# McImage

[中文文档](README-CN.md)

[Mc插件原理解析(0.1.5之前版本)](https://smallsoho.com/android/2017/04/07/McImage%E6%8F%92%E4%BB%B6%E8%A7%A3%E6%9E%90/)

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

gradle 3.X use 1.X.X

before gradle 3.0 use 0.1.5

### Feature

- Compress all png and jpg, every img can save %70 size.
- As far as possible to convert img to webp (after v0.0.3 support)
- Auto match the system which you build your project.Include Linux,Mac OS X and Windows (after v0.0.4 support)
- Use this plugin only need one line code.

### Update Log

> The user use v0.0.2 update plugin need update your mctools dir together.

- 1.0.1 : Bug fix, fix maxSize float error
- 1.0.0 : Support AAPT2 , now don't need to close aapt2 with "android.enableAapt2=false", you can delete this line in gradle.properties.
- 0.1.4 : Bug fix, add the white list feature, add the img width and height check feature.
- 0.1.2 : Bug fix(Fix the problem that check image size not work)
- 0.1.1 : Bug fix(Fix the problem not work for module and fix the problem of enableWhenDebug not work)
- 0.0.4 : Add auto choose system future.Remove webpQualitu config (Set inappropriate will result the img lossless)
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
        classpath 'com.smallsoho.mobcase:McImage:1.0.1'
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
  isCheck true //default true
  isCompress true //default true
  maxSize 1*1024*1024 //default 1MB
  isWebpConvert true //default true
  isJPGConvert true //default true
  enableWhenDebug true //default true
  isCheckSize true //default true
  maxWidth 500 //defualt 500 the default size of check size feature
  maxHeight 500 //defualt 500 the default size of check size feature
  whiteList = [
    "drawable-xxhdpi-v4/img_five_stars.png" //add this line, the plugin can not deal with this img.
  ]
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
