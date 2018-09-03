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

```
<!-- https://mvnrepository.com/artifact/org.springframework/spring-jdbc -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>4.3.18.RELEASE</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.springframework/spring-aspects -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>4.3.18.RELEASE</version>
</dependency>
```

- springmvc: sring webmvc - 4.3.7

```
<!-- https://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>4.3.18.RELEASE</version>
</dependency>
```

- mybatis: Mybatis - 3.4.2 | MyBatis Spring

```
<!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.4.2</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.mybatis/mybatis-spring -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>1.3.2</version>
</dependency>
```

- 数据库连接池、驱动: C3P0 - 0.9.1 | MySQL -Connector/J - 5.1.41

```
<!-- https://mvnrepository.com/artifact/c3p0/c3p0 -->
<dependency>
    <groupId>c3p0</groupId>
    <artifactId>c3p0</artifactId>
    <version>0.9.1</version>
</dependency>
<!-- mariadb -->
<!-- https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client -->
<!-- 
<dependency>
    <groupId>org.mariadb.jdbc</groupId>
    <artifactId>mariadb-java-client</artifactId>
    <version>2.2.5</version>
</dependency>
 -->
 <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.41</version>
</dependency>
```

- 其他: jstl - 1.2、servlet-api(scrop: provided) - 2.5、junit - 4.12

```
<!-- https://mvnrepository.com/artifact/jstl/jstl -->
<dependency>
    <groupId>jstl</groupId>
    <artifactId>jstl</artifactId>
    <version>1.2</version>
</dependency>
<!-- https://mvnrepository.com/artifact/javax.servlet/servlet-api -->
<!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.0.1</version>
    <scope>provided</scope>
</dependency>
<!-- https://mvnrepository.com/artifact/junit/junit -->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
</dependency>
```

## 引入BootStrap和jQuery

- BootStrap: 下载bootstrap放到`src/main/webapp/static`目录下

- jQuery: 放到`src/main/webapp/static/js`目录下

- 页面引入jquery.js,bootstrap.min.css和bootstrap.min.js

> 项目根路径为下面`/项目名称`,使用`request.getContextPath()`可以得到

```
<% pageContext.setAttribute("APP_PATH", request.getContextPath()) %>

<script type="text/javascript" src="${APP_PATH}/static/..."></script>
```

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
        # 指定utf-8编码
        jdbc.driverClass=com.mysql.jdbc.Driver?useUnicode=true&characterEncoding=utf-8
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
    <context:component-scan base-package="com.qpf.crud">
		<context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
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

```
-- 创建数据库时指定utf-8编码
-- CREATE DATABASE IF NOT EXISTS ssm DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

CREATE TABLE empl (
  empl_id int(11) auto_increment not null comment '员工id',
  empl_name varchar(255) not null comment '员工姓名',
  empl_gender char(1) not null comment '员工性别',
  empl_email varchar(255) not null comment '员工邮箱',
  dept_id int(11) not null comment '员工部门id',
  PRIMARY KEY (empl_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment = '员工表';

CREATE TABLE dept (
  dept_id int(11) auto_increment not null comment '部门id',
  dept_name varchar(255) not null comment '部门名称',
  PRIMARY KEY (dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment = '部门表';

ALTER TABLE empl ADD FOREIGN KEY (dept_id) REFERENCES dept(dept_id);
```

2. pom.xml添加MyBatis Generator包

```
<!-- https://mvnrepository.com/artifact/org.mybatis.generator/mybatis-generator-core -->
<dependency>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-core</artifactId>
    <version>1.3.5</version>
    <scope>test</scope>
</dependency>
```

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
	    <property name="suppressAllComments" value="true"/>
	</commentGenerator>
    ```
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
  PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
  "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
  <context id="DB2Tables" targetRuntime="MyBatis3">
  	<commentGenerator>
	    <!-- 无注释生成 -->
	    <property name="suppressAllComments" value="true"/>
	</commentGenerator>
    <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                    connectionURL="jdbc:mysql://116.85.54.176:3306/ssm?useSSL=false"
                    userId="mysql"
                    password="mysql">
    </jdbcConnection>

    <javaTypeResolver >
      <property name="forceBigDecimals" value="false" />
    </javaTypeResolver>

    <javaModelGenerator targetPackage="com.qpf.crud.bean" targetProject="./src/main/java">
      <property name="enableSubPackages" value="true" />
      <property name="trimStrings" value="true" />
    </javaModelGenerator>

    <sqlMapGenerator targetPackage="mapper"  targetProject="./src/main/resources">
      <property name="enableSubPackages" value="true" />
    </sqlMapGenerator>

    <javaClientGenerator type="XMLMAPPER" targetPackage="com.qpf.crud.dao"  targetProject="./src/main/java">
      <property name="enableSubPackages" value="true" />
    </javaClientGenerator>

    <table tableName="empl" domainObjectName="Employee"></table>
    <table tableName="dept" domainObjectName="Department"></table>

	
	
  </context>
</generatorConfiguration>
```
4. 生成程序

```
public class GenerateMapper {
	public static void main(String[] args) throws Exception{
	   List<String> warnings = new ArrayList<String>();
	   boolean overwrite = true;
	   // 工程目录下 MyBatis Generator 配置文件
	   File configFile = new File("mbg.xml");
	   ConfigurationParser cp = new ConfigurationParser(warnings);
	   Configuration config = cp.parseConfiguration(configFile);
	   DefaultShellCallback callback = new DefaultShellCallback(overwrite);
	   MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
	   myBatisGenerator.generate(null);
	   System.out.println("finish");
	}
}
```

5. `Employee`类添加`department`属性,封装部门信息

6. 新增查询结果封装`Department`接口

