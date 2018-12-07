package com.example.springbootcache.mapper;

import com.example.springbootcache.bean.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface DepartmentMpper {

    @Select("select * from department where id = #{id}")
    Department getDeptById(Integer id);
}
