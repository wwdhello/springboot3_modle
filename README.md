# 1.简介
本项目基于springboot+mybatisplus，集成了一般的springboot项目使用的基本功能*增删改查，上传文件，邮箱验证等*,同时包含一些redis的应用,项目只进行的接口的实现保证其有一定的可扩展性
## 环境
- Windows11
- springboot:3.4.3
- jdk:17.0.8
- 其他:redis 
- 数据库编码推荐 utf8mb4
- 规则推荐 utf8mb4_unicode_ci
- PostMan 请求为application/json
## 基本结构↓
```
springboot3_model/
├── .idea/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.wwdui.springboot3_model/
│   │   │       ├── Springboot3ModelApplication.java
│   │   │       ├── config/
│   │   │       │   ├── RedissonConfig.java
│   │   │       │   ├── FastJsonRedisSerializer.java
│   │   │       │   ├── JwtAuthenticationTokenFilter.java
│   │   │       │   ├── RedisConfig.java
│   │   │       │   └── SecurityConfig.java
│   │   │       ├── controller/
│   │   │       │   ├── GoodController.java
|   |   |       |   ├── AdminController.java
│   │   │       │   ├── FileController.java
│   │   │       │   └── UserController.java
│   │   │       ├── mapper/
│   │   │       │   ├── GoodMapper.java
│   │   │       │   ├── MenuMapper.java
│   │   │       │   ├── FileMapper.java
│   │   │       │   └── UserMapper.java
│   │   │       ├── pojo/
│   │   │       │   ├── Good.java
│   │   │       │   ├── GoodRequest.java
│   │   │       │   ├── Menu.java
│   │   │       │   ├── FileEntity.java
│   │   │       │   ├── FileRequest.java
│   │   │       │   ├── LoginUser.java
│   │   │       │   ├── PageRequest.java
│   │   │       │   ├── Result.java
│   │   │       │   ├── User.java
│   │   │       │   └── VerifyCodeRequest.java
│   │   │       ├── service/
│   │   │       │   └── impl/
│   │   │       │   |    ├── GoodServiceImpl.java
│   │   │       │   |    ├── FileServiceImpl.java
│   │   │       │   |    ├── SthServiceImpl.java
│   │   │       │   |    ├── UserServiceImpl.java
│   │   │       │   |    └── VerificationCodeServiceImpl.java
│   │   │       │   ├── FileService.java
│   │   │       │   ├── GoodService.java
│   │   │       │   ├── UserService.java
│   │   │       │   └── VerificationCodeService.java
│   │   │       └── util/
│   │   │           ├── JwtUtil.java
│   │   │           ├── RedisCache.java
│   │   │           └── WebUtils.java
│   │   └── resources/
│   │       ├── mapper/
│   │       │   ├── MenuMapper.xml
│   │       │   └── FileMapper.xml
│   │       └── application.yml
│   └── test/
│       └── java/
│           └── com.wwdui.springboot3_model/
└── target/
```
---

# 2.登录相关功能
## 2.1注册功能
 **创建实体类（User）**：表示用户数据  
 **创建数据访问层（UserMapper）**：用于与数据库交互  
 **创建服务层（UserServiceImpl）**：处理注册逻辑  
 **创建控制器（UserController）**：处理 HTTP 请求  
 **处理密码加密(WebConfig)**：使用 Spring Security 工具*BCryptPasswordEncoder*加密用户密码  
 **验证输入数据**：确保用户输入的数据符合要求  

---

## 2.2登录认证功能

##### 走spring security的登录流程,将几个重要的过滤器*UsernamePasswordAuthenticationFilter  UserDetails*的方法重写自定义登录的功能  

 **用户登录**：验证用户名和密码  
 **生成  Token (JwtUtil)**：登录成功后返回token,token传入redis中  
 **存入缓存(RedisCache)**: RedisCache对普通的RedisTemplate功能进行了完善  
 **验证 token (JwtUtil)**：后续请求需携带token  
 **过滤（SecurityConfig）**：允许注册和登录接口公开访问，同时保护其他接口  

---

## 2.3登出功能
 **用户登出**：验证用户名和密码  
 **验证 token (JwtUtil)**  
 **删除缓存**:将之前存入redis的用户缓存清除  

---

## 2.4用户信息
 **查询**:通过用户携带的token,解析id在redis中进行查询  
 *扩展性*:个人主页,改密码的信息比对等功能  

---

## 2.5邮箱验证
 **JavaMailSender**  
 **qq邮箱等**:通过IMAP/SMTP服务,将个人邮箱用于发验证码  
 **VerificationCodeServiceImpl**:进行邮箱的发送,验证码的生成(存入redis),验证码的比对  
 *扩展性*:邮箱的绑定,改密码时邮箱的安全认证等  

---

# 3.文件相关功能
## 3.1文件的秒传
 **创建实体类（FileEntity）**：文件数据,与用户userid绑定  
 **创建数据访问层（FileMapper）**：用于与数据库交互  
 **创建服务层（FileServiceImpl）**：(处理文件逻辑),本处将前端传递的md5值在redis中查询,若不存在则去数据库中查询,再存入缓存中  
 **创建控制器（FileController）**：处理 HTTP 请求  

---

## 3.2文件分片上传
 **FileServiceImpl**:对前端切割好的分片索引进行对齐并且合并,合并后存入数据库和redis中  

---

## 3.3文件列表
 **pagehelper**:使用pagehelper进行分页吗,传入页码和页大小  
 **FileServiceImpl**:获取用户userid,进行pagehelper分页操作  
 **FileMapper**:配置xml搜索条件,获取userid的所有文件信息  
 *扩展性*:用户上传的文件的下载等功能  

---

# 4.后续
## 这里将会对之前的一些不足进行优化  
### 2025.3.9 更新了基于RBAC权限模型的授权  
 **加入了Menu权限表和role角色表**  
 **项目结构图已更新**  
 
 ---
 
### 2025.3.11 更新了redisson分布式锁的应用  
 **加入了good商品表和user_good操作记录表**:为了实现分布式锁的应用,加入了需要高并发的操作如'商品的购买'    
 **项目结构图已更新**

 



