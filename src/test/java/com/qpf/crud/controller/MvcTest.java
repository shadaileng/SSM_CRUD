package com.qpf.crud.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.pagehelper.PageInfo;
import com.qpf.crud.bean.Employee;
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testPages() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/empls").param("pageNum", "3")).andReturn();
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
