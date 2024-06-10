package com.shop.serve.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.EmployeeAllDTO;
import com.shop.pojo.dto.EmployeeDTO;
import com.shop.pojo.dto.EmployeeLoginDTO;
import com.shop.pojo.entity.Employee;
import com.shop.pojo.vo.EmployeeVO;
import jakarta.servlet.http.HttpSession;


public interface EmployeeService extends IService<Employee> {


    //! Func


    /**
     * 发送手机验证码
     *
     * @return 验证码
     */
    String sendCodeA(String phone, HttpSession session);


    /**
     * 登录
     *
     * @return Token
     */
    String loginA(EmployeeLoginDTO employeeLoginDTO, HttpSession session);


    /**
     * 注销
     */
    void logoutA();


    //! ADD


    /**
     * 新增一个员工
     */
    void postEmployeeA(EmployeeDTO employeeDTO);


    //! DELETE


    /**
     * 删除一个员工
     */
    void deleteEmployeeA(String account);


    //! UPDATE


    /**
     * 更新一个员工
     */
    void putEmployeeA(EmployeeAllDTO employeeAllDTO);


    //! QUERY


    /**
     * 获取一个员工
     */
    EmployeeVO getEmployeeA(String account);


}
