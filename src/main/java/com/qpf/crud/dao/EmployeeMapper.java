package com.qpf.crud.dao;

import com.qpf.crud.bean.Employee;
import com.qpf.crud.bean.EmployeeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EmployeeMapper {
    long countByExample(EmployeeExample example);

    int deleteByExample(EmployeeExample example);

    int deleteByPrimaryKey(Integer emplId);

    int insert(Employee record);

    int insertSelective(Employee record);

    List<Employee> selectByExample(EmployeeExample example);

    Employee selectByPrimaryKey(Integer emplId);
    
    /** 列表查询员工并封装部门信息 **/
    List<Employee> selectByExampleWithDept(EmployeeExample example);
    
    /** 查询员工并封装部门信息 **/
    Employee selectByPrimaryKeyWithDept(Integer emplId);

    int updateByExampleSelective(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByExample(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);
}