```
# com.qpf.crud.dao.EmployeeMapper
public interface EmployeeMapper {
    /** 列表查询员工并封装部门信息 **/
    List<Employee> selectByExampleWithDept(EmployeeExample example);
    
    /** 查询员工并封装部门信息 **/
    Employee selectByPrimaryKeyWithDept(Integer emplId);
}

# /src/main/resources/mapper/EmployeeMapper.xml

<resultMap id="BaseResultMapWithDept" type="com.qpf.crud.bean.Employee">
    <id column="empl_id" jdbcType="INTEGER" property="emplId" />
    <result column="empl_name" jdbcType="VARCHAR" property="emplName" />
    <result column="empl_gender" jdbcType="CHAR" property="emplGender" />
    <result column="empl_email" jdbcType="VARCHAR" property="emplEmail" />
    <result column="dept_id" jdbcType="INTEGER" property="deptId" />
    <association property="department" column="dept_id" javaType="com.qpf.crud.bean.Department">
    	<id property="deptId" column="dept_id"/>
    	<result property="deptName" column="dept_name"/>
    </association>
</resultMap>
<!-- 查询员工和部门信息 -->
<sql id="Base_Column_List_With_Dept">
    e.empl_id, e.empl_name, e.empl_gender, e.empl_email, e.dept_id, d.dept_id, d.dept_name
</sql>

<!-- 列表查询员工并封装部门信息 -->
<select id="selectByExampleWithDept" parameterType="com.qpf.crud.bean.EmployeeExample" resultMap="BaseResultMapWithDept">
    select
    <if test="distinct">
      distinct
    </if>
    <include refid="Base_Column_List_With_Dept" />
    from empl e 
    left join dept d on e.dept_id = d.dept_id
    <if test="_parameter != null">
      <include refid="Example_Where_Clause" />
    </if>
    <if test="orderByClause != null">
      order by ${orderByClause}
    </if>
</select>
<!-- 查询员工并封装部门信息 -->
<select id="selectByPrimaryKeyWithDept" parameterType="java.lang.Integer" resultMap="BaseResultMapWithDept">
    select 
    <include refid="Base_Column_List_With_Dept" />
    from empl e, dept d
    where e.dept_id = e.dept_id and empl_id = #{emplId,jdbcType=INTEGER}
</select>
```

## dao单元测试

1. 导入 spring test包
```
<!-- https://mvnrepository.com/artifact/org.springframework/spring-test -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>4.3.18.RELEASE</version>
    <scope>test</scope>
</dependency>
```
2. 测试类添加注`解@ContextConfiguration(location={"classpath:applicationContext.xml"})`
3. 类添加jUnit注解`@RunWith(SpringJUnit4ClassRunner.class)`
4. Mapper添加注解@AutoWired自动注入
```
@Autowired
DepartmentMapper departmentMapper;
@Autowired
EmployeeMapper employeeMapper;
```
5. 配置一个批量sqlSession,执行批量操作
    - `IOC`容器配置批量`sqlSession`
    ```
    <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
		<constructor-arg name="sqlSessionFactory" ref="sqlSessionFactoryBean"/>
		<constructor-arg name="executorType" value="BATCH"/>
	</bean>
    ```
    - 使用`sqlSession`
    ```
    // 1. 注入sqlSession
	@Autowired
	SqlSession sqlSession;
	
	public void test() {
	    // 2. 获取批量mapper
		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
		
		// 3. 批量插入
		for (int i = 0; i < 100; i++) {
			String name = UUID.randomUUID().toString().substring(0, 5) + i;
			mapper.insertSelective(new Employee(null, name, "M", name + "@qq.com", 2));
		}
	}
    ```
    
> 完整测试类代码

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class CrudTest {

	@Autowired
	DepartmentMapper departmentMapper;
	@Autowired
	EmployeeMapper employeeMapper;
	// 1. 注入sqlSession
	@Autowired
	SqlSession sqlSession;

	@Test
	public void testDept() {
//		departmentMapper.insertSelective(new Department(null, "Develop"));
//		departmentMapper.updateByPrimaryKey(new Department(2, "Test"));
		Department department = departmentMapper.selectByPrimaryKey(1);
		System.out.println(department);
	}
	
	@Test
	public void testempl() {
		Employee employee = new Employee(null, "Shadaileng", "M", "shadaileng@gmail.com", 1);
		// 新增
//		employeeMapper.insertSelective(employee);
		
		// 修改
//		employee.setEmplId(1);
//		employee.setDeptId(2);
//		System.out.println(employee);
//		employeeMapper.updateByPrimaryKey(employee);
		
		// 主键查询，未封装Department
//		employee = employeeMapper.selectByPrimaryKey(1);

		// 主键查询，封装Department
		employee = employeeMapper.selectByPrimaryKeyWithDept(1);
		System.out.println(employee);
		
		// 2. 获取批量mapper
//		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
		
		// 3. 批量插入
//		for (int i = 0; i < 100; i++) {
//			String name = UUID.randomUUID().toString().substring(0, 5) + i;
//			mapper.insertSelective(new Employee(null, name, "M", name + "@qq.com", 2));
//		}
		
		List<Employee> employees = null;
		// 列表查询,未封装Department
//		employees = employeeMapper.selectByExample(null);
		// 列表查询，封装Department
		employees = employeeMapper.selectByExampleWithDept(null);
		System.out.println(employees);
	}
}
```
# 分页查询

## 未分页显示
1. 新建`Employee`服务类`com.qpf.crud.service.EmployeeService`,并添加注解`@Service`


2. 自动注入`EmployeeMapper`,并编写获取所有`Employee`方法`getAll()`

```
@Service
public class EmployeeService {
	@Autowired
	EmployeeMapper employeeMapper;
	
	public List<Employee> getAll() {
		List<Employee> employees = employeeMapper.selectByExample(null);
		return employees;
	}
}
```

3. 新建`Employe`控制器`com.qpf.crud.controller.EmployeeController`,并添加注解`@Controller`

4. `Employee`列表显示,请求路径`/empls`,显示页面`list`

```
@RequestMapping("/empls")
	public String listEmpl() {
		
		return "list";
	}
```


5. 调用`EmployeeService`的`getAll()`方法获取`Employee`列表,并放入`Model`中

```
@RequestMapping("/empls")
	public String listEmpl(Model model) {
		
		List<Employee> employess = employeeService.getAll();
		
		model.addAttribute("employess", employess);
		
		return "list";
	}
```

## 分页显示

1. 导入`pageHellper`jar包

```
<!-- https://mvnrepository.com/artifact/com.github.pagehelper/pagehelper -->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>5.1.2</version>
</dependency>
```

2. `MyBatis`配置文件中配置`pageHelper`插件

> 在`<typeAliases>`标签后添加
```
<plugins>
	<plugin interceptor="com.github.pagehelper.PageInterceptor">
		<!-- 分页合理化参数，默认值为false。 -->
		<property name="reasonable" value="true"/>
	</plugin>
