package com.shop.serve.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.EmployeeLoginDTO;
import com.shop.pojo.entity.Employee;


public interface EmployeeService extends IService<Employee> {

    /**
     * 员工登录
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);


}
