0.启动mysql数据库
1.启动nacos1.4.1 集群
    执行脚本/Users/mc/nacos1.4.1/startup.sh
    注：如果启动失败，应删除
    /Users/mc/nacos1.4.1/8848/data/protocol
    /Users/mc/nacos1.4.1/8849/data/protocol
    /Users/mc/nacos1.4.1/8850/data/protocol
    检查/Users/mc/nacos1.4.1/884？/conf/cluster.conf
    配置文件中的ip是否和application.yml或bootstrap.yml中的ip一致
    启动完成访问http://172.26.33.9:8848/nacos，查看集群节点是否都为UP
2.启动sentinel-dashboard-1.8.0.jar
    java -jar /Users/mc/sentinel-dashboard-1.8.0.jar
    启动完成访问http://localhost:8080/
3.启动rocketmq
    先启动namesrv
    sh /Users/mc/rocketmq-all-4.8.0-bin-release/bin/mqnamesrv
    再启动broker
    sh /Users/mc/rocketmq-all-4.8.0-bin-release/bin/mqbroker -n localhost:9876 autoCreateTopicEnable=true
    再启动rocketmq管理平台
    java -jar /Users/mc/ideaworkspace/rocketmq-externals/rocketmq-console/target/rocketmq-console-ng-2.0.0.jar
    启动完成访问http://localhost:17890，检查rocketmq是否启动正常
4.启动zipkin
    java -jar /Users/mc/zipkin-server-2.23.2-exec.jar
    启动完成访问http://localhost:9411/
5.启动springbootadmin
    用idea打开springbootadmin项目
    启动前检查nacos的ip地址
    启动项目
    启动完成访问http://localhost:8888/admin
6.启动gateway网关
    用idea打开springbootadmin项目
    启动前检查nacos，zipkin的ip地址
    启动gateway项目，可启动多个实例
7.启动本项目
    可启动多个UserApplication，ContentApplication实例


