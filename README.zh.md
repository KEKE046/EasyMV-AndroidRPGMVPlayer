## EasyMV-AndroidRPGMVPlayer
一个简单的安卓RPGMV游戏启动器。

# 简介

RPGMakerMV生成游戏是在网页上运行的，理论上有移植到安卓平台的潜力。但网上没有什么好的工具，已有的工具也都有各种各样的缺陷。

* [mv-android-client](https://github.com/AltimitSystems/mv-android-client) 项目可以将RPGMV的游戏打包成一个apk，但是需要下一个Android Studio，还有好多配置，非常麻烦。
  * 而且一旦卸载了apk，游戏存档就会消失。
* [AndroidLocalStorage](https://github.com/didimoo/AndroidLocalStorage) 项目提供了一种将`localStorage`储存在本地的方法，但该项目无法运行。
  * 经过修改，项目可以运行，但在玩游戏时卡顿严重，性能极差。

EasyMV 参考了上述两项目，具有以下特点：
* 将游戏存档防止在游戏目录下，即使卸载程序存档依然存在。
* 操作简单，将游戏中`www`文件夹放在在搜索路径即可。
* 解决移动设备上音频`.m4a`无法找到的问题。
* 借鉴了`mv-android-client`中的启动器，可以自动设置渲染和声音。

# 如何使用

1. 找到游戏目录中的`www`文件夹，文件夹里面要有`index.html`。
2. 将`www`文件夹拷贝出来，重命名成游戏的名字。
3. 很多个游戏重命名后的`www`，放在同一个目录下。
4. 将这个目录添加到EasyMV中（右上角的设置键）。
5. 更改设置，然后愉快地游戏吧。

# Hint

* `www/icon/icon.png`作为游戏的图标。
* 存档保存在`www/save/EasyMV.save`。
* EasyMV的配置文件保存在`www/EasyMV.properties`文件里。
* 设置中的标题可以和文件名不一样（甚至可以输入多行文本）。
* 如果需要对游戏注入javascript，可以直接在index.html里面改。
