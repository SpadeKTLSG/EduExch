package com.shop.serve.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.EmployeeDTO;
import com.shop.pojo.dto.EmployeeLoginDTO;
import com.shop.pojo.entity.Employee;
import com.shop.pojo.vo.EmployeeLoginVO;
import com.shop.pojo.vo.EmployeeVO;


public interface EmployeeService extends IService<Employee> {

    EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);

    void saveOne(EmployeeDTO employeeDTO);

    void updateOne(Employee employee);

    void deleteByAccount(String account);

    EmployeeVO getByAccount(String account);
}
