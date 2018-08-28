# <center>基于SSM的CRUE项目</center>

[toc]

# 功能点

- 分页
- 数据校验
    - 前端: jQuery
    - 后台: JSR303
- ajax: 通过ajax进行CRUD操作
- REST风格的URL: 使用HTTP协议请求的动词
    - GET: 查询
    - POST: 新增
    - PUT: 修改
    - DELETE: 删除
---
# 技术点

- 基础框架: SSM(spring + SpringMVC + MyBatis)
- 数据库: Mysql
- 前端框架: Bootstrap
- 项目依赖管理: maven
- 分页: pagehelper
- 逆向工程:MyBatis Generator

---

# 环境搭建

## 搭建Maven工程

1. New Maven Project -> create a simple project -> next

2. Group Id: com.qpf | Artifact Id: ssm-crud | Packing: war -> finish

3. 右键Deployment Descriptor: ssm-crud -> Generate Deployment Descriptor Stub

## 引入依赖jar包

- spring: Spring JDBC - 4.3.7 | Spring Aspect - 4.3.7

- springmvc: sring webmvc - 4.3.7

- mybatis: Mybatis - 3.4.2 | MyBatis Spring - 

- 数据库连接池、驱动: C3P0 - 0.9.1 | MySQL -Connector/J - 5.1.41

- 其他: jstl - 1.2、servlet-api(scrop: provided) - 2.5、junit - 4.12

## 引入BootStrap和jQuery

- BootStrap: 下载bootstrap放到`src/main/webapp/static`目录下

- jQuery: 放到`src/main/webapp/static/js`目录下

- 页面引入jquery.js,bootstrap.min.css和bootstrap.min.js

## 编写整合SSM配置文件


- web.xml
    - 启动spring容器: `ContextLoaderListener`指定spring配置文件位置(`classpath:[spring配置].xml`)
    ```
    <!-- needed for ContextLoaderListener -->
    <context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:applicationContext.xml</param-value>
	</context-param>
	
	<!-- Bootstraps the root web application context before servlet initialization -->
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>
    ```
    - springmvc前端拦截器用于拦截前端请求: `dispatcherservlet`指定`springmvc`配置文件位置(`src/main/webapp/WEB-INF/[servlet名]-servlet.xml`[默认] | `classpath: [springmvc配置].xml`)
    
    ```
    <!-- The front controller of this Spring Web application, responsible for handling all application requests -->
	<servlet>
		<servlet-name>springDispatcherServlet</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath:springmvc.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>

	<!-- Map all requests to the DispatcherServlet for handling -->
	<servlet-mapping>
		<servlet-name>springDispatcherServlet</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
    ```
    
    - 字符编码过滤器: `CharacterEncodingFilter`
    ```
    <filter>
		<filter-name>characterEncodingFilter</filter-name>
		<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
		<init-param>
			<param-name>encoding</param-name>
			<param-value>utf-8</param-value>
		</init-param>
		<init-param>
			<param-name>forceRequestEncoding</param-name>
			<param-value>true</param-value>
		</init-param>
		<init-param>
			<param-name>forceResponseEncoding</param-name>
			<param-value>true</param-value>
		</init-param>
	</filter>
    ```
    
    - RUST风格URL过滤器: `HiddenHttpMethodFilter`
    ```
    <filter>
		<filter-name>hiddenHttpMethodFilter</filter-name>
		<filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
	</filter>
	
	<filter-mapping>
		<filter-name>hiddenHttpMethodFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
    ```