</plugins>
```

3. 在列表查询前设置分页,传入页码(`pageNum`)以及每页显示数据大小(`pageSize`)

4. 查询之后的结果使用`PageInfo`封装,封后的对象包含分页信息

```
@RequestMapping("/empls")
public String listEmpl(@RequestParam(name="pageNum", defaultValue="1") Integer pageNum, Model model) {
	
	// 在查询之前那设置分页,pageNum: 当前页码, pageSize: 每页显示的大小
	PageHelper.startPage(pageNum, 5);
	
	List<Employee> employess = employeeService.getAll();
	
	// 使用PageInfo封装查询结果,封装后的对象包含分页信息
	PageInfo<Employee> pageInfo = new PageInfo<Employee>(employess);
	model.addAttribute("pageInfo", pageInfo);
	
	return "list";
}
```

## Mock测试

> `Mock`测试需要`servlet-api`版本号`3.1.0`以上，加载`springmvc`配置文件

```
<!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.1.0</version>
    <scope>provided</scope>
</dependency>
```

1. 测试类添加注解`@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:springmvc.xml"})`,加载IOC容器和SpringMVC配置文件

2. 测试类添加注解`@RunWith(SpringJUnit4ClassRunner.class)`,使用`Spring test`测试模块

3. 测试类添加注解`@WebAppConfiguration`,自动注入上下文`WebApplicationContext`

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:springmvc.xml"})
@WebAppConfiguration
public class MvcTest {
	@Autowired
	private WebApplicationContext context;
}
```

4. 初始化`MockMvc`实例

```
private MockMvc mockMvc;
@Before
public void init() {
	mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
}
```

5. 使用`MockMvc`对象的`perform()`方法Http服务,并得到返回结果

6. 通过返回结果获取请求域,并得到请求域中的分页信息

```
MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/empls").param("pn", "1")).andReturn();
MockHttpServletRequest request = result.getRequest();
PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
```

7. 打印分页信息

```
int pageNum = pageInfo.getPageNum();
int pages = pageInfo.getPages();
long total = pageInfo.getTotal();
int[] navigatepageNums = pageInfo.getNavigatepageNums();

System.out.println("总页数: " + pages + " 当前: " + pageNum + " 总记录数: " + total);
System.out.println(Arrays.toString(navigatepageNums));

System.out.println("ID\tName\tGender\tEmail\tDepartment");
List<Employee> list = pageInfo.getList();
for (Employee employee : list) {
	System.out.println(employee.getEmplId() + "\t" + employee.getEmplName() + "\t" + employee.getEmplGender() + "\t" + employee.getEmplEmail() + "\t" + (employee.getDepartment() == null ? "--" : employee.getDepartment().getDeptName()));
}
```

> 完整测试类

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:springmvc.xml"})
@WebAppConfiguration
public class MvcTest {
	
	MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void testPages() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/empls").param("pn", "1")).andReturn();
		MockHttpServletRequest request = result.getRequest();
		PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
		
		int pageNum = pageInfo.getPageNum();
		int pages = pageInfo.getPages();
		long total = pageInfo.getTotal();
		int[] navigatepageNums = pageInfo.getNavigatepageNums();
		
		System.out.println("总页数: " + pages + " 当前: " + pageNum + " 总记录数: " + total);
		System.out.println(Arrays.toString(navigatepageNums));
		
		System.out.println("ID\tName\tGender\tEmail\tDepartment");
		List<Employee> list = pageInfo.getList();
		for (Employee employee : list) {
			System.out.println(employee.getEmplId() + "\t" + employee.getEmplName() + "\t" + employee.getEmplGender() + "\t" + employee.getEmplEmail() + "\t" + (employee.getDepartment() == null ? "--" : employee.getDepartment().getDeptName()));
		}
	}
}
```

## 编写列表页面

1. 页面头部引入`bootstrap`,声明`c`标签

```
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
    <% pageContext.setAttribute("APP_PATH", request.getContextPath()); %>
    <script type="text/javascript" src="${ APP_PATH }/static/js/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="${ APP_PATH }/static/js/popper.min.js"></script>
    <link rel="stylesheet" href="${ APP_PATH }/static/bootstrap-4.0.0-dist/css/bootstrap.min.css">
    <script type="text/javascript" src="${ APP_PATH }/static/bootstrap-4.0.0-dist/js/bootstrap.min.js"></script>
	<head>
<html>
```

2. 使用`bootstrap`的栅格布局,将页面分为4行

```
<!-- 栅格容器 -->
<div class="container">
    <!-- 标题 -->
	<div class="row"></div>
    <!-- 多选操作按钮 -->
	<div class="row"></div>
    <!-- 表格 -->
	<div class="row" style="height: 400px;"></div>
    <!-- 分页信息 -->
	<div class="row"></div>
	</div>
</div>
```

3. 标题行占用该行所有列,多选操作按钮占4列并列偏移8列

```
<div class="container">
	<div class="row">
		<div class="col-md-12">
			<h1>SSM-CRUD</h1>
		</div>
	</div>
	<div class="row">
		<div class="col-md-4 offset-md-8">
			<button class="btn btn-primary">新增</button>
			<button class="btn btn-danger">删除</button>
		</div>
	</div>
	<div class="row" style="height: 400px;">...</div>
	<div class="row">...</div>
</div>
```

4. 表格行,使用`<c:forEach>`标签遍历分页数据中的list

```
<c:forEach items="${pageInfo.list }" var="empl">

</c:forEach>
```

5. 使用`bootstrap`的分页组件

```
<nav aria-label="Page navigation example">
    <ul class="pagination">
      <li class="page-item"><a class="page-link"  href="${APP_PATH }/empls?pageNum=1">首页</a></li>
    <c:choose>
    	<c:when test="${pageInfo.hasPreviousPage }">
    	    <li class="page-item">
    	      <a class="page-link" href="${APP_PATH }/empls?pageNum=${pageInfo.pageNum - 1 }" aria-label="Previous">
    	        <span aria-hidden="true">&laquo;</span>
    	        <span class="sr-only">Previous</span>
    	      </a>
    	    </li>
    	</c:when>
        <c:otherwise>
    	    <li class="page-item disabled">
    	      <a class="page-link" href="#" aria-label="Previous">
    	        <span aria-hidden="true">&laquo;</span>
    	        <span class="sr-only">Previous</span>
    	      </a>
    	    </li>
        </c:otherwise>
    </c:choose>
    <c:forEach items="${pageInfo.navigatepageNums }" var="pageNum">
    	<c:choose>
    		<c:when test="${pageNum == pageInfo.pageNum }">
    			<li class="page-item active"><a class="page-link"  href="${APP_PATH }/empls?pageNum=${pageNum}">${pageNum}</a></li>
    		</c:when>
    		<c:otherwise>
    			<li class="page-item"><a class="page-link"  href="${APP_PATH }/empls?pageNum=${pageNum}">${pageNum}</a></li>
    		</c:otherwise>
    	</c:choose>
    </c:forEach>
    <c:choose>
    	<c:when test="${pageInfo.hasNextPage }">
    		<li class="page-item">
    	      <a class="page-link" href="${APP_PATH }/empls?pageNum=${pageInfo.pageNum + 1 }" aria-label="Next">
    	        <span aria-hidden="true">&raquo;</span>
    	        <span class="sr-only">Next</span>
    	      </a>
    	    </li>
    	</c:when>
        <c:otherwise>
        	<li class="page-item disabled">
    	      <a class="page-link" href="#" aria-label="Next">
    	        <span aria-hidden="true">&raquo;</span>
    	        <span class="sr-only">Next</span>
    	      </a>
    	    </li>
        </c:otherwise>
    </c:choose>
      <li class="page-item"><a class="page-link"  href="${APP_PATH }/empls?pageNum=${pageInfo.pages}">末页</a></li>
    </ul>
</nav>
```
> 完整的`list.jsp`源码

