package com.shop.serve.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.constant.PasswordConstant;
import com.shop.common.exception.AccountAlivedException;
import com.shop.common.exception.AccountNotFoundException;
import com.shop.common.exception.InvalidInputException;
import com.shop.common.utils.RegexUtils;
import com.shop.pojo.dto.EmployeeDTO;
import com.shop.pojo.dto.EmployeeLocalDTO;
import com.shop.pojo.dto.EmployeeLoginDTO;
import com.shop.pojo.entity.Employee;
import com.shop.pojo.vo.EmployeeVO;
import com.shop.serve.mapper.EmployeeMapper;
import com.shop.serve.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.shop.common.constant.MessageConstants.*;
import static com.shop.common.constant.RedisConstants.*;
import static com.shop.common.utils.NewBeanUtils.getNullPropertyNames;

@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public String login(EmployeeLoginDTO employeeLoginDTO, HttpSession session) {

        //删除掉之前的所有登陆令牌
        Set<String> keys = stringRedisTemplate.keys(LOGIN_USER_KEY_ADMIN + "*");
        if (keys != null) {
            stringRedisTemplate.delete(keys);
        }

        String phone = employeeLoginDTO.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) throw new InvalidInputException(PHONE_INVALID);

        //从redis获取验证码并校验
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY_ADMIN + phone);
        String code = employeeLoginDTO.getCode();
        if (cacheCode == null || !cacheCode.equals(code)) throw new InvalidInputException(CODE_INVALID);

        //根据用户名查询用户
        Employee employee = query().eq("account", employeeLoginDTO.getAccount()).one();
        if (employee == null) throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);


        // 随机生成token，作为登录令牌
        String token = UUID.randomUUID().toString(true);
        EmployeeLocalDTO employeeLocalDTO = BeanUtil.copyProperties(employee, EmployeeLocalDTO.class);
        Map<String, Object> employeeMap = BeanUtil.beanToMap(employeeLocalDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));

        // 存储
        String tokenKey = LOGIN_USER_KEY_ADMIN + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, employeeMap);
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL_ADMIN, TimeUnit.MINUTES);

        return token;
    }

    @Override
    public String sendCode(String phone, HttpSession session) {

        if (RegexUtils.isPhoneInvalid(phone)) throw new InvalidInputException(PHONE_INVALID);

        Set<String> keys = stringRedisTemplate.keys(LOGIN_USER_KEY_ADMIN + phone + "*"); //删除之前的验证码
        if (keys != null) {
            stringRedisTemplate.delete(keys);
        }

        String code = RandomUtil.randomNumbers(6); //生成

        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY_ADMIN + phone, code, LOGIN_CODE_TTL_ADMIN, TimeUnit.MINUTES);

        return code; //调试环境: 返回验证码; 未来引入邮箱发送验证码
    }

    @Override
    public void logout() {
        //删除掉之前的所有登陆令牌
        Set<String> keys = stringRedisTemplate.keys(LOGIN_USER_KEY_ADMIN + "*");
        if (keys != null) {
            stringRedisTemplate.delete(keys);
        }
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


    @Override
    public void deleteByAccount(String account) {
        Employee employee = this.getOne(Wrappers.<Employee>lambdaQuery().eq(Employee::getAccount, account));
        if (employee == null) throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);
        this.removeById(employee.getId());
    }

    @Override
    public EmployeeVO getByAccount(String account) {
        Employee employee = this.getOne(Wrappers.<Employee>lambdaQuery().eq(Employee::getAccount, account));
        if (employee == null) throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);

        EmployeeVO employeeVO = new EmployeeVO();
        BeanUtils.copyProperties(employee, employeeVO);
        return employeeVO;
    }


}
