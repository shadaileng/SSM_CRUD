package com.qpf.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qpf.crud.bean.Department;
import com.qpf.crud.dao.DepartmentMapper;

@Service
public class DepartmentService {
	
	@Autowired
	DepartmentMapper departmentMapper;

	public List<Department> getAll() {
		
		List<Department> depts = departmentMapper.selectByExample(null);
		
		return depts;
	}

	
	
	
}
