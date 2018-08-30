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
CREATE TABLE empl (
  empl_id int(11) auto_increment not null,
  empl_name varchar(255) not null,
  empl_gender char(1) not null,
  empl_email varchar(255) not null,
  dept_id int(11) not null,
  PRIMARY KEY (empl_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE dept (
  dept_id int(11) auto_increment not null,
  dept_name varchar(255) not null,
  PRIMARY KEY (dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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

3. 新建`Employe`控制器`com.qpf.crud.controller.EmployeeCtroller`,并添加注解`@Controller`

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
		<!-- 使用下面的方式配置参数，后面会有所有的参数介绍 -->
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
	
	public static Msg msg(String code, String desc) {
		return new Msg(code, desc);
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
	
	return Msg.msg("100", "成功").add("pageInfo", pageInfo);
}
```