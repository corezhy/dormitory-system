# 1. 项目简介
一个基于Spring Boot和Vue.js的前后端分离宿舍管理系统，提供楼栋管理、宿舍分配、学生入住退宿、数据统计等完整功能。

# 2.功能特性
🏢 楼栋、楼层、宿舍、床位四级管理
👨‍🎓 学生信息全生命周期管理
📊 可视化数据统计与报表
🔐 JWT令牌认证与权限控制
📱 响应式前端设计

# 3.技术栈
后端：Spring Boot 3.x, MyBatis-Plus, JWT, Spring Validation
前端：Vue 3, Element Plus, ECharts, Axios
数据库：MySQL 8.0
工具：Maven, Knife4j(API文档)

# 4.快速开始
# (1)环境准备
必需软件 
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

# (2)数据库初始化
- 创建数据库
CREATE DATABASE dorm_system DEFAULT CHARACTER SET utf8mb4;
- 执行项目中的SQL脚本
- sql/schema.sql        (表结构)
- sql/init_data.sql     (演示数据)

# (3)后端配置启动
1.本地环境变量中配置阿里云OSS密钥：  
- 以管理员身份打开命令行，执行：    
set OSS_ACCESS_KEY_ID=your_key_id  
set OSS_ACCESS_KEY_SECRET=your_key_secret  
- 执行如下命令让更改生效：    
setx OSS_ACCESS_KEY_ID "%OSS_ACCESS_KEY_ID%"  
setx OSS_ACCESS_KEY_SECRET "%OSS_ACCESS_KEY_SECRET%"  
- 执行如下命令，验证环境变量是否生效：    
echo %OSS_ACCESS_KEY_ID%  
echo %OSS_ACCESS_KEY_SECRET%

2.修改dorm-management中的application.yaml.template：    
- 数据库连接信息
- aliyunOSS配置
- jwt配置

# (4)接口文档访问地址
- swagger: http://localhost:8080/swagger-ui.html
- knife4j: http://localhost:8080/doc.html#/home




