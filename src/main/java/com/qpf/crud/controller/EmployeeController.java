package com.qpf.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qpf.crud.bean.Employee;
import com.qpf.crud.bean.Msg;
import com.qpf.crud.service.EmployeeService;

@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;

	@RequestMapping("/empls")
	public String listEmpl(@RequestParam(value="pageNum", defaultValue="1") Integer pageNum, Model model) {
		
		// 在查询之前那设置分页,pageNum: 当前页码, pageSize: 每页显示的大小
		PageHelper.startPage(pageNum, 5);
		
		List<Employee> employess = employeeService.getAll();
		
		// 使用PageInfo封装查询结果,封装后的对象包含分页信息
		PageInfo<Employee> pageInfo = new PageInfo<Employee>(employess, 5);
		model.addAttribute("pageInfo", pageInfo);
		
		return "list";
	}
	
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
	
}
