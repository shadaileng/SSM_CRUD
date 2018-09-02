package com.qpf.crud.dao;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qpf.crud.bean.Department;
import com.qpf.crud.bean.DepartmentExample;
import com.qpf.crud.bean.DepartmentExample.Criteria;
import com.qpf.crud.bean.Employee;

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
		departmentMapper.insertSelective(new Department(null, "开发部"));
		departmentMapper.insertSelective(new Department(null, "测试部"));
//		departmentMapper.updateByPrimaryKey(new Department(1, "开发部"));
//		departmentMapper.updateByPrimaryKey(new Department(2, "测试部"));
//		Department department = departmentMapper.selectByPrimaryKey(3);
//		System.out.println(department);
//		DepartmentExample example = new DepartmentExample();
//		Criteria criteria = example.createCriteria();
//		criteria.andDeptIdGreaterThan(2);
//		departmentMapper.deleteByExample(example);
		
		List<Department> list = departmentMapper.selectByExample(null);
		for (Department department : list) {
			System.out.println(department);
		}
		System.out.println("finish!");
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
		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
		
		// 3. 批量插入
		for (int i = 0; i < 100; i++) {
			String name = UUID.randomUUID().toString().substring(0, 5) + i;
			mapper.insertSelective(new Employee(null, name, "M", name + "@qq.com", 2));
		}
		
		List<Employee> employees = null;
		// 列表查询,未封装Department
//		employees = employeeMapper.selectByExample(null);
		// 列表查询，封装Department
		employees = employeeMapper.selectByExampleWithDept(null);
		System.out.println(employees);
		System.out.println("finish!");
	}
}
