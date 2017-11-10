## Reflaction
### 项目由来
在上一个项目“游戏盒子”即将结束，进入验收阶段时， App 上的某些内容需要临时屏蔽。被问到能否在后台设置屏蔽与否，鉴于修改的复杂性和这种临时屏蔽工作的低频性，给出的答案是“不能”。

事后反思，答案合情合理，不可能在后台塞入大量的使用频率极低的标记字段，这样后台会被迫变成功能繁多却不实用、数据库和接口也随之变得臃肿。

恰巧和舍友谈及 RN ，于是探索着 code 一个项目，可以轻量改变 App 的部分少量 appearance 的同时减轻后台的压力。

### 名字来源
来源于 Android 的属于名词反射机制(Reflection) 和行为(Action)。

### 脑内讨论
Q: 到底是怎样减轻后台压力？<br />
A: 以自定语法的脚本，取代过多的、使用频率低的字段。

Q: 脚本如何交付给 App ？<br />
A: 不是这个项目解决的问题。

Q: 如何确保安全性？<br />
A: 不确保安全性。

Q: 是否具有侵入性？<br />
A: 是 :)

Q: 效率性如何？<br />
A: 低。



### 脚本格式
```
receiver1,method1,argv_class:argv_value,...;
receiver2,method2,argv_class:argv_value,...;
receiver3,method3,argv_class:(receiver4,method4,argv_class:argv_value,...),...;
```

argv_class 内置格式：
- String
- Integer(int)
- Boolean(boolean)
- Float(float)
- Double(double)
- Long(long)

### 存在问题
- 不支持基础运算(+,-,\*,/) 和一些基础的操作，不支持类方法。