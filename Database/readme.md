# 说明
schema.sql 为数据库表结构sql
注意没有建表sql


建表语句
```
CREATE DATABASE  IF NOT EXISTS `weibo_db` /*!40100 DEFAULT CHARACTER SET utf8 */;
 USE `weibo_db`;
```

建立一个名为sa_weibo的用户 并把weibo_db数据库所有权限赋给sa_weibo这个用户.密码为123.
```
insert into mysql.user(Host,User,Password) values('%','sa_weibo',password('123'));
flush privileges;
grant all privileges on weibo_db.* to sa_weibo@'%' identified by '123';
flush privileges;
```



