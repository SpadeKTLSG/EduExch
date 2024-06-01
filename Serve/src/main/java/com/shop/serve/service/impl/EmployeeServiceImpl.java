package com.shop.serve.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.constant.MessageConstant;
import com.shop.common.exception.AccountNotFoundException;
import com.shop.common.exception.PasswordErrorException;
import com.shop.pojo.dto.EmployeeLoginDTO;
import com.shop.pojo.entity.Employee;
import com.shop.serve.mapper.EmployeeMapper;
import com.shop.serve.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO 员工登录DTO
     * @return 员工实体对象
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {

        Employee employee = employeeMapper.selectOne(new LambdaQueryWrapper<Employee>() //用户名查询数据库中的数据
                .eq(Employee::getAccount, employeeLoginDTO.getAccount()));

        if (employee == null) {//账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);

        }
        if (!DigestUtils.md5DigestAsHex(employeeLoginDTO.getPassword().getBytes()).equals(employee.getPassword())) {//密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        return employee;
    }


}
