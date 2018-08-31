package com.qpf.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qpf.crud.bean.Department;
import com.qpf.crud.bean.Msg;
import com.qpf.crud.service.DepartmentService;

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
