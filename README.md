# dubbo-shop

> 本系统是springcloudshop项目的dubbo 改版,采用tcc 模式来保证分布式事务,springcloud 版本参考　https://github.com/FurionCS/springCloudShop

## 开发环境
-  MySQL 5.7.17
-  RabbitMQ 3.6.6
-  Java8
-  redis 3.0
-  mongodb
-  guava
-  zookeeper
-  dubbo

## 项目结构

| 主要模块名称|     作用|   备注|
| :-------- | --------:| :------: |
|common|整个项目的工具类
|domain|领域,放一些model,request,response|
|order|订单模块|
|proudct|产品模块|
|user|用户模块|
|tcc|tcc事务模块|
|integral|用户积分模块|接受各种事件，进行积分变化

## 部署
1: 启动zookeeper,修改配置文件
2: 创建数据库,数据库文件在db文件夹中,修改数据库密码
3: 配置rabbitmq,mongodb,redis数据库
4: 启动用户系统=>产品系统=>积分系统＝>订单系统