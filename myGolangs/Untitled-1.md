###当前迭代流程
```puml
@startuml
[*] -> 用反头部问题
用反头部问题 -> 预研问题场景
state 预研问题场景{
挖掘对应场景数据 : 分析判定数据抓取规则
挖掘对应场景数据 -> 线下标注 : 挖掘数据线下标注人员标注
线下标注 --> 分析是否存在问题:确认预研标准
分析是否存在问题 --> 挖掘对应场景数据
}

预研问题场景 -> 标注开发
标注开发 --> 真值标注 : 输出
真值标注 -left-> 标注规范

标注规范 -left-> 真值
真值 -left-> 评测
state 评测{
   真值diff -left->策略: 问题发现
   策略  --> 麒麟:回归
   麒麟 ->真值diff
}
@enduml
```

###当前流程简化
```puml
@startuml
[*]-->等待用户反馈
等待用户反馈 ->头部问题:确认头部问题
头部问题 -->预研与迭代:评测迭代
预研与迭代:耗时长
预研与迭代 -left-> 策略:改进
策略-left->上线
上线->等待用户反馈
@enduml
```

从用户反馈问题到确定问题 然后要验证问题是否解决 就需要积累真值,而真值的获取是通过人工标注的方式一点一点慢慢积累,等真值ok后在修改判断策略ok否
那么如何缩短**预研与迭代**的时间

#####目前获取真值的方式是
```puml
@startuml
[*]->圈定用反范围
圈定用反范围->筛选socol设备数据
筛选socol设备数据->确定标注规范
确定标注规范->积累问题真值
积累问题真值->迭代解决问题
迭代解决问题->[*]
@enduml
```
####**<table><tr><td bgcolor=orange>此流程问题在用反问题是否真是伤害用户最大的不确定(很多问题不一定会上报),且获取真值和迭代流程长</td></tr></table>**

###理想流程(如以后有高精真值,则更容易适配)
####不要问题牵我们走,我们要牵着问题走
```puml
@startuml
[*]->可定真值数据
可定真值数据 --> 线上判定服务
可定真值数据:socol图片数据
可定真值数据:高精真值数据

麒麟发布数据 --> 线上判定服务
线上判定服务 : 机器学习(咨询过永光是可以做的)
线上判定服务 : 判定模型(重点保障模型准确度)
线上判定服务 : 根据图片和高精数据做真值
线上判定服务 -> 差异问题数据
差异问题数据 -> 数据问题归类
数据问题归类 -> 解题
@enduml
```

#####同时这套流程个人感想是也可以应用在事件体系上

##目前要做的
如何保障判定服务的模型准确度需要大量的真值来训练,
目前socol标注是通过标注一条link上的点来做为真值,但实际标注的时候标注人员是已经查看了设备的trace上下轨迹点(这些点都有图片)来判定要标注的点,trace信息如果能保留
可以对模型提高其判定准确度.
所以目前socol标注需要将标注人确定的信息都入库为真值.如何通过高效的方式保存目前确定的真值但是没入库的点可以商讨确定, 虽然会降低目前标注link的效率,但是重要的是积累了连续真值数据,能很大提升模型的准确度,且对长期评测发挥积极作用
###后续要做的
搭建判定服务模型