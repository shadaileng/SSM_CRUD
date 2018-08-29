package com.qpf.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qpf.crud.bean.Employee;
import com.qpf.crud.dao.EmployeeMapper;

@Service
public class EmployeeService {

	@Autowired
	EmployeeMapper employeeMapper;
	
	public List<Employee> getAll() {
		List<Employee> employees = employeeMapper.selectByExampleWithDept(null);
		return employees;
	}

}
