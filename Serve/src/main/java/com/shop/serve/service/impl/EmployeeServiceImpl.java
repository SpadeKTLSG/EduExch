package com.shop.serve.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.constant.JwtClaimsConstant;
import com.shop.common.constant.MessageConstants;
import com.shop.common.constant.PasswordConstant;
import com.shop.common.exception.AccountAlivedException;
import com.shop.common.exception.AccountNotFoundException;
import com.shop.common.exception.PasswordErrorException;
import com.shop.common.properties.JwtProperties;
import com.shop.common.utils.JwtUtil;
import com.shop.pojo.dto.EmployeeDTO;
import com.shop.pojo.dto.EmployeeLoginDTO;
import com.shop.pojo.entity.Employee;
import com.shop.pojo.vo.EmployeeLoginVO;
import com.shop.serve.mapper.EmployeeMapper;
import com.shop.serve.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.shop.common.constant.MessageConstants.ACCOUNT_ALIVED;
import static com.shop.common.constant.MessageConstants.ACCOUNT_NOT_FOUND;
import static com.shop.common.utils.NewBeanUtils.getNullPropertyNames;

@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO) {

        Employee employee = employeeMapper.selectOne(new LambdaQueryWrapper<Employee>() //用户名查询数据库中的数据
                .eq(Employee::getAccount, employeeLoginDTO.getAccount()));

        if (employee == null) {//账号不存在
            throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);

        }
        if (!DigestUtils.md5DigestAsHex(employeeLoginDTO.getPassword().getBytes()).equals(employee.getPassword())) {//密码错误
            throw new PasswordErrorException(MessageConstants.PASSWORD_ERROR);
        }

        //管理端使用JWT令牌简单实现登录
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());

        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = new EmployeeLoginVO();
        BeanUtils.copyProperties(employee, employeeLoginVO);
        employeeLoginVO.setToken(token);
        return employeeLoginVO;
    }


    @Override
    public void saveOne(EmployeeDTO employeeDTO) {

        if (this.query().eq("account", employeeDTO.getAccount()).count() > 0) {
            throw new AccountAlivedException(ACCOUNT_ALIVED);
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        this.save(employee);
    }


    @Override
    public void updateOne(Employee employee) {
        //? 选择性更新字段示例

        Optional<Employee> optionalEmployee = Optional.ofNullable(this.getOne(Wrappers.<Employee>lambdaQuery().eq(Employee::getAccount, employee.getAccount())));
        if (optionalEmployee.isEmpty()) {
            throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);
        }

        Employee e2 = optionalEmployee.get();
        String[] nullPropertyNames = getNullPropertyNames(employee);
        BeanUtils.copyProperties(employee, e2, nullPropertyNames);

        Optional.ofNullable(employee.getPassword()) //手动调整密码生成
                .ifPresent(password -> e2.setPassword(DigestUtils.md5DigestAsHex(password.getBytes())));

        this.updateById(e2);
    }


}