- spring: 业务逻辑
    - 扫描控制器以外的包:
        ```
        <context:component-scan base-package="com.qpf.urud">
		    <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
	    </context:component-scan>
        ```
    - 数据源: 
        - 数据库配置文件: `dbconfig.properties`:
        ```
        jdbc.jdbcUrl=jdbc:mysql://localhost:3306/ssm_crud
        jdbc.driverClass=com.,ysql.jdbc.Driver
        jdbc.user=root
        jdbc.password=123456
        ```
        - 定位数据库配置文件:
        ```
        <context: property-placeholder location="classpath:dbconfig.properties"/>
        ```
        - 数据源
        ```
        <bean id="pooledDataSource" class="..ComboPooledDataSource">
            <property name="jdbcUrl" value="${jdbc.jdbcUrl}"/>
            <property name="driverClass" value="${jdbc.driverClass}"/>
            <property name="user" value="${jdbc.user}"/>`
            <property name="password" value="${jdbc.password}"/>
        </context: property-placeholder>
        ```
    - 整合MyBatis:
        - `sqlSessionFactoryBean`:
        ```
        <bean id="sqlSessionFactoryBean" class="..SqlSessionFactoryBean">
            <!-- 指定MyBatis全局配置文件 -->
            <property name="configLocation" value="classpath:mybatis.xml"/>
            <!-- 数据源 -->
            <property name="dataSource" ref="pooledDataSource">
            <!-- MyBatis映射文件位置 -->
            <property name="mapperLocation" value="classpath:mapper/*.xml"/>
        </bean>
        ```
        - MyBatis映射扫描器: 将MyBatis接口的实现加入IOC容器
        ```
        <bean class="..MapperScannerConfigurer">
            <property name="basePackage" value="com.qpf.crud.dao"></property>
        </bean>
        ```
    - 事务控制:
        - 事务控制器:
        ```
        <bean id="transactionManager" class="..DataSourceTransactionManager">
            <property name="dataSource" ref="pooledDataSource"/>
        </bean>
        ```
        - 开启基于注解的事务和xml配置事务
        ```
        <aop:config>
            <!-- 切入点表达式: 所有访问权限,com.qpf.crud.service包下的所有类(包括子包下的类),所有方法,参数任意-->
            <aop:pontcut expression="execution(* com.qpf.crud.service..*(..))" id="txPoint" />
            <!-- 指定事务开启方式,事务增强 -->
            <aop:advisor advice-ref="txAdice" pointcut-ref="txPoint">
        </aop:config>
        ```
        - 配置事务增强,事务开启方式,并指定事务控制器
        
        ```
        <tx:advice id="txAdvice" transaction-manager="transactionManager">
            <tx:attributes>
                <!-- 所有方法 -->
                <tx:method name="*"/>
                <!-- 所有get前缀的方法,设为只读 -->
                <tx:method name="get*" read-only="true"/>
            </tx:attributes>
        </tx:advice>
        ```
    
- springmvc: 跳转逻辑
    - 扫描控制器:
    ```
    <context:component-scan use-default-filters="false">
        <context:include-filter type="annotation" expression="..Controller"/>
    </context:component-scan>
    ``` 
    - 视图解析器: 
    ```
    <bean class="..InternalResourceViewResolve">
        <property name="prefix" value="/WEB-INF/views/"/>
        <property name="suffix" value=".jsp"/>
    </bean>
    ```
    - 将springmvc不能处理的请求(静态资源)交给tomcat
    ```
    <mvc:default-servlet-handler/>
    ```
    - 支持springmvc的JSR303校验、ajax、映射动态请求等高级功能
    ```
    <mvc:annotation-driven/>
    ```

- mybatis
    - 驼峰命名规则
    ```
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
    ```
    - 类型别名
    ```
    <typeAliases>
        <package name="com.qpf.crud.bean" />
    </typeAliases>
    ```

---
## MyBatis逆向工程

生成bean、Dao接口以及mapper文件

1. 数据库生成表

2. pom.xml添加MyBatis Generator包

3. 工程目录下新建文件`mbg.xml`
    - 数据库连接
    
    ```
    <jdbcConnection
    driverClass="com.mysql.jdbc.Driver"
    connectionURL="jdbc:mysql://localhost:330/ssm_crud"
    userId="root"
    password="123456">
    </jdbcConnection>
    ```
    - 类型解析
    
    ```
    <javaTypeResolver>
        <property name="forceBigDecimals" value="false"/>
    </javaTypeResolver>
    ```
    
    - java模型生成
    
    ```
    <javaModelGenerator targetPackage="com.qpf.crud.bean" targetProject=".\src\main\java">
        <property name="enableSubPackages" value="true"/>
        <property name="trimsStrings" value="true"/>
    </javaModelGenerator>
    ```
    - sql映射文件生成
    
    ```
    <sqlMapGenerator targetPackage="mapper" targetProject=".\src\main\resources">
        <property name="enableSubPackages" value="true"/>
    </sqlMapGenerator>
    ```
    
    - dao接口生成
    
    ```
    <javaClientGenerator targetPackage="com.qpf.crud.dao" targetProject=".\src\main\java">
        <property name="enableSubPackages" value="true"/>
    </javaClientGenerator>
    ```
    
    - 表的生成策略
    
    ```
    <table tableName="empl" domainObjectName="Employee"></table>
    <table tableName="dept" domainObjectName="Department"></table>
    ```
    
    - 注释生成
    
    ```
    <commentGenerator>
        <!-- 无注释生成 -->
        <property name="supperessAllComments" value="true"/>
    </commentGenerator>
    ```
    
4. 生成程序

```
List<String> warnings = new ArrayList<String>();
boolean overwrite = true;
ConfigurationParser cp = new ConfigurationParser(warnings);
Configuration config = cp.parseConfiguration(
        Generator.class.getResourceAsStream("mbg.xml"));
DefaultShellCallback callback = new DefaultShellCallback(overwrite);
MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
myBatisGenerator.generate(null);
```