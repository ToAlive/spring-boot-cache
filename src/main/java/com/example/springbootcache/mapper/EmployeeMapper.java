package com.example.springbootcache.mapper;

import com.example.springbootcache.bean.Employee;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository //加这个注解是因为EmployeeMpper在自动注入的时候会有错误警告，但是又不影响使用
public interface EmployeeMapper {

    @Select("select * from employee where id=#{id}")
    public Employee getEmployee(Integer id);


    @Update("update employee set lastName=#{lastName},email=#{email}," +
            "gender=#{gender},d_id=#{dId} where id=#{id}")
    public int updateEmp(Employee employee);

    @Delete("delete from employee where id=#{id}")
    public int delEmp(Integer id);


    @Insert("insert into employee(lastName,email,gender,d_id) values" +
            "(#{lastName},#{email},#{gender},#{dId})")
    public int saveEmp(Employee employee);

    @Select("select * from employee where lastName=#{lastName}")
    Employee getEmployeeByLastName(String lastName);
}