```
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>员工列表</title>
	<% pageContext.setAttribute("APP_PATH", request.getContextPath()); %>
	<script type="text/javascript" src="${ APP_PATH }/static/js/jquery-3.3.1.min.js"></script>
	<script type="text/javascript" src="${ APP_PATH }/static/js/popper.min.js"></script>
	<link rel="stylesheet" href="${ APP_PATH }/static/bootstrap-4.0.0-dist/css/bootstrap.min.css">
	<script type="text/javascript" src="${ APP_PATH }/static/bootstrap-4.0.0-dist/js/bootstrap.min.js"></script>
	<script>
		$(function() {
			console.log("enter list page ...")
		});
	</script>
	
	</head>
	<body>
		<div class="container">
			<div class="row">
				<div class="col-md-12">
					<h1>SSM-CRUD</h1>
				</div>
			</div>
			<div class="row">
				<div class="col-md-4 offset-md-8">
					<button class="btn btn-primary">新增</button>
					<button class="btn btn-danger">删除</button>
				</div>
			</div>
			<div class="row" style="height: 400px;">
				<div class="col-md-12">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>#</th>
								<th>Name</th>
								<th>Gender</th>
								<th>Email</th>
								<th>Department</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${pageInfo.list }" var="empl">
								<tr>
									<td>${empl.emplId }</td>
									<td>${empl.emplName }</td>
									<td>${empl.emplGender == "M" ? "男" : "女" }</td>
									<td>${empl.emplEmail }</td>
									<td>${empl.department.deptName }</td>
									<td>
										<button class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-pencil"></span>修改</button>
										<button class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-trash"></span>删除</button>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">共${pageInfo.pages }页,第${pageInfo.pageNum }页,共${pageInfo.total }条记录</div>
				<div class="col-md-6">
					<nav aria-label="Page navigation example">
					  <ul class="pagination">
					  	<li class="page-item"><a class="page-link"  href="${APP_PATH }/empls?pageNum=1">首页</a></li>
					    <c:choose>
					    	<c:when test="${pageInfo.hasPreviousPage }">
							    <li class="page-item">
							      <a class="page-link" href="${APP_PATH }/empls?pageNum=${pageInfo.pageNum - 1 }" aria-label="Previous">
							        <span aria-hidden="true">&laquo;</span>
							        <span class="sr-only">Previous</span>
							      </a>
							    </li>
					    	</c:when>
						    <c:otherwise>
							    <li class="page-item disabled">
							      <a class="page-link" href="#" aria-label="Previous">
							        <span aria-hidden="true">&laquo;</span>
							        <span class="sr-only">Previous</span>
							      </a>
							    </li>
						    </c:otherwise>
					    </c:choose>
					    <c:forEach items="${pageInfo.navigatepageNums }" var="pageNum">
					    	<c:choose>
					    		<c:when test="${pageNum == pageInfo.pageNum }">
					    			<li class="page-item active"><a class="page-link"  href="${APP_PATH }/empls?pageNum=${pageNum}">${pageNum}</a></li>
					    		</c:when>
					    		<c:otherwise>
					    			<li class="page-item"><a class="page-link"  href="${APP_PATH }/empls?pageNum=${pageNum}">${pageNum}</a></li>
					    		</c:otherwise>
					    	</c:choose>
					    </c:forEach>
					    <c:choose>
					    	<c:when test="${pageInfo.hasNextPage }">
					    		<li class="page-item">
							      <a class="page-link" href="${APP_PATH }/empls?pageNum=${pageInfo.pageNum + 1 }" aria-label="Next">
							        <span aria-hidden="true">&raquo;</span>
							        <span class="sr-only">Next</span>
							      </a>
							    </li>
					    	</c:when>
						    <c:otherwise>
						    	<li class="page-item disabled">
							      <a class="page-link" href="#" aria-label="Next">
							        <span aria-hidden="true">&raquo;</span>
							        <span class="sr-only">Next</span>
							      </a>
							    </li>
						    </c:otherwise>
					    </c:choose>
					  	<li class="page-item"><a class="page-link"  href="${APP_PATH }/empls?pageNum=${pageInfo.pages}">末页</a></li>
					  </ul>
					</nav>
				</div>
			</div>
		</div>
	</body>
</html>
```

# 返回Json数据

1. 导入`jackson-databind`包

```
<!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.5</version>
</dependency>
```

2. 封装通用返回消息类`Msg`

```
public class Msg {
	/** 100 - 成功 \n200 - 失败 **/
	private String code;
	/** 描述 ***/
	private String desc;
	/** 绑定数据  **/
	Map<String, Object> data = new HashMap<String, Object>();
	
	public static Msg success() {
		return new Msg("100", "成功");
	}
	public static Msg fail() {
		return new Msg("200", "失败");
	}
	public Msg desc(String desc) {
		this.desc = desc;
		return this;
	}
	// 链式绑定数据
	public Msg add(String key, Object value) {
		this.data.put(key, value);
		return this;
	}
	public Msg() {}
	public Msg(String code, String desc) {
		super();
		this.code = code;
		this.desc = desc;
	}
	// getter setter
}
```

3. 控制器添加一个json处理方法,添加注解`@ResponseBody`

