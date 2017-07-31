# McImage

[中文文档](README-CN.md)

[Mc插件原理解析](http://smallsoho.com/android/2017/04/07/McImage%E6%8F%92%E4%BB%B6%E8%A7%A3%E6%9E%90/)

McImage is a Non-invasive plugin for compress all res in your project.

Include

- The img in Jar
- The img in AAR
- The img in Module

Used algorithm

- [pngquant](https://github.com/pornel/pngquant) compress png
- [guetzli](https://github.com/google/guetzli) compress jpg
- [cwebp](https://developers.google.com/speed/webp/) compress webp

### Future

- Compress all png and jpg, every img can save %70 size.
- As far as possible to convert img to webp (after v0.0.3 support)
- Auto match the system which you build your project.Include Linux,Mac OS X and Windows (after v0.0.4 support)
- Use this plugin only need one line code.

### Update Log

> The user use v0.0.2 update plugin need update your mctools dir together.

- 0.0.4 : Add auto choose system future.Remove webpQualitu config (Set inappropriate will result the img lossless)
- 0.0.3 : Add webp ! It will auto convert your png (without alpha in min API < 18 and not work in min API < 14) and jpg to webp if it will become more small.
- 0.0.2 : Improve the log.

### Use

The first, add the plugin in your project root build.gradle.

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.smallsoho.mobcase:McImage:0.0.4'
    }
}
```

Then, apply the plugin in your every module.PS: If your have one more Module, you need apply it in every one.

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
