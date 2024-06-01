package com.shop.serve.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.EmployeeDTO;
import com.shop.pojo.dto.EmployeeLoginDTO;
import com.shop.pojo.entity.Employee;
import com.shop.pojo.vo.EmployeeVO;


public interface EmployeeService extends IService<Employee> {
    /**
     * 员工登录
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     */
    void save(EmployeeVO employeeVO);


    /**
     * 编辑员工信息
     */
    void update(EmployeeDTO employeeDTO);
}
