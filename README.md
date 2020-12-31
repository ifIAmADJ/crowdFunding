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

我们并不会每一次都手动提取出 `war` 包然后通过命令行不断部署，而是通过 IntelliJ IDE 中进行。这需要两个步骤：

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

### 配置对页面的请求

打开 `sping-web-mvc.xml` 文件加入以下格式的配置。

```xml
<!--对于简单的页面请求，不需要 Controller，可以通过 SpringMVC 直接返回视图 -->
<mvc:view-controller path="/admin/to/login/page.html" view-name="admin-login"/>
```

`view-name` 对应着 `WEB-INF` 目录下的 `jsp` 文件。

### 配置 layer

## (拓展了解) Spring MVC 如何实现的参数绑定

参考以下连接：

[SpringMVC|参数绑定](https://www.jianshu.com/p/cc6b482d34b9)

[SpringMVC源码之参数解析绑定原理](https://www.cnblogs.com/w-y-c-m/p/8443892.html)

