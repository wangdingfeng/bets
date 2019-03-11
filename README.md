# AdminBets 简称bets
#### 项目介绍
AdminBets是一个简单的脚手架权限管理系统。项目基础框架采用Spring Boot2.0.4，消除了繁杂的XML配置，使得二次开发更为简单；数据访问层采用Mybatis，同时引入了通用Mapper和PageHelper插件，可快速高效的对单表进行增删改查操作，消除了大量传统XML配置SQL的代码；安全框架采用时下流行的Apache Shiro，可实现对按钮级别的权限控制；前端页面使用基于Bootstrap的AdminLTE构建，响应式布局。框架主模块包含：系统管理、系统监控、任务调度。系统管理子模块--用户管理、部门管理、菜单管理、角色管理、字典管理、日志查询、连接池监视，实现权限精细控制。项目中还提供了接口限流、防xss攻击的功能，很适合新手学习。后期将会引入activiti工作流引擎、邮件模块、WebSocket通信。

在线演示 http://39.107.110.75
账号：AdminBets
密码：123456


### 技术选型
#### 环境要求
1. JDK 8
2. Tomcat 8
3. Apache Maven
#### 基础框架
1. Spring Boot
2. Apache Shiro
3. Redis
#### 持久层
1. Alibaba Druid
2. Apache MyBatis
2. Tk.MyBatis
3. Hibernate Validation
#### 视图层
1. Beetl模板引擎
2. CSS框架：Bootstrap 3.3  AdminLTE 2.4.8
3. 其他组件：jquery 1.12.4  jqGrid 4.7  jquery-zTree 3.5  jquery-toastr  jquery-validation  layer 3.1 webuploader  select2.4.0 cropper3.1.3

### 使用说明
1. 框架基于IntelliJ IDEA 开发
2. 初始化数据库 bets.sql
3. 数据库字符集：utf8   排序规则：utf8_bin
4. 项目依赖redis，所以请安装redis

### 特别感谢
项目中采用的table插件是由jeesite4二次封装的jqgrid插件，而且前端有的内容确实参考了jeesite4，所以页面布局会比较相似jeesite4。jeesite请移步：https://jeesite.gitee.io/
### 这个项目非常适合新手学习

### 预览
![image](https://github.com/wangdingfeng/bets/blob/master/img/WX20190310-142602%402x.png)
![image](https://github.com/wangdingfeng/bets/blob/master/img/WX20190310-142648%402x.png)
![image](https://github.com/wangdingfeng/bets/blob/master/img/WX20190310-142718%402x.png)
![image](https://github.com/wangdingfeng/bets/blob/master/img/WX20190310-142752%402x.png)
![image](https://github.com/wangdingfeng/bets/blob/master/img/WX20190310-142904%402x.png)
![image](https://github.com/wangdingfeng/bets/blob/master/img/WX20190310-142938%402x.png)
![image](https://github.com/wangdingfeng/bets/blob/master/img/WX20190310-142959%402x.png)
![image](https://github.com/wangdingfeng/bets/blob/master/img/WX20190310-143034%402x.png)