```
@ResponseBody
@RequestMapping("/empls2json")
public Msg listEmpl2Json(@RequestParam(value="pageNum", defaultValue="1") Integer pageNum) {
	
	// 在查询之前那设置分页,pageNum: 当前页码, pageSize: 每页显示的大小
	PageHelper.startPage(pageNum, 5);
	
	List<Employee> employess = employeeService.getAll();
	
	// 使用PageInfo封装查询结果,封装后的对象包含分页信息
	PageInfo<Employee> pageInfo = new PageInfo<Employee>(employess, 5);
	
	return Msg.success().add("pageInfo", pageInfo);
}
```

# 前端使用Vue.js

1. 新建文件`/src/main/webapp/static/js/vue.js`,用于编写`vue`组件.

2. 导入`vue.js`以及组件文件

```
<script src="https://cdn.bootcss.com/vue/2.4.2/vue.min.js"></script>
<script type="text/javascript" src="${ APP_PATH }/static/js/vue.js"></script>
```

3. 初始化`vue`

```
$(function() {
   var vm = new Vue({
        el: '#app',
        data: {}
})
```

4. 组件都放在容器`<div class="container" id="app"></div>`内

## table_com组件

1. `vue.js`文件编写`table_com`组件

```
Vue.component('table_com', {
		template: `
		<div class="col-md-12">
		    <table class="table table-hover">
    			<thead>
    				<tr>
    				    <th>#</th><th>Name</th><th>Gender</th><th>Email</th><th>Department</th><th>操作</th>
    				</tr>
    			</thead>
					<tbody>
						<tr v-for="item in list">
							<td>{{ item.emplId }}</td>
							<td>{{ item.emplName }}</td>
							<td>{{ item.emplGender === "M" ? "男" : "女" }}</td>\
							<td>{{ item.emplEmail }}</td>
							<td>{{ item.department && item.department.deptName }}</td>
							<td>
								<button class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-pencil"></span>修改</button>
								<button class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-trash"></span>删除</button>
							</td>
						</tr>
					</tbody>
				</table>
			</div>`,
		props: ['list'],
	})
```

2. `list`界面使用`table_com`组件用于显示后台返回的员工列表数据,`vue`对象`vm`添加数据`list`,并绑定`table_com`组件中

```

{
    data: {
        list: []
    }
}

<div class="row" style="height: 400px;">
	<table_com :list="list"></table_com>
</div>
```

## 分页组件`pagination`

1. `vm`添加对象`page`存储分页信息

```
{
    data: {
        page: {
			pageNum: '',
			pages: '',
			total: '',
			hasPreviousPage: '',
			hasNextPage: '',
			navigatepageNums: []
		}
    }
}
```

2. 编写`pagination`,显示分页信息以及操作分页查询

```
Vue.component('pagination', {
	template: `
		<div class="row">
			<div class="col-md-6 text-center">共 {{ page.pages }} 页,第{{ page.pageNum }}页,共{{ page.total }}条记录</div>
			<div class="col-md-6">
				<nav aria-label="Page navigation">
					<ul class="pagination">
						<li><a href="#" :nav="1" @click="pageTo">首页</a></li>
						<li>
							<a href="#" :class="{disabled : !page.hasPreviousPage}" :nav="page.pageNum - 1" @click="pageTo"aria-label="Previous">
								<span aria-hidden="true">&laquo;</span>
							</a>
					    </li>
						<li v-for="nav in page.navigatepageNums" :class="{active : page.pageNum === nav}"><a href="#" :nav="nav" @click="pageTo">{{ nav }}</a></li>
						<li>
							<a href="#" aria-label="Next"  :nav="page.pageNum + 1" @click="pageTo">
								<span aria-hidden="true">&raquo;</span>
							</a>
						</li>
						<li><a href="#" :nav="page.total" @click="pageTo">末页</a></li>
					</ul>
				</nav>
			</div>
		</div>`,
	props: ['page'],
	methods: {
		pageTo: function (event) {
			var event = event || window.event
			event.preventDefault()
			var nav = event.currentTarget.attributes["nav"].value
			this.$emit('pageto', nav)
			return false;
		}
	}
})
```

> `pageTo`函数是分页操作的回调函数,触发自定义事件`pageto`,将跳转页码传到父组件

3. `Vue`对像调后台分页查询接口,返回结果修改`page`属性和`list`属性

```
methods: {
    getEmpls: function (pageNum) {
    	$.ajax({
    		url: '${ APP_PATH }/empls2json?pageNum=' + pageNum,
    		method: 'GET',
    		success:  (result) => {
    			var pageInfo = result && result.data && result.data && result.data.pageInfo
    			this.list = pageInfo && pageInfo.list
    			this.page = {
    				pageNum: pageInfo.pageNum,
    				pages: pageInfo.pages,
    				total: pageInfo.total,
    				hasPreviousPage: pageInfo.hasPreviousPage,
    				hasNextPage: pageInfo.hasNextPage,
    				navigatepageNums: pageInfo.navigatepageNums
    			}
    		}
    	})
    }
}
```

4. `list`界面使用`pagination`组件,绑定`page`属性,设置`pageto`事件回调函数查询数据

```
<pagination @pageto="getEmpls" :page="page"></pagination>
```
## `modalpanel`模态框组件

1. 编写组件

