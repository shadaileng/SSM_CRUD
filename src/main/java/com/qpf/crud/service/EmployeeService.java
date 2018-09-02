package com.qpf.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qpf.crud.bean.Employee;
import com.qpf.crud.bean.EmployeeExample;
import com.qpf.crud.bean.EmployeeExample.Criteria;
import com.qpf.crud.dao.EmployeeMapper;

@Service
public class EmployeeService {

	@Autowired
	EmployeeMapper employeeMapper;
	
	public List<Employee> getAll() {
		EmployeeExample example = new EmployeeExample();
		example.setOrderByClause("empl_id");
		List<Employee> employees = employeeMapper.selectByExampleWithDept(example );
		return employees;
	}

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

	public Employee getEmplyeeById(int emplId) {
		return employeeMapper.selectByPrimaryKeyWithDept(emplId);
	}

	public int updateEmplyee(Employee employee) {
		employeeMapper.updateByPrimaryKeySelective(employee);
		return 0;
	}

	public int deleteEmployees(List<Integer> ids) {
		EmployeeExample example = new EmployeeExample();
		example.createCriteria().andEmplIdIn(ids);
		employeeMapper.deleteByExample(example );
		return 0;
	}

}
