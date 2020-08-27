管家服务
==========

## 基础建设

1. SBT 1.3.x
2. scala 2.12.x

## 需求要点

1. 分布式自动调度任务

2. 基于ZIO实现

3. 需要实现自定义DSL，形式如下：

```bash
  cron"*/5 * * ? * *" ~~> classOf[Simulator]
```
这里表示每5秒执行一次调度。

![img](log.png)

4. 不能发生并发，满足CAP，数据满足一致性。


## 服务职责

1. 动态添加。生产数据库
2. 随机的数据机制
3. 集中为FP数据测试
4. 定时任务计算
5. 产生表格
6. 统计分析、推送、收集数据

## build image

```shell script
# publish image
sbt docker:publishLocal
# compose
```

指定入参种子

```shell script
-Dakka.cluster.seed-nodes.0=akka://ClusterSystem@host1:2552
-Dakka.cluster.seed-nodes.1=akka://ClusterSystem@host2:2552
```
