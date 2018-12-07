package com.example.springbootcache.controller;

import com.example.springbootcache.bean.Employee;
import com.example.springbootcache.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    @GetMapping("/emp/{id}")
    public Employee getEmp(@PathVariable("id") Integer id){
        Employee emp = employeeService.getEmp(id);
        return emp;
    }

    @GetMapping("/updateEmp")
    public String updateEmp(Employee employee){
        int i = employeeService.updateEmp(employee);
        return i+"";
    }

    @GetMapping("emp/lastname/{lastName}")
    public Employee getEmpByLastName(@PathVariable("lastName") String lastName){
        Employee empByLastName = employeeService.getEmpByLastName(lastName);
        return empByLastName;
    }

}