```
Vue.component('modalpanel', {
		template: `
			<div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="exampleModalLabel">{{ title }}</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
				<form>
					<div class="form-group row" >\
						<label for="emplName" class="col-md-2 control-label text-center">姓名</label>
						<div class="col-md-8" :class="[name_status.status?`has-success`:`has-error`]">
							<input type="text" class="form-control" placeholder="5到16位英文或3到6位中文" v-model="emplName" name="emplName" 
							@keyup="validateName" @blur="hide_tips"
							data-toggle="popover" data-placement="top" 
							data-trigger="manual" data-delay="{show: 500, hide: 100}">
						</div>
					</div>
					<div class="form-group row">
						<label for="emplEmail" class="col-md-2 control-label text-center">邮箱</label>
						<div class="col-md-8" :class="[email_status.status?`has-success`:`has-error`]">
							<input type="email" class="form-control" placeholder="yourname@example.com" v-model="emplEmail" name="emplEmail" 
							@keyup="validateEmail" @blur="hide_tips"
							data-toggle="popover" data-placement="top" 
							data-trigger="manual" data-delay="{show: 500, hide: 100}">
						</div>
					</div>
					<div class="form-group row">
						<label for="emplGender" class="col-md-2 control-label text-center">性别</label>
						<div class="col-md-8">
							<div class="form-check form-check-inline col-md-2">
							  <input class="form-check-input" type="radio" name="emplGender" value="M" checked v-model="emplGender">
							  <label class="form-check-label" for="inlineRadio1">男</label>
							</div>
							<div class="form-check form-check-inline col-md-2">
							  <input class="form-check-input" type="radio" name="emplGender" value="F" v-model="emplGender">
							  <label class="form-check-label" for="inlineRadio2">女</label>
							</div>
						</div>
					</div>
					<div class="form-group row">
						<label for="deptId" class="col-md-2 control-label text-center">部门</label>
						<div class="col-md-4">
							<select id="inputState" class="form-control"  v-model="deptId">
								<option v-for="(dept, i) in depts" :key="i" :value="dept.deptId" >{{ dept.deptName }}</option>
							</select>
						</div>
					</div>
				</form>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
		        <button type="button" class="btn btn-primary" @click="insert">{{ opt }}</button>
		      </div>
		    </div>
		`,
		props: ['title', 'opt', 'depts', 'name_status', 'email_status', 'data'],
		data: function () {
			return {
				emplName: '',
				emplEmail: '',
				emplGender: '',
				deptId: '',
			}
		},
		methods: {
			hide_tips: function (e) {
				$(e.currentTarget).popover('hide')
			},
			validateName: function () {
				this.$emit('validate_name', this.$data.emplName)
			},
			validateEmail: function () {
				this.$emit('validate_email', this.$data.emplEmail)
			},
			insert: function () {
				// 1. 数据校验
				if (!this.name_status.status || !this.email_status.status){
					$('[name="emplName"]').attr('data-content', this.name_status.desc)
					$('[name="emplName"]').popover('show')
					$('[name="emplEmail"]').attr('data-content', this.email_status.desc)
					$('[name="emplEmail"]').popover('show')
					return
				}
				// 2. 封装数据
				var data = {
					emplName: this.$data.emplName,
					emplEmail: this.$data.emplEmail,
					emplGender: this.$data.emplGender,
					deptId: this.$data.deptId
				}
				// 3. 触发事件
				this.$emit('insert', data)
			}
		},
		watch: {
			name_status: function() {
				$('[name="emplName"]').attr('data-content', this.name_status.desc)
				$('[name="emplName"]').popover('show')
			},
			email_status: function() {
				$('[name="emplEmail"]').attr('data-content', this.email_status.desc)
				$('[name="emplEmail"]').popover('show')
			},
			data: function () {
				this.emplName = this.data.emplName
				this.emplEmail = this.data.emplEmail
				this.emplGender = this.data.emplGender
				this.deptId = this.data.deptId
			}
		}
	})
```

2. `Vue`对象添加数据

```
{
    data: {
        modal: {
			depts: [],
			name_status: {
				status: false,
				desc: ''
			},
			email_status: {
				status: false,
				desc: ''
			},
			modal_data: {
				emplName: '',
				emplEmail: '',
				emplGender: '',
				deptId: '',
			}
		}
    }
}
```

3. `list`使用`modal_panel`组件,新增按钮显示模态框,并查询部门数据更新`depts`

```
<button class="btn btn-primary" data-toggle="modal" data-target="#modal_panel" @click="enter_add_panel">新增</button>
<div class="modal fade" id="modal_panel" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  	<div class="modal-dialog" role="document">
  		<modalpanel 
  			title="新增用户" 
  			opt="保存" 
  			:depts="modal.depts" 
 			:name_status="modal.name_status" 
 			:email_status="modal.email_status"
 			:data="modal.modal_data"
 			@validate_name="validate_name"
 			@validate_email="validate_email"
 			@insert="insert"></modalpanel>
  	</div>
  </div>
```

4. `Vue`对象添加方法`validate_name`、`validate_email`和`insert`验证用户名和电子邮箱

```
methods: {
	getDepts: function () {
		$.ajax({
			url: '${ APP_PATH }/depts2json',
			method: 'GET',
			success:  (result) => {
				this.depts = result && result.data && result.data.depts
			}
		})
	},
	enter_add_panel: function () {
		this.modal.modal_data = {
			emplName: '',
			emplEmail: '',
			emplGender: '',
			deptId: '',
		}
		this.getDepts()
	},
	validate_name: function (name) {
    	var reg = /(^[\u2E80-\u9FFF0-9a-zA-Z]{3,10}$)/ 
    	if (reg.test(name)){
    		$.ajax({
    			url: '${ APP_PATH }/validateName',
    			method: 'POST',
    			data: {name: name},
    			success:  (result) => {
    				if (result && result.code === "100") {
    					// 用户名可用
    					this.modal.name_status = {
    						status: true,
    						desc: result.desc
    					}
    				} else {
    					// 用户名不可用
    					this.modal.name_status = {
    						status: false,
    						desc: result.desc
    					}
    				}
    			}
    		})
    	} else {
    		// 用户名5到16位英文或3到6位中文
    		this.modal.name_status = {
    			status: false,
    			desc: '用户名3到10字符'
    		}
    	}
    	
    },
    validate_email: function(email) {
    	var reg = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/
    	if (reg.test(email)){
    		this.modal.email_status = {
    			status: true,
    			desc: '邮箱可用'
    		}
    	} else {
    		this.modal.email_status = {
    			status: false,
    			desc: '邮箱格式不正确'
    		}
    	}
    },
    insert: function (data) {
    	$.ajax({
    		url: '${ APP_PATH }/empl',
    		method: 'POST',
    		data: data,
    		success:  (result) => {
    			// 1. 是否插入成功
    			if (result.code === "200") {
    				// 2. 失败 - 显示失败信息
    				var errors = result && result.data && result.data.errors
    				if (errors.emplEmail) {
    					this.modal.email_status = {
    						status: false,
    						desc: errors.emplEmail
    					}
    				}
    				if (errors.emplName) {
    					this.modal.name_status = {
    						status: false,
    						desc: errors.emplName
    					}
    				}
    			} else {
    				// 3. 显示最后一页
    				this.getEmpls(this.page.total)
    				// 4. 成功 - 关闭模态框
    				$('#modal_panel').modal('hide')
    				// 5. 清空模态框
    				this.modal.modal_data = {
    					emplName: '',
    					emplEmail: '',
    					emplGender: '',
    					deptId: '',
    				}
    			}
    		}
    	})
    }
}

# com.qpf.crud.controller.EmployeeController
@Controller
public class EmployeeController {
	@Autowired
	EmployeeService employeeService;
		@ResponseBody
	@RequestMapping(value="/empl", method=RequestMethod.POST)
	public Msg insertEmpl(Employee employee) {
		employeeService.saveEmpl(employee);
		
		return Msg.success().desc("保存成功");
	}

	@ResponseBody
	@RequestMapping(value="/validateName", method=RequestMethod.POST)
	public Msg insertEmpl(String name) {
		boolean bool = employeeService.validateName(name);
		if (bool) return Msg.success().desc("用户名可用");
		return Msg.fail().desc("用户名不可用");
	}
}
# com.qpf.crud.controller.DepartmentController
@Controller
public class DepartmentController {
	@Autowired
	DepartmentService departmentService;
	@ResponseBody
	@RequestMapping("/depts2json")
	public Msg getAll2Json() {
		List<Department> depts = departmentService.getAll();
		return Msg.success().add("depts", depts);
	}
}

# com.qpf.crud.service.EmployeeService
@Service
public class EmployeeService {
	@Autowired
	EmployeeMapper employeeMapper;

	public int saveEmpl(Employee employee) {
		int save = employeeMapper.insertSelective(employee);
		return save;
	}

	public boolean validateName(String name) {
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmplNameEqualTo(name);
		long count = employeeMapper.countByExample(example);
		return count <= 0;
	}
}

# com.qpf.crud.service.DepartmentService
@Service
public class DepartmentService {
	@Autowired
	DepartmentMapper departmentMapper;

	public List<Department> getAll() {
		List<Department> depts = departmentMapper.selectByExample(null);
		return depts;
	}
}
```

