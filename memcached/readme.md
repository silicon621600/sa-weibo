# 说明
    Memcached 是一个高性能的分布式内存对象缓存系统.
    Memcached基于一个存储键/值对的hashmap。其守护进程（daemon ）是用C写的.
    需要自己选择相应的客户端软件,可以用telnet访问memcached服务. 
    是否分布式取决于客户端的部署和实现.  

# 配置
1. 服务端
安装ubuntu下直接使用`sudo apt-get install memcached`安装
.启动memcache：
  ./memcached -help
  ./memcached -d -m 1024 -u root -p 11211 -c 1024 -p /tmp/memcached.pid 
   启动参数说明：
   -d   选项是启动一个守护进程，
   -m  是分配给Memcache使用的内存数量，单位是MB，默认64MB
   -M  return error on memory exhausted (rather than removing items)
   -u  是运行Memcache的用户，如果当前为root 的话，需要使用此参数指定用户。
   -l   是监听的服务器IP地址，默认为所有网卡。
   -p  是设置Memcache的TCP监听的端口，最好是1024以上的端口
   -c  选项是最大运行的并发连接数，默认是1024
   -P  是设置保存Memcache的pid文件
   -f   <factor>   chunk size growth factor (default: 1.25)
   -I   Override the size of each slab page. Adjusts max item size(1.4.2版本新增)
  也可以启动多个守护进程，但是端口不能重复
```
memcached -d  -u root -p 1624  -P /tmp/memcached.pid 
```
停止Memcache进程：
``` 
  kill `cat /tmp/memcached.pid` 
```


```
netstat -lp | grep memcached
```

2. 客户端

客户端有很多
这里使用 https://github.com/gwhalin/Memcached-Java-Client

文档 https://github.com/gwhalin/Memcached-Java-Client/blob/master/doc/HOWTO.txt
https://github.com/gwhalin/Memcached-Java-Client/blob/master/src/com/meetup/memcached/MemcachedClient.java
maven POM中添加依赖
```
<dependency>
    <groupId>com.whalin</groupId>
    <artifactId>Memcached-Java-Client</artifactId>
    <version>3.0.2</version>
</dependency>
```

根据文档编写类即可