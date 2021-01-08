# 众筹网

全程使用 IDEA IntelliJ 环境进行开发，此系统源自于尚硅谷机构的一个开源项目：尚筹网。

图片暂时使用 github 图床，可能需要 VPN 才可以加载。

## 项目架构

![frameworks](https://raw.githubusercontent.com/ifIAmADJ/imageRepository/master/frameworks.png)

## 项目开发

### 瀑布模型

把软件产品看成是一个工业化的标准品，以工厂流水线的思路进行开发。

![](https://raw.githubusercontent.com/ifIAmADJ/imageRepository/master/software_dev.png)

### 敏捷开发

敏捷开发是把一个软件产品看作是一个 "成长" 中的生物。

技术角度，根据需求开发出来可以运行的代码，根据需求开发原型，然后直接编码。

商业角度上，它加速了用户体验新功能的过程，"小步快跑"，更新的幅度虽然不大，但是更新的频率高。

## 环境搭建总目标

![env](https://raw.githubusercontent.com/ifIAmADJ/imageRepository/master/env.png)

后端项目目标

![back_end_frame](https://raw.githubusercontent.com/ifIAmADJ/imageRepository/master/back_end_frame.png)

以此对应的项目架构：
![逻辑架构](https://raw.githubusercontent.com/ifIAmADJ/imageRepository/master/%E9%80%BB%E8%BE%91%E6%9E%B6%E6%9E%84.png)

## ( 重要 ) 建立 Mybatis 逆向工程

> 该部分的参考资料来源：[Mybatis3详解（十三）——Mybatis逆向工程](https://www.cnblogs.com/tanghaorong/p/14013584.html)

Mybatis 允许根据数据库表来反向建立相关的 "JPA" 接口，该过程称之为 Mybatis 逆向工程 ( MBG, MyBatis Generator )。这节约了人工编写对 POJO 的各种繁杂 CRUD 的时间成本，之后只需要将生成的源码复制到需要的模块内即可。

该项目的文件结构如下：

```
src
  ├ main
  | └ java
  |   └ GeneratorLauncher		# 根据配置文件生成源代码的主程序
  └ resources
    ├ generatorConfig.xml		# 配置数据库信息及相关配置
    └ log4j.properties			# 配置 log4j 相关配置
```

pom 文件需要配置一个 `mybatis-generator-core` 包和一个插件。

```xml
    <!--依赖 MyBatis 核心包 -->
    <dependencies>
        <dependency>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-core</artifactId>
            <version>1.3.2</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.5</version>
                <configuration>
                    <!--MBG 需要一个配置文件，在此处指定。-->
                    <configurationFile>.\src\main\resources\generatorConfig.xml</configurationFile>
                    <verbose>true</verbose>
                    <overwrite>true</overwrite>
                </configuration>

                <!--如果自己的 MySQL 版本是 8.0 或以上，则需要 8.0.x 以上的版本-->
                <dependencies>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.21</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>

```

逆向工程的主要配置部分全部来自于 `generatorConfig.xml` ( 该文件名可自定义 ) 文件中，首先我们需要配置数据库驱动，数据库连接等基本信息，然后再决定生成

```xml
<!DOCTYPE generatorConfiguration PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>


    <context id="crowdFunding" targetRuntime="MyBatis3">

        <!-- 是否去掉自动生成的注释 true:是，false:否-->
        <commentGenerator>
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>

        <!-- 配置 JDBC 连接 -->
        <jdbcConnection
                driverClass="com.mysql.cj.jdbc.Driver"
                connectionURL="jdbc:mysql://localhost:3306/crowdfunding"
                userId="root"
                password="ljh20176714"
        />

        <!-- 实体类文件生成的位置 -->
        <javaModelGenerator
                targetPackage="com.iproject.crowd.entity"
                targetProject="..\crowdFunding02-admin-webUI\src\main\java">

            <!-- 是否对modal添加构造函数 -->
            <property name="constructorBased" value="true" />
            <!-- 是否让 schema 作为包的后缀 -->
            <property name="enableSubPackages" value="false"/>
            <!-- 从数据库返回的值被清理前后的空格 -->
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>

        <!-- Mapper 文件生成的位置-->
        <sqlMapGenerator
                targetPackage="mybatis.mapper"
                targetProject="..\crowdFunding02-admin-webUI\src\main\resources">
            <property name="enableSubPackages" value="false"/>
        </sqlMapGenerator>

        <!-- Mapper 接口生成的位置 -->
        <javaClientGenerator
                targetPackage="com.iproject.crowd.mapper"
                targetProject="..\crowdFunding02-admin-webUI\src\main\java"
                type="XMLMAPPER">
            <property name="enableSubPackages" value="false"/>
        </javaClientGenerator>

        <table tableName="t_admin" domainObjectName="Admin"/>
    </context>
</generatorConfiguration>
```

## 替换 Spring 日志记录系统

移除 `spring-orm` 下的 `common-logging` 包，新增以下配置：

```xml
<!-- Spring 依赖 -->
<!-- https://mvnrepository.com/artifact/org.springframework/spring-orm -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-orm</artifactId>
    <!--将 Spring 的 commons-logging 排除-->
    <exclusions>
        <exclusion>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<!--省略中间的部分-->
<!-- 日志 -->
<!-- 不使用此依赖 Spring 将使用默认的 commons-logging -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
</dependency>

<!-- 其他日志框架的中间转换包 -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>jcl-over-slf4j</artifactId>
</dependency>
```

## 声明式事务

编程式事务：

```java
try{
	// 前向通知
	dao.doSomething();
	// 返回通知
}catch(Exception e){
	// 异常通知
}finally{
	// 后置通知
}
```

## (问题) 有关于 Spring 接收 Get/Post 参数的注解

[springboot（服务端接口）获取URL请求参数的几种方法](https://www.cnblogs.com/zhanglijun/p/9403483.html)

[@requestBody 与@requestparam；@requestBody的加与不加的区别。](https://blog.csdn.net/jiashanshan521/article/details/88244735)

### 有无 @RequestBody 注解的区别

使用`@RequestBody`：当请求 content_type 为：`application/json` 类型的请求，数据类型为 json 时， json 格式如下：

```json
{
    "aaa":"111",
 	"bbb":"222"
}
```

不使用 `@requestBody`：当请求 content_type 为：application/x-www-form-urlencoded 类型的或 multipart/form-data 时，数据格式为 aaa=111&bbb=222。相当于 GET 附带 URL 参数或者是 POST 提交了一个 Form 表单。

## (问题) 有关于 Mybatis Mapper 无法自动装配的问题

当前的解决方案:

```java
@Autowire(required=false)
```

相关连接：

[SpringBoot整合MyBatis，service中导入mapper报错——Could not autowire.No beans of 'xxxMapper' type found.](https://chenchenchen.blog.csdn.net/article/details/88708354?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-4.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-4.control)

[小记：idea中springboot无法自动装配Could not autowire. No beans of 'UserMapper' type found. more...](https://blog.csdn.net/weixin_41935702/article/details/88852598?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1.control)

## (重要) 关于 Tomcat 的配置文件

这一小节探讨如何 Tomcat 如何启动 Spring MVC 项目。

想要将某个项目作为 JavaWeb 项目，可以在模块中右键选中 `Add Frameworks -> Web Application` 。

![add—frameworks_sup](https://raw.githubusercontent.com/ifIAmADJ/imageRepository/master/add_frameworks_sup.png)

[IntelliJ 官网提供的 doc ](https://www.jetbrains.com/help/idea/2018.1/add-frameworks-support-dialog.html)

这样，IDEA 将在该模块下新建一个 `web` 文件夹。该 `web` 文件夹会 "附赠" 以下内容：

```
web
  ├ WEB-INF      # 该文件夹的内容是受保护的，不可被外部浏览器直接访问。
  | └ web.xml    # 重要的配置文件
  └ index.jsp    # 默认的页面
```

其中，`web.xml` 是一项重要的配置文件，这个文件将 SpingMVC IOC 容器配置到 Tomcat 内。有三个核心的组件要在其中配置：`contextConfigLocation`，`CharacterEncodingFilter`，以及 `springDispatcherServlet` 。

//TODO 配置的具体细节略：

参考资料：

[通过tomcat容器启动spring容器的启动过程](https://blog.csdn.net/qq_31854907/article/details/86300901)

[【spring mvc】springmvc在tomcat中的执行过程](https://www.cnblogs.com/hantalk/p/6652967.html)

## (重要) 将项目打包

我们不必每一次更改完都手动提取出 `war` 包部署，而是在 IntelliJ IDE 中直接配置一个 tomcat 服务器并进行测试。这需要两个步骤：

1. 将某个模块**以及它依赖的模块**一同打为 `war` 包。
2. 在 IntelliJ IDEA 中配置 `war` 包在 tomcat 环境中测试。

第一步，在界面右上角中通过 `File -> Project Structure` 进入到项目设置中去，在 `Artifacts` 中进行配置。有三个主要问题需要考虑：

1. 将哪个模块打包；
2. 打什么包 (jar 包，war 包)；
3. 打包时有无其它的模块要作为依赖添加进去 ( 图示中的黄框部分 ) 。

![project_structure](https://raw.githubusercontent.com/ifIAmADJ/imageRepository/master/project_structure.png)

随后就是将该 `war` 包放入到 tomcat 服务器中去运行 ( 以配置 IntelliJ IDEA 的形式进行 ) 。选择界面右上角 ( `run` 图标旁边的 ) `edit configurations` 。

![edit_configurations](https://raw.githubusercontent.com/ifIAmADJ/imageRepository/master/edit_configurations.png)

配置完毕之后，就可以在 tomcat 中测试自己的 SpringMVC 项目了。IDEA 提供四种启动方式：

![4_updates](https://raw.githubusercontent.com/ifIAmADJ/imageRepository/master/4_updates.png)

以下四种方式的耗时逐项递增：

1. 如果只更改了 resources 文件 ( 这包括了项目中的 `.jsp`，`.html`，`.css`，和 `image` 等静态文件 ) ，则选择 `Update resources`。
2. 配置文件和字节码 ( 这只会简单地替换更新的字节码文件 ) 都需要更新，则选择 `Update classes and resources` ，但是，这取决于运行时是否能够及时将最新的字节码文件加载到其中。
3. 重新编译并且部署 `war` 包 ，则选择 `Redeploy` 。
4. 重启 tomcat 服务器，并且重新编译部署在其内的 `war` 包，则选择 `Restart server`。

## 前端页面相关

### base 标签

```jsp
<base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
```

一是保证我们能够正确的访问到指定映射的 Controller，二是保证了我们能够正确引入 `css`，`fonts` 资源目录下的各种文件。

### JSP-复用页面内容

> 这部分内容针对 jsp 开发而言。或许它对于前后端分离式开发已经过时了。

随着项目开发的进行，我们可以发现多个 `jsp` 页面总是要引入相同的 `<head>` 标签，如：

```jsp
<head>
    <meta charset="UTF-8">
	<%-- 其它 meta 标签--%>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/main.css">
    <%-- 可能引入的其它 js, css 等--%>
</head>
```

我们可以新建一个 `include-head.jsp` ( 文件名称自拟 ) 将这些标签保存起来，这样，别的 `.jsp` 页面只需要简易地引用它即可 ( [jsp 中的各种标签都是什么含义？](https://blog.csdn.net/dupengshixu/article/details/85256677) )：

```jsp
<%@incldue file="/WEB-INF/include-head.jsp" %>
```

对于其它可能被复用的组件 ( 如轮播图，导航栏等 ) 我们都可以类似的形式来复用它们。

### 配置对 (伪) 静态页面的请求

打开 `sping-web-mvc.xml` 文件加入以下格式的配置。

```xml
<!--对于简单的页面请求，不需要 Controller，可以通过 SpringMVC 直接返回视图 -->
<mvc:view-controller path="/admin/to/login/page.html" view-name="admin-login"/>
```

`view-name` 对应着 `WEB-INF` 目录下的 `jsp` 文件。

## (避坑) 两种统一异常处理方式——注意 ControllerAdvice 的使用细节

第一种处理方式，是使用 Spring 提供的 `@ControllerAdvice` 注解，将服务器运行期间所有抛出的异常都交付给某个专用类来处理。

使用这个方法之前，确保 `spring-web-mvc.xml` 中配置了 `<annotation:driven/>` 。

```java
// @ControllerAdvice 和 @ExceptionHandler 配合使用
@ControllerAdvice
public class XXXExceptionResolver {
    // 指定该方法 "抓包" 的异常类型。
    @ExceptionHandler(value = LoginFailureException.class)
    public ModelAndView resolveLoginFailureException(
            LoginFailureException loginFailureException,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        //TODO...
    }
}
```

不过一定要注意 ！！它只处理 `Controller` 抛出的异常。**如果一个请求没有经过 `Controller` 而是被 SpringMVC 直接转发，那么它的异常处理就无法被触发**。

典型的，上述在 `spring-web-mvc.xml` 中配置的 `<mvc:view-controller>` 就是不经过 `Controller` 处理的请求。因此我们要在该配置文件中注册一个 `simpleMappingExceptionResolver` ( 故名思意，简单映射异常处理类 ) 单独处理这类请求。

```xml
<!-- 基于 XML 的异常映射，针对于 mvc:view-controller 而言。-->
<bean id="simpleMappingExceptionResolver"
      class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
    <!--配置异常类型和具体视图页面的对应关系-->
    <property name="exceptionMappings">
        <props>
            <!--key属性指定异常全类名，可以是自定义的异常-->
            <!--标签体内写对应的视图名( 和 viewResolver 组合成具体的前后缀 )-->
            <prop key="com.iproject.crowd.exception.AccessForbiddenException">admin-login</prop>
            <prop key="java.lang.Exception">system-error</prop>
        </props>
    </property>
</bean>
```

这些异常都应该继承自 `RuntimeException` ( 它又继承自 `Exception` ) 。这样，在返回的 `jsp` 页面中，插入一段 EL 表达式 `${RequestScope.exception.message}` 就可以打印出相关异常的原因了。

## (拓展了解) Spring MVC 如何实现的参数绑定

参考以下连接：

[SpringMVC|参数绑定](https://www.jianshu.com/p/cc6b482d34b9)

[SpringMVC源码之参数解析绑定原理](https://www.cnblogs.com/w-y-c-m/p/8443892.html)

## (业务) 管理员登录

### 实现思路

以下是身份验证业务的实现逻辑：

```flow
st=>start: 前端点击登录页面
controller=>operation: Controller 接收账号密码
service=>operation: Service 实现身份验证逻辑
mapper=>operation: Mapper 封装实体向数据库请求 Admin
c1=>condition: 如果 Admin 为空
ex=>operation: 抛出 LoginFailureException 异常
c2=>condition: 如果数据库密码和提交的不一致
ifc2Yes=>end: 登录成功
advice=>operation: 由 ControllerAdvice 处理

st->controller->service->mapper->c1()
c1(yes)->ex->advice
c1(no)->c2()
c2(yes)->ifc2Yes
c2(no)->ex->advice
```

另外，为了保证的安全性，数据库不会以明文的形式存储账户的密码，而是会使用 MD5 ( 或者是其它方式 ) 对其进行加密。用户提交明文密码时，后端将采用相同的加密算法加密，并和数据库中存储的密文比对是否一致。

在登录验证成功后，后端返回视图模型 `main.jsp`，并且将用户信息作为 `Attribute` **存储到 Session 域中**。

加密的明文通过创建 MD5 工具方法来实现，它被存储到 `com.iproject.utils.Crowd` 包。

### 完善 1：避免刷新浏览器时重复提交表单 —— 重定向 

注意，即便是登录成功并直接返回了视图 `admin-main` ，我们也仍然处于 `/admin/do/login.html` 的位置。这会导致我们在此处刷新页面时，页面会相当于又一次提交了用户名表单到 `AdminCointroller` 中。

我们希望当用户登录成功之后，`Controller` 能够**重定向**到另一个代表用户主页的 `url` ，而不是仍然停留在请求登录的 `url` 。当然，新的 `url` 中仍然会携带用户的登录信息，因为我们将其存到了 `sessionScope` 当中。

将 `AdminController` 指定映射方法的返回值修改为重定向形式：

```java
// return "admin-main"
// 实际 SpringMVC 会直接返回 /WEB-INF/admin-main.jsp 视图，但是浏览器的 url 不变。

// 此时这个字符串将不是 "视图名"，而是 "伪" 静态页面的 url。
return "redirect:/admin/to/main/page.html"
```

然后在 `spring-web-mvc.xml` 配置这个伪静态页面 ( 实际上它是 `.jsp` 渲染的 ) 映射：

```xml
<mvc:view-controller path="/admin/to/main/page.html" view-name="admin-main"/>
```

### 完善 2：登出系统  —— 销毁 Session

退出登录的原理是，设置一个映射方法，该方法通过参数绑定的形式获取到 `Session` 对象，然后将其销毁即可。在注销完用户后，我们仍然通过重定向的方式返回到登录页面中。

```java
@RequestMapping("/admin/do/logout.html")
public String Logout(HttpSession session){
    //退出登录的原理是：只需要将 sessionScope 内的信息清除即可。
    session.invalidate();
    return "redirect:/admin/to/login/page.html";
}
```

同时，将页面内的 `herf` 做出设置：

```jsp
<base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
...
<%-- 
    浏览器结合 base 标签组装出的 url :
	http://localhost:8080/crowd/admin/do/logout.html
--%>
<a href="admin/do/logout.html">退出登录</a>
```

### 完善 3：登录状态检查 —— 拦截器实现

用户的请求可如下划分：

1. 请求公共资源。
2. 请求受保护的资源。

对于第一种请求，系统不要求用户统一进行登录。而对于第二种请求，系统则要求用户**是在已经登录的境况**下发出请求，或者说对于受保护的资源拒绝匿名请求，并强制登录。

显然，我们要对用户的请求进行拦截筛选。对于这种需求，可以通过配置拦截器来完成：

1. 实现指定功能的拦截器。
2. 在 `spring-web-mvc.xml` 文件中注册它。
3. 在对应的 `ControllerAdvice` 中处理拦截器可能抛出的异常。

这里需要继承一个 `org.springframework.web.servlet.handler.HandlerInterceptorAdapter` 类，并实现 `preHandle`  方法。

检查的思路是从请求 `request` 中 ( 通过参数绑定 ) 尝试获取 Session 域对象内的 `loginAdmin` 属性 ( 这个属性封装了一个 Admin 实体类对象 )，如果为空，则说明该请求没有携带登录信息。

```java
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. 尝试通过 Request 获取 Session 对象。
        Admin admin = (Admin)request.getSession().getAttribute(
                ProjectConstant.ATTR_NAME_LOGIN_ADMIN
        );

        // 2. 这个拦截器针对受保护的资源，因此一旦发现没有登录就返回 false.
        // 这个异常我们将抛给 com.iproject.crowd.mvc.config 下的 ExceptionResolver 来处理。
        if(admin == null) throw new AccessForbiddenException(ProjectConstant.MESSAGE_ACCESS_FORBIDDEN);

        // 返回 true 表示允许 (不拦截) 。
        return true;
    }
}
```

## (业务) 注册

目标是将表单提交的 Admin 保存起来，要求：

1. `loginAcct` ( 用户账号 ) 不允许重复。
2. 密码是经过 MD5 加密过的。 

假设注册页面是 `add.jsp` ，由于注册用户不需要携带额外数据，因此可以直接在 `view-controller` 当中进行配置。同时，为了保证账号的一致性，需要到数据库为其添加唯一约束。

如果采用命令行的方式，则：

```sql
alter table `crowdFunding`.`t_admin` Add unique index(`account`)
```

调整按钮和表单，之后，我们完善之前 `ServiceImpl` 的 `saveAdmin` 方法 ( 之前没有密码加密和时间戳 )

```java
@Override
public void saveAdmin(Admin admin) {

    // 使用 MD5 的加密串替换密码
    String safePwd = Md5Helper.md5(admin.getPwd());
    admin.setPwd(safePwd);

    Date createTime = new Date();
    admin.setCreateTime(createTime);

    adminMapper.insert(admin);
}
```

剩下的任务就是另 `ControllerResolver` 处理账号不唯一导致的异常 ( 这个错误将在 Mybatis 执行 SQL 语句的过程中抛出，如果不捕获会打印一大串信息 ) 

因此，`adminMapper.insert(admin)` 要使用一层 try-catch 语句包围：

```java
try {
    adminMapper.insert(admin);
} catch (Exception e) {

    LoggerFactory.getLogger(this.getClass()).warn(e.getMessage());
    if (e instanceof DuplicateKeyException) throw
        new AcctNotUniqueException(MESSAGE_LOGIN_ACCOUNT_ALREADY_IN_USE);
}
```

其中，`org.springframework.DuplicateKeyException` 是 Spring 框架捕获的异常。我们拦截此异常，并抛出我们的异常。 ( 还要在 `ControllerResolver` 那里注册一下 )

## (业务) 查询

在该业务中，我们需要使用 Mybatis 提供的分页插件来执行查询。确保依赖文件中有 Pagehelper：

```xml
<!-- MyBatis 分页插件 -->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
</dependency>
```

回到 `spring-mybatis.xml` 文件中，在 `SqlSessionFactoryBean` 项目中装配该插件：

```xml
<!--配置 sqlSessionFactoryBean整合Mybatis-->
<!--是 Spring 和 Mybatis 整合的重要工具-->
<bean id="sqlSessionFactoryBean" class="org.mybatis.spring.SqlSessionFactoryBean">

    <!--省略了之前的配置-->
    <!--配置 MyBatis 插件-->
    <property name="plugins">
        <!-- 插件可以有多个，因此这里实际上是一个数组。-->
        <array>
            <bean class="com.github.pagehelper.PageHelper">
                <property name="properties">
                    <props>
                        <!--配置数据库方言，告诉 Pagehelper 当前使用的数据库-->
                        <prop key="dialect">mysql</prop>

                        <!--
                            配置页码的合理化修正，在 1 ~ 总页数之间修正页码
                            这避免了用户特意传入不合理的页码数而引起的错误。
                        -->
                        <prop key="reasonable">true</prop>
                    </props>
                </property>
            </bean>
        </array>
    </property>
</bean>
```

在 `AdminMapper.xml` 当中 ( 这个文件是通过 MBG 逆向生成的 ) 编写 SQL 语句：

```xml
<!-- 插入分页查询 -->
<select id="selectAdminByKey" resultMap="BaseResultMap">
  select id,`account`,pwd,email,`name`,create_time
  from t_admin
  where `account` like concat('%',#{key},'%') or
   `email` like concat('%',#{key},'%') or
   `name` like concat('%',#{key},'%')
</select>
```

注：实际上，这个模糊查询的效率很低。将来我们会用 ElasticSearch 来解决这样的搜索问题。这里由于我们已经装配了 Pagehelper，因此不用再使用 `limit` 进行分页查询了。

不要忘记在对应的 `AdminMapper.java` 接口中添加对应的映射方法：

```java
//@Param 是 ibatis 中提供的注解。
List<Admin> selectAdminByKey(@Param("key") String key);
```

在 `Service` 层使用它，注意，这里的返回值不是单纯的 `List<Admin>` 而是 `PageInfo<Admin>`，原因是除了数据之外我们还需要封装其它信息，比如页码等等 ( 具体细节我们可以不去关心 ) ，这些全部装进了 `PageInfo 当中`。

```java
/**
 * @param keyword  搜索的关键字。
 * @param pageNum  搜索的页号
 * @param pageSize 一页显示多少内容？
 * @return 返回指定页号的内容。
 */
@Override
public PageInfo<Admin> getAdminPageInfo(String keyword, Integer pageNum, Integer pageSize) {

    // 1. 调用 PageHelper 的静态方法开启分页功能。
    // 这里充分体现了 PageHelper 的非侵入式设计，原本的业务不需要做任何修改。
    PageHelper.startPage(pageNum, pageSize);

    // 2. 执行查询
    List<Admin> admins = adminMapper.selectAdminByKey(keyword);

    // 3. 封装到 PageInfo 对象中。
    return new PageInfo<>(admins);
}
```

`Controller` 则需要对外部接收关键词，请求页数，每一页的显示数量了。

```java
@RequestMapping("/admin/get/page.html")
public String getAdminPageInfo(
        // 允许提供空的字符串值。
        @RequestParam(value = "keyword",defaultValue = "") String key,
        @RequestParam(value = "pageNumber",defaultValue = "1") Integer pageNumber,
        @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
        ModelMap modelMap
){

    PageInfo<Admin> adminPageInfo = adminService.getAdminPageInfo(key, pageNumber, pageSize);

    // jsp 可以通过 ${RequestScope.pageInfo.list} 获取。
    modelMap.addAttribute(ProjectConstant.ATTR_NAME_PAGE_INFO,adminPageInfo);
    retu rn "admin-page";
}
```

而在 `.jsp` 页面中则选择了使用 `jstl` 实现渲染：

```jsp
<%-- 导入 JSTL --%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
....
<%-- 通过 pageInfo.list 获取信息，admin 代表数据内的每一个元素 --%>
<c:forEach items="${requestScope.pageInfo.list}" var="admin">
```

在查询的表单中添加 `action` 和 `name` 等属性，以便于参数传递，同时将原来的 `button` 属性调整为 `submit `。

```jsp
<div class="..">
 	<%-- 重点1：补充 action--%>
    <form action="admin/get/page.html" class=".." role="form" style="..">
        <div class="..">
            <div class="..">
                <div class="..">查询条件</div>
	 			<%-- 重点2：补充输入框的 name = keyword--%>
                <input name="keyword" class=".." type="text" placeholder="请输入查询条件">
            </div>
        </div>
 		<%-- 重点3：补充 button 的属性为 submit--%>
        <button type="submit" class=".."><i class=".."></i> 查询</button>
    </form>
```

### 完善1：(前端) 分页导航栏

除了后端使用了 PageHelper 达成分页目的之外，前端同样也使用了来自 jQuery 的 pagination 工具。在主页面中将其引入样式和脚本文件：

```jsp
<link rel="stylesheet" href="css/pagination.css"/>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
```

编写分页逻辑：

```jsp
<script type="text/javascript">
    $(function(){
        // 调用后面声明的函数对页码导航条进行初始化操作
        initPagination();
    });
    // 生成页码导航条的函数
    function initPagination() {
        // 获取总记录数
        var totalRecord = ${requestScope.pageInfo.total};
        // 声明一个JSON对象存储Pagination要设置的属性
        var properties = {
            num_edge_entries: 3,                   // 边缘页数
            num_display_entries: 5,                // 主体页数
            callback: pageSelectCallback,          // 指定用户点击“翻页”的按钮时跳转页面的回调函数
            items_per_page: ${requestScope.pageInfo.pageSize},  // 每页要显示的数据的数量
            current_page: ${requestScope.pageInfo.pageNum - 1}, // Pagination 内部使用 pageIndex 来管理页码，pageIndex从0开始，pageNum从1开始，所以要减一
            prev_text: "上一页",                          // 上一页按钮上显示的文本
            next_text: "下一页"                           // 下一页按钮上显示的文本
        };
        // 生成页码导航条
        $("#Pagination").pagination(totalRecord, properties);
    }

    // 回调函数的含义：声明出来以后不是自己调用，而是交给系统或框架调用
    // 用户点击“上一页、下一页、1、2、3……”这样的页码时调用这个函数实现页面跳转
    // pageIndex是Pagination传给我们的那个“从0开始”的页码
    function pageSelectCallback(pageIndex, jQuery) {
        // 修正页号，pagination <=> mybatis pagehepler.
        var pageNum = pageIndex + 1;
        // 跳转页面，从 param 域中可以直接获取(上一个)请求域中携带的参数 keyword，这个参数会被继续携带至这一个转发的请求。
        window.location.href = "admin/get/page.html?pageNumber="+pageNum+"&keyword="+${param.keyword};
        // 由于每一个页码按钮都是超链接，所以在这个函数最后取消超链接的默认行为
        return false;
    }
</script>
```

核心语句是 `window.location.href`，它相当于跳转到下一个用户点击的页号上，除此之外还要携带关键字信息。

### 完善2：单点删除

首先，调整负责删除的按钮，用户点击按钮，即向服务器发送一个 RESTFul 请求：

```jsp
<%--这里使用了 RESTFul 风格的参数传递。--%>
<a class="btn btn-danger btn-xs" href="admin/remove/${admin.id}/${requestScope.pageInfo.pageNum}/${param.keyword}.html">
    <i class=" glyphicon glyphicon-remove"></i>
</a>
```

而后端的重点在于，如何返回这个视图 ( 直接返回 / 转发 / <u>重定向</u> )：

```java
@RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
public String remove(
        @PathVariable("adminId") Integer adminId,
        @PathVariable("pageNum") Integer pageNum,
        @PathVariable("keyword") String keyword
){
    // 执行删除
    adminService.remove(adminId);
    /*
    * 重点是页面跳转部分。
    *
    * 方案1: return "admin-page";
    * 这种方式没有在 admin-page 视图内设置任何数据 ( 参考 getAdminPageInfo ) 方法，因此页面会不显示数据。
    *
    * 方案2: return "forward:/admin/get/page.html"
    * 这种方式会重复做一次删除操作，没有必要。
    *
    * 方案3: return "redirect:/admin/get/page.html"
    *
    * */
    return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
}
```

而 Service 层的操作十分简单，仅仅是调用 `adminMapper.deleteByPrimaryKey(..)` ，因此在这里不赘述。

### 完善3：(业务) 修改管理员信息

修改指定的管理员信息，但是不修改密码和创建时间。

思路：`admin-page.jsp` ( 点击修改 ) > `admin-edit.jsp` ( 修改完毕 ) -> `AdminController(..)` (提交修改)

## (业务，重要) RBAC 模型

为什么要进行权限控制？如果不进行权限控制，则相当于人人都是 `root` 。整个系统的所有功能都会暴露给用户，这是不可以接受的。

RBAC 模型的通俗解释很简单，"用户是什么身份，他就能做哪些事"。权限控制，即 "权利" 加 "控制"。其本质目的是保护资源，形式上有很多，比如 `url`，`handler` 方法，`service` 方法等等。

一个 "权限" 通常涉及了多个资源的操作：

```
修改权限 => 访问用户组
	    => 修改界面
	    => 提交修改
```

比如对于一个完整的修改权限来说，需要访问三种资源。我们应当将这三个资源和此权限绑定到一起，这样当某个用户组有修改权限时，他自然就会有这些资源的使用权。

不同身份的角色，他能获取的权限也各不相同，而用户本身又能扮演多个角色。这建立了下面的关系：

```
权限(n) -> 资源(n)
	: 从权限可以对应找到资源，但是通过资源无法定位到权限。一个权限可以多个资源，同时多个权限可以复用资源。
角色(1) -> 权限(n)
	: 一个角色可以有多个权限。但是无法通过权限定位到角色。
用户(n) <-> 角色(n)
	: 一个角色可以由多个用户共同扮演，同样，一个用户也可以承担多个角色。
```

我们要根据这个理论建立对应的数据库表。

### RBAC 三层模型

RBAC，Role-based Access Control，一个用户可以有多个角色，一个角色可以有多个权限。

RBAC0，是最基本的 RBAC 模型。

RBAC1，在 RBAC0 的基础上增加了角色之间的继承关系。

RBAC2，是在 RBAC1 的基础上增加了责任分离机制，和 RBAC1 是平级的关系。他包含了静态和动态责任分离。

​	静态责任分离：

​		角色互斥：指角色之间存在互斥关系。比如说一个用户不能既是会计师 ( 记账 ) 又是审计师 ( 查账 ) 。

​		基数约束：一个角色可分配给的用户数量是受限的，一个管理员可获取的角色也是受限的。

​		先决条件角色：一个用户获取 A 角色之前必须要是 B 角色，保证拥有 X 权限之前必定有 Y 权限。比如：在给某人授								予 "金牌会员" 角色之前，TA 首先应该是 "银牌会员"，而不是 "普通会员"。

​	动态责任分离：

​		一个用户身兼多职，在特定场所下激活特定角色。

RBAC3，是 RBAC1 和 RBAC2 的组合。

## (后端) 实现批量删除

这里使用了 MGB 提供的接口。主要思路是通过 `RoleExample` 定义批量删除的所有 id ( 相当于在 SQL 语句中定义了 `delete from where ... in (...))  ，然后通过执行 `deleteByExample(..)` 方法来实现。

```java
@Override
public void removeRole(List<Integer> roleIdList) {

    // 新将一个 RoleExample
    RoleExample roleExample = new RoleExample();

    // 定义 Criteria
    RoleExample.Criteria criteria = roleExample.createCriteria();
    
    // 意为 "满足在该列表的所有 id " 都是被执行对象
    criteria.andIdIn(roleIdList);
    
    roleMapper.deleteByExample(roleExample);
}
```

