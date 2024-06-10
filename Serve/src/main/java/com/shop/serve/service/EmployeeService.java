package com.shop.serve.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.EmployeeAllDTO;
import com.shop.pojo.dto.EmployeeDTO;
import com.shop.pojo.dto.EmployeeLoginDTO;
import com.shop.pojo.entity.Employee;
import com.shop.pojo.vo.EmployeeVO;
import jakarta.servlet.http.HttpSession;


public interface EmployeeService extends IService<Employee> {

    /**
     * 登录
     *
     * @return {@link String }  Token
     */
    String login(EmployeeLoginDTO employeeLoginDTO, HttpSession session);

    /**
     * 发送手机验证码
     * @return {@link String }  验证码
     */
    String sendCode(String phone, HttpSession session);

    /**
     * 注销当前用户
     *
     */
    void logout();

    /**
     * 新增一个员工
     */
    void saveOne(EmployeeDTO employeeDTO);

    /**
     * 更新一个员工
     */
    void updateOne(EmployeeAllDTO employeeAllDTO);

    /**
     * 删除一个员工
     */
    void deleteByAccount(String account);

    /**
     * 获取一个员工
     */
    EmployeeVO getByAccount(String account);
}