# JSR303验证

> `tomcat-7.0`以下的版本需要更新最新的`el`包才能使用`JSR303`验证

1. 导入`hibernate-validator`包

```
<!-- https://mvnrepository.com/artifact/org.hibernate/hibernate-validator -->
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>5.4.1.Final</version>
</dependency>
```

2. `Emplyee`类的`emplName`和`emplEmail`属性添加`@Pattern`注解

```
# com.qpf.crud.bean.Employee
public class Employee {
    @Pattern(regexp="(^[\\u2E80-\\u9FFF0-9a-zA-Z]{3,10}$)", message="用户名3到10字符")
    private String emplName;
    
    @Pattern(regexp="^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$", message="邮箱格式不正确")
    private String emplEmail;
}
```

3. 控制器验证输入对象

```
# com.qpf.crud.controller.EmployeeController

@Controller
public class EmployeeController {
	@Autowired
	EmployeeService employeeService;
	
	@ResponseBody
	@RequestMapping(value="/empl", method=RequestMethod.POST)
	public Msg insertEmpl(@Valid Employee employee, BindingResult result) {
		if (result.hasErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (FieldError fieldError : result.getFieldErrors()) {
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errors", map);
		} else {
			employeeService.saveEmpl(employee);
			return Msg.success().desc("保存成功");
		}
	}
}
```

4. 前端`Vue`对象处理错误信息

```
{
    methods: {
        insert: function (data) {
			$.ajax({
				url: '${ APP_PATH }/empl',
				method: 'POST',
				data: data,
				success:  (result) => {
					// 1. 是否插入成功
					if (result.code === "200") {
						// 2. 失败 - 显示失败信息
						var errors = result && result.data && result.data.errors
						if (errors.emplEmail) {
							this.modal.email_status = {
								status: false,
								desc: errors.emplEmail
							}
						}
						if (errors.emplName) {
							this.modal.name_status = {
								status: false,
								desc: errors.emplName
							}
						}
					} else {
						// 3. 显示最后一页
						this.getEmpls(this.page.total)
						// 4. 成功 - 关闭模态框
						$('#modal_panel').modal('hide')
						// 5. 清空模态框
						this.modal.modal_data = {
							emplName: '',
							emplEmail: '',
							emplGender: '',
							deptId: '',
						}
					}
				}
			})
		}
    }
}
```

# 修改员工

1. 修改员工模态框

```
<div class="modal fade" id="modal_panel_update" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
  	<modalpanel 
  		title="更新用户" 
  		opt="更新" 
  		:depts="modal.depts" 
 		:name_status="modal.name_status" 
 		:email_status="modal.email_status"
 		:data="modal.modal_data"
 		@validate_name="validate_name"
 		@validate_email="validate_email"
 		@insert="update">
 	</modalpanel>
  </div>
</div>
```

2. `Vue`对象添加更新方法,根据`emplId`查询`Employee`方法,控制器添加修改`Employee`方法

```
{
    methods: {
        update: function (data) {
		//	data._method = "PUT" // 发送POST请求需要添加这个参数
			$.ajax({
				url: '${ APP_PATH }/empl/' + data.emplId,
				method: 'PUT',
				data: data,
				success:  (result) => {
					// 1. 是否插入成功
					if (result.code === "200") {
						// 2. 失败 - 显示失败信息
						var errors = result && result.data && result.data.errors
						if (errors.emplEmail) {
							this.modal.email_status = {
								status: false,
								desc: errors.emplEmail
							}
						}
						if (errors.emplName) {
							this.modal.name_status = {
								status: false,
								desc: errors.emplName
							}
						}
					} else {
						// 3. 显示当前页
						this.getEmpls(this.page.pageNum)
						// 4. 成功 - 关闭模态框
						$('#modal_panel_update').modal('hide')
						// 5. 清空模态框
						this.modal.modal_data = {
							emplName: '',
							emplEmail: '',
							emplGender: '',
							deptId: '',
						}
					}
				}
			})
		},
		getEmplById: function (id) {
			$.ajax({
				url: '${ APP_PATH }/empl/' + id,
				method: 'GET',
				success:  (result) => {
					this.modal.modal_data = result && result.data && result.data.empl
				}
			})
		}
    }
}

# com.qpf.crud.controller.EmployeeController

@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
    @ResponseBody
	@RequestMapping("/empl/{emplId}")
	public Msg getEmplbyId(@PathVariable("emplId") Integer emplId) {
		
		Employee employee = employeeService.getEmplyeeById(emplId);
		
		return Msg.success().add("empl", employee);
	}
	
	@ResponseBody
	@RequestMapping(value="/empl/{emplId}", method=RequestMethod.PUT)
	public Msg updateEmpl(Employee employee) {
		int update = employeeService.updateEmplyee(employee);
		System.out.println(update);
		return Msg.success();
	}
}

# com.qpf.crud.service.EmployeeService

@Service
public class EmployeeService {
	@Autowired
	EmployeeMapper employeeMapper;
	
	public Employee getEmplyeeById(int emplId) {
		return employeeMapper.selectByPrimaryKeyWithDept(emplId);
	}

	public int updateEmplyee(Employee employee) {
		employeeMapper.updateByPrimaryKeySelective(employee);
		return 0;
	}
}

```

