记录一下语法吧

### Markdown 语法介绍

Markdown 是一种轻量级标记语言，让写作者专注于写作而不用关注样式。Coding 的许多版块均采用了 Markdown 语法，比如冒泡、讨论、Pull Request 等。

#### 标题

用 Markdown 书写时，只需要在文本前面加上『# 』即可创建一级标题。同理，创建二级标题、三级标题等只需要增加『# 』个数即可，Markdown 共支持六级标题。如下所示：

```
# 一级标题
## 二级标题
### 三级标题
#### 四级标题
##### 五级标题
###### 六级标题
```

点击预览可以看到效果：

![在这里输入图片描述](https://dn-coding-net-production-pp.codehub.cn/d46c3a8f-b74a-4008-ad1d-a56be443d5fa.png)

#### 锚点

Coding 会针对每个标题，在解析时都会添加锚点 id，如

```
# 锚点
```

会被解析成：

```
<h1 id="user-content-锚点">锚点</h1>
```

注意我们添加了一个 user-content- 的前缀，所以如果要自己添加跳转链接要使用 Markdown 的形式，且链接要加一个 user-content- 前缀，如：

```
[访问链接](#user-content-锚点);
```

#### 引用

Markdown 标记区块引用和 email 中用 『>』的引用方式类似，只需要在整个段落的第一行最前面加上 『>』 ：

```
> Coding.net 为软件开发者提供基于云计算技术的软件开发平台，包括项目管理，代码托管，运行空间和质量控制等等。
```

效果图如下：

![在这里输入图片描述](https://dn-coding-net-production-pp.codehub.cn/d735ad0c-2113-48dd-ae5d-2d3b3fca6977.png)

区块引用可以嵌套，只要根据层次加上不同数量的『>』：

```
> 这是第一级引用。
>
> > 这是第二级引用。
>
> 现在回到第一级引用。
```

效果图如下：

![在这里输入图片描述](https://dn-coding-net-production-pp.codehub.cn/db2ce6d5-5dc9-4c92-b226-50174d853eb9.png)

引用的区块内也可以使用其他的 Markdown 语法，包括标题、列表、代码区块等：

```
> ## 这是一个标题。
> 1. 这是第一行列表项。
> 2. 这是第二行列表项。
>
> 给出一些例子代码：
>
> return shell_exec(`echo $input | $markdown_script`);
```

效果图如下：

![在这里输入图片描述](https://dn-coding-net-production-pp.codehub.cn/a3485b98-6e38-45f2-9c9c-fb0b5027174c.png)

#### 列表

列表项目标记通常放在最左边，项目标记后面要接一个字符的空格。

**无序列表**：使用星号、加号或是减号作为列表标记

```
- Red
- Green
- Blue
```

效果图如下：

![在这里输入图片描述](https://dn-coding-net-production-pp.codehub.cn/d5772b14-8976-4e9f-945b-4b06d2a6e8f1.png)

**有序列表**：使用数字接着一个英文句点

```
1. Red
2. Green
3. Blue
```

效果图如下：

![在这里输入图片描述](https://dn-coding-net-production-pp.codehub.cn/d0321f44-c344-43d4-817e-9040735cf5b3.png)

如果要在列表项目内放进引用，那『>』就需要缩进：

```
*  Coding.net有以下主要功能:
    > 代码托管平台
    > 在线运行环境    
    > 代码质量监控    
    > 项目管理平台
```

效果图如下：

![在这里输入图片描述](https://dn-coding-net-production-pp.codehub.cn/21bfcf00-3a71-4b90-9f6e-e692dd3100a2.png)

代办列表: 表示列表是否勾选状态（注意：[ ] 前后都要有空格）

```
- [ ] 不勾选
- [x] 勾选
```

效果图如下：

![图片](https://dn-coding-net-production-pp.codehub.cn/6ff6802f-8105-4a6b-b8a4-2abc380c1107.png)

#### 代码

只要把你的代码块包裹在 “` 之间，你就不需要通过无休止的缩进来标记代码块了。 在围栏式代码块中，你可以指定一个可选的语言标识符，然后我们就可以为它启用语法着色了。 举个例子，这样可以为一段 Ruby 代码着色：

```
​```ruby
require 'redcarpet'
markdown = Redcarpet.new("Hello World!")
puts markdown.to_html
​```
```

效果图如下：

![在这里输入图片描述](https://dn-coding-net-production-pp.codehub.cn/64a6b611-e0b9-443d-b7a2-c134613b63f9.png)

#### 强调

在Markdown中，可以使用 * 和  _  来表示斜体和加粗。

**斜体**：

```
*Coding，让开发更简单*
_Coding，让开发更简单_
```

效果图如下：

![在这里输入图片描述](https://dn-coding-net-production-pp.codehub.cn/0e72e420-fd75-4dc8-8093-66a57e38cd68.png)

**加粗**：

```
**Coding，让开发更简单**
__Coding，让开发更简单__
```

*效果图如下：*

![在这里输入图片描述](https://dn-coding-net-production-pp.codehub.cn/372a6abf-f801-4a70-9f20-49f9e7db632d.png)

#### 自动链接

方括号显示说明，圆括号内显示网址， Markdown 会自动把它转成链接，例如：

```
[超强大的云开发平台Coding](http://coding.net)
```

效果图如下：

![在这里输入图片描述](https://dn-coding-net-production-pp.codehub.cn/3aeea7b8-a675-4491-adbb-b64b1145ff1a.png)

#### 表格

在 Markdown 中，可以制作表格，例如：

```
First Header | Second Header | Third Header
------------ | ------------- | ------------
Content Cell | Content Cell  | Content Cell
Content Cell | Content Cell  | Content Cell
```

效果图如下：

#### ![在这里输入图片描述](https://dn-coding-net-production-pp.codehub.cn/9a77d37a-95d4-4ad6-ab09-0d41f766fe34.jpg)

或者也可以让表格两边内容对齐，中间内容居中，例如：

```
First Header | Second Header | Third Header
:----------- | :-----------: | -----------:
Left         | Center        | Right
Left         | Center        | Right
```

效果图如下：

#### ![在这里输入图片描述](https://dn-coding-net-production-pp.codehub.cn/0d4014c0-3f54-462a-8a99-4706c62b9e5e.jpg)

#### 分割线

在 Markdown 中，可以使用 3 个以上『-』符号制作分割线，例如：

```
这是分隔线上部分内容
---
这是分隔线上部分内容
```

效果图如下：

#### ![在这里输入图片描述](https://dn-coding-net-production-pp.codehub.cn/aeb88b18-688b-41e9-a4a3-0f970ab3af3e.png)

#### 

#### 图片

Markdown 使用了类似链接的语法来插入图片, 包含两种形式: **内联** 和 **引用**.

**内联图片语法如下**:

```
![Alt text](/path/to/img.jpg)
或
![Alt text](/path/to/img.jpg "Optional title")
```

也就是:

- 一个惊叹号『!』
- 接着一个方括号，里面是图片的替代文字
- 接着一个普通括号，里面是图片的网址，最后还可以用引号包住并加上 选择性的『title’』文字。

**引用图片语法如下**:

```
![Alt text][id]
```

『id』 是图片引用的名称. 图片引用使用链接定义的相同语法:

```
[id]: url/to/image "Optional title attribute"
```

#### 流程图

Markdown 编辑器已支持绘制流程图、时序图和甘特图。通过 mermaid 实现图形的插入，点击查看 [更多语法详情](https://mermaidjs.github.io/)。

```
​```graph
graph TD;
    A-->B;
    A-->C;
    B-->D;
    C-->E;
    E-->F;
    D-->F;
    F-->G;
​```
```

效果图如下：

![img](https://dn-coding-net-production-pp.codehub.cn/9a6a38b8-172e-47f7-8464-b31948728149.jpg)

#### 时序图

```
​```graph
sequenceDiagram
    participant Alice
    participant Bob
    Alice->John: Hello John, how are you?
    loop Healthcheck
        John->John: Fight against hypochondria
    end
    Note right of John: Rational thoughts 
prevail...
    John-->Alice: Great!
    John->Bob: How about you?
    Bob-->John: Jolly good!
​```
```

效果图如下：![img](https://dn-coding-net-production-pp.codehub.cn/65d713f4-4088-4988-8959-79ba16b1fa6e.jpg)

#### 甘特图

```
​```graph
gantt
        dateFormat  YYYY-MM-DD
        title Adding GANTT diagram functionality to mermaid
        section A section
        Completed task            :done,    des1, 2014-01-06,2014-01-08
        Active task               :active,  des2, 2014-01-09, 3d
        Future task               :         des3, after des2, 5d
        Future task2               :         des4, after des3, 5d
        section Critical tasks
        Completed task in the critical line :crit, done, 2014-01-06,24h
        Implement parser and jison          :crit, done, after des1, 2d
        Create tests for parser             :crit, active, 3d
        Future task in critical line        :crit, 5d
        Create tests for renderer           :2d
        Add to mermaid                      :1d
​```
```

效果图如下：![img](https://dn-coding-net-production-pp.codehub.cn/651d802e-6409-4cf0-a3dd-b260c8d2cc60.jpg)
