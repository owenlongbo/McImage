# McImage

[Mc插件原理解析](http://smallsoho.com/android/2017/04/07/McImage%E6%8F%92%E4%BB%B6%E8%A7%A3%E6%9E%90/)

McImage是一个插件帮助你检查你res中的大图和全量压缩你的res

包括

- Jar包中的图
- AAR中的图
- 子Module中的图

插件使用[pngquant](https://github.com/pornel/pngquant)算法进行压缩，每张图能节省百分之70%的大小

### Use

首先，修改你根目录的build.gradle.

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.smallsoho.mobcase:McImage:0.0.2'
    }
}
```

然后在你想要压缩的Module的build.gradle中应用这个插件

```groovy
apply plugin: 'McImage'
```

最后将我代码中的mctools文件夹放到项目根目录，此文件在[这里下载](https://github.com/Mobcase/McImage/releases)

```
mctools
```

PS：插件默认执行平台是MAC OSX，若果你想要在别的平台上使用，清手动替换mctools中的pngquant文件，文件在[pngquant](https://github.com/pornel/pngquant)库中可以找到

### Config

你可以在build.gradle中配置插件的几个属性，包括是否检查，是否压缩，和检查的最大大小

```groovy
McImageConfig {
  isCheck true //default true
  isCompress true //default true
  maxSize 1*1024*1024 //default 1MB 
}
```

### Thanks

[pngquant](https://github.com/pornel/pngquant)