3. `table_com`组件修改按钮添加触发打开模态框的事件

```
# Vue.js
{
    template: `
        ...
        <button class="btn btn-primary btn-sm" :mdata="item.emplId" @click="enter_update_panel"><span class="glyphicon glyphicon-pencil"></span>修改</button>
        ...
    `,
    methods: {
        enter_update_panel: function (event) {
    		var event = event || window.event
    		var mdata = event.currentTarget.attributes["mdata"].value
    		this.$emit('showupdatepanel', mdata)
    	}
    }
}

# list.jsp

<table_com :list="list" @showupdatepanel="showupdatepanel"></table_com>

```

4. `Vue`对象添加打开模态框方法

```
{
    methods: {
        showupdatepanel: function (data) {
    		this.getEmplById(data)
    		this.getDepts()
    		$('#modal_panel_update').modal('show')
    	}
    }
}
```

> 发送`PUT`请求有两种方法

1. 发送`POST`请求,添加`_method=PUT`参数

```
update: function (data) {
	data._method = "PUT"
	$.ajax({
		url: '${ APP_PATH }/empl/' + data.emplId,
		method: 'POST',
		data: data,
		success:  (result) => {}
	})
}
		
```

2. 直接发送`PUT`请求,`web.xml`添加`HttpPutFormContentFilter`过滤器


`Tomcat`只会在接受`POST`请求的时候封装请求体数据到`map`中,接受`PUT`请求`HttpPutFormContentFilter`过滤器重写的`getParameter()`方法从过滤封装的`map`中获取参数



```
update: function (data) {
$.ajax({
	url: '${ APP_PATH }/empl/' + data.emplId,
	method: 'PUT',
	data: data,
	success:  (result) => {}
})

# web.xml

<filter>
	<filter-name>httpPutFormContentFilter</filter-name>
	<filter-class>org.springframework.web.filter.HttpPutFormContentFilter</filter-class>
</filter>
<filter-mapping>
	<filter-name>httpPutFormContentFilter</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>
```

# 删除员工

1. `table_com`组件添加多选框,设置全选,绑定多选数据

```
Vue.component('table_com', {
	template: `
	    <div class="col-md-12">
			<table class="table table-hover">
				<thead>
					<tr>
						<th><input type="checkbox" value="" v-model="chengkAll"></th><th>#</th><th>Name</th><th>Gender</th><th>Email</th><th>Department</th><th>操作</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="item in list">
						<td><input type="checkbox" :value="item.emplId" v-model="chooselist"></td>
						<td>{{ item.emplId }}</td>
						<td>{{ item.emplName }}</td>
						<td>{{ item.emplGender === "M" ? "男" : "女" }}</td>
						<td>{{ item.emplEmail }}</td>
						<td>{{ item.department && item.department.deptName }}</td>
						<td>
							<button class="btn btn-primary btn-sm" :mdata="item.emplId" @click="enter_update_panel"><span class="glyphicon glyphicon-pencil"></span>修改</button>\
							<button class="btn btn-danger btn-sm" :mdata="item.emplId" @click="deleteEmpl"><span class="glyphicon glyphicon-trash"></span>删除</button>
						</td>
					</tr>
				</tbody>
			</table>
		</div>1,
	props: ['list'],
	data: function () {
		return  {
			chengkAll: false,
			chooselist: []
		}
	},
	methods: {
		enter_update_panel: function (event) {
			var event = event || window.event
			var mdata = event.currentTarget.attributes["mdata"].value
			this.$emit('showupdatepanel', mdata)
		},
		deleteEmpl: function (event) {
			var event = event || window.event
			var mdata = event.currentTarget.attributes["mdata"].value
			this.chengkAll = false
			this.chooselist = []
			this.chooselist.push(Number(mdata))
			this.$emit('deleteempl', this.chooselist)
		}
	},
	watch: {
		chooselist: function () {
			this.$emit('deletelist', this.chooselist)
		},
		chengkAll: function () {
			if (this.chengkAll) {
				this.chooselist = []
				this.list.forEach((e) => {
					this.chooselist.push(e.emplId)
				})
			} else {
				this.chooselist = []
			}
		},
		list: function () {
			this.chooselist = []
			this.chengkAll = false
		}
	}
})
```

2. 绑定多选触发事件, `Vue`对象添加相应方法

```
<table_com :list="list" @showupdatepanel="showupdatepanel" @deletelist="deletelist" @deleteempl="deleteEmpl"></table_com>

{
    methods: {
        deletelist: function (list) {
    		this.chooseList = list
    	},
    	deleteEmpl: function (list) {
    		if (!(list instanceof Array)) {
    			list = this.chooseList
    		}
    		if (list.length <= 0) return
    		if (!confirm('是否确定[ ' + list + ' ]? ')) return
    		$.ajax({
    			url: '${ APP_PATH }/empl/' + list.join('-'),
    			method: 'DELETE',
    			success:  (result) => {
    				if (result && result.code == "100") {
    					alert("删除成功 !!")
    					this.getEmpls(this.page.pageNum)
    				} else {
    					alert("删除失败 !!")
    				}
    			}
    		})
    	}
    }
}

```

3. 控制器添加输出员工方法

```
# com.qpf.crud.controller.EmployeeController

@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	private Logger logger = Logger.getLogger(getClass());
	
	@ResponseBody
	@RequestMapping(value="/empl/{emplId}", method=RequestMethod.DELETE)
	public Msg deleteEmpl(@PathVariable("emplId") String emplId) {
		System.out.println(emplId);
		String[] emplIds = emplId.split("-");
		List<String> ids = Arrays.asList(emplIds);
		List<Integer> id = new ArrayList<Integer>();
		for (String str: ids) {
			id.add(Integer.parseInt(str));
		}
		int delete = employeeService.deleteEmployees(id);
		logger.info(delete);
		return Msg.success();
	}

}

# com.qpf.crud.service.EmployeeService

@Service
public class EmployeeService {

	@Autowired
	EmployeeMapper employeeMapper;
    
    public int deleteEmployees(List<Integer> ids) {
		EmployeeExample example = new EmployeeExample();
		example.createCriteria().andEmplIdIn(ids);
		employeeMapper.deleteByExample(example );
		return 0;
	}
}

```