# 说明
根据  [官方wiki](https://github.com/MyCATApache/Mycat-Server/wiki)简单配置

版本为1.6-RELEASE.

# 配置

1. 下载并解压
```
wget http://dl.mycat.io/1.6-RELEASE/Mycat-server-1.6-RELEASE-20161028204710-linux.tar.gz
tar zxvf Mycat-server-1.6-RELEASE-20161028204710-linux.tar.gz
```
当前目录会有一个mycat文件夹

2. 配置数据库
使用Database/schema.sql创建两个数据库weibo_db1和weibo_db2
在weibo_db1里执行mycat/sequence.sql,这个用来实现mycat的自增主键和全局序列id

3. 修改配置文件
修改过的配置文件都在mycat/conf目录下,可以直接copy覆盖
下面列出各文件修改过的地方和简要说明:
(1)server.xml 
```
<!--数据库方式配置全局序列号 -->
		<property name="sequnceHandlerType">1</property>
```
从文档来看1.6默认就是这个
```
<system>
	 <property name="defaultSqlParser">druidparser</property>
</system>
```
注意:删除了原本默认的user标签
```
<user name="sa_weibo">
		<property name="password">123</property>
		<property name="schemas">weibo_db</property>
	</user>
```

(2)sequence_db_conf.properties
数据库方式配置全局序列号,配置了自增主键

(3) schema.xml
配置数据库
(4) rule.xml
配置分片规则
```
<!--sa-weibo项目的规则 myrule1用于user,weibo和log表 myrule2用于count表都
使用取模算法分片 -->
        <tableRule name="myrule1">
                <rule>
                        <columns>id</columns>
                        <algorithm>my-mod-long</algorithm>
                </rule>
        </tableRule>
        <tableRule name="myrule2">
                <rule>
                        <columns>weiboId</columns>
                        <algorithm>my-mod-long</algorithm>
                </rule>
        </tableRule>
```
```
	<function name="my-mod-long" class="io.mycat.route.function.PartitionByMod">
		<property name="count">2</property>
	</function>
```

3. 启动并测试
./mycat start 启动

./mycat stop 停止

./mycat console 前台运行

./mycat install 添加到系统自动启动（暂未实现）

./mycat remove 取消随系统自动启动（暂未实现）

./mycat restart 重启服务

./mycat pause 暂停

./mycat status 查看启动状态

然后使用客户端连接而是
`mysql -u root -p -P 8066 -h 127.0.0.1`


# 遇到的错误和问题解决办法

1. 常见的错误是xml配置文件编写错误,查看日志即可.
 需要注意的是同类型标签必须在一起,不能随便移位
 
2. 如果出现客户端(mysql命令 workbench)能够连接,但是java代码jdbc(包括idea内置的database功能)却连接不上,且报如下异常
```
com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure
The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
```
原因可能很多:https://stackoverflow.com/questions/6865538/solving-a-communications-link-failure-with-jdbc-and-mysql

但这里(客户端可以连接) 最可能是code代码中配置(__mycat的端口默认是8066__) 和 防火墙端口的原因
