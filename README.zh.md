## EasyMV-AndroidRPGMVPlayer
一个简单的安卓RPGMV游戏启动器。

作者: KEKE_046

版本: v1.1.0

github主页：[EasyMV-AndroidRPGMVPlayer](https://github.com/KEKE046/EasyMV-AndroidRPGMVPlayer)

# 简介

EasyMV 具有以下特点：
* 将游戏存档防止在游戏目录下，即使卸载程序存档依然存在。
* 操作简单，将游戏中`www`文件夹放在在搜索路径即可。
* 提供了fake_greenworks.js。调用greenworks.js向steam发送信息的游戏也可以启动。
* 没有操作手柄的游戏可以显示虚拟操作手柄。
* 提供了手动启动模式，解决部分游戏启动时会测试appid是否正确导致无法启动的问题。
* 解决移动设备上音频`.m4a`无法找到的问题。
* 使用启动器，可以自动设置渲染和声音。

<img src='fig/app1.jpg'/>
<img src='fig/app2.jpg'/>


# 如何使用

1. 找到游戏目录中的`www`文件夹，文件夹里面要有`index.html`。
2. 将`www`文件夹拷贝出来，重命名成游戏的名字。
3. 很多个游戏重命名后的`www`，放在同一个目录下。
4. 将这个目录添加到EasyMV中（右上角的设置键）。
5. 对于大部分游戏，可能需要更改音频后缀名为强制使用.ogg。
6. 更改设置，然后愉快地游戏吧。
```
gamedir
|-- noel s1（将www文件夹改成你想要的名字）
|   |-- index.html
|   |-- ......
|-- noel s2（将www文件夹改成你想要的名字）
|   |-- index.html
|   |-- ......
|-- ......
```

## 启动"被虐的诺艾尔"
<img src='fig/noel.png'/>

1. 将`www`文件夹放到你的游戏目录。
2. 将设置中的`音频文件后缀名`改成`强制使用.ogg`。
3. 愉快地游戏吧！

## 启动"烟火"
<img src='fig/fireworks.png'/>

1. 将`www`文件夹放到你的游戏目录。
2. 打开`手动模式启动`，`加入虚拟操作杆`，`使用假的greenwork.js`。
3. 烟火在游戏中会有突然FPS降低的情况，建议打开`显示FPS`。
4. 愉快地游戏吧！

# Hint

* `www/icon/icon.png`作为游戏的图标。
* 存档保存在`www/save/EasyMV.save`。
* EasyMV的配置文件保存在`www/EasyMV.properties`文件里。
* 设置中的标题可以和文件名不一样（甚至可以输入多行文本）。
* `fake_greenworks.js`中很多函数没有完全实现，如果你有需要，可以修改后直接写在游戏的`index.html`里面。
* 如果需要对游戏注入javascript，可以直接在`index.html`里面改。
* 诺艾尔太可爱了，强烈安利《被虐的诺艾尔》

# 参考

RPGMakerMV生成游戏是在网页上运行的，理论上有移植到安卓平台的潜力。但网上没有什么好的工具，已有的工具也都有各种各样的缺陷。

* [mv-android-client](https://github.com/AltimitSystems/mv-android-client) 项目可以将RPGMV的游戏打包成一个apk，但是需要下一个Android Studio，还有好多配置，非常麻烦。
  * 而且一旦卸载了apk，游戏存档就会消失。
* [AndroidLocalStorage](https://github.com/didimoo/AndroidLocalStorage) 项目提供了一种将`localStorage`储存在本地的方法，但该项目无法运行。
  * 经过修改，项目可以运行，但在玩游戏时卡顿严重，性能极差。

# 简要教程

第一步：找到你的游戏
<details>

![](fig/1.png)
</details>
第二步：找到游戏中的www文件夹
<details>

![](fig/2.png)
</details>
第三步：检查里面是否有index.html文件
<details>

![](fig/3.png)
</details>
第四步：将这个文件夹整个拷贝到你的手机上，注意这里拷贝到了**EasyMV**目录
<details>

![](fig/4.png)
</details>
第五步：修改成你喜欢的名字
<details>

![](fig/5.png)
</details>
第六步：打开EasyMV，点右上角的设置
<details>

![](fig/6.png)
</details>
第七步：点击添加，在弹出的目录选择框中将**EasyMV**添加到路径里面，然后点击右上角的确定。
<details>

![](fig/7.png)
</details>
第八步：游戏应该在列表中出现了
<details>

![](fig/8.png)
</details>
第九步：若有需要，可点击游戏标签上蓝色的设置按钮进行设置。（一般维持默认设置就可以了）。
<details>

![](fig/9.png)
</details>

第十步：点击游戏标签，开始游戏。
<details>

![](fig/10.png)
</details>