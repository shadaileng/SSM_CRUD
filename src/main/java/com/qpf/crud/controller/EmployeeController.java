package com.qpf.crud.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	private Logger logger = Logger.getLogger(getClass());

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
	@ResponseBody
	@RequestMapping("/empl/{emplId}")
	public Msg getEmplbyId(@PathVariable("emplId") Integer emplId) {
		
		Employee employee = employeeService.getEmplyeeById(emplId);
		
		return Msg.success().add("empl", employee);
	}

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

	@ResponseBody
	@RequestMapping(value="/validateName", method=RequestMethod.POST)
	public Msg validateName(String name) {
		boolean bool = employeeService.validateName(name);
		if (bool) return Msg.success().desc("用户名可用");
		return Msg.fail().desc("用户名不可用");
	}

	@ResponseBody
	@RequestMapping(value="/empl/{emplId}", method=RequestMethod.PUT)
	public Msg updateEmpl(Employee employee) {
		int update = employeeService.updateEmplyee(employee);
		logger.info(update);
		return Msg.success();
	}
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
