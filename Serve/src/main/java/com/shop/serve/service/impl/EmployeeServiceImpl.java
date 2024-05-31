package com.shop.serve.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.constant.PasswordConstant;
import com.shop.pojo.dto.EmployeeDTO;
import com.shop.pojo.dto.EmployeeLoginDTO;
import com.shop.pojo.dto.EmployeePageQueryDTO;
import com.shop.pojo.entity.Employee;
import com.shop.pojo.result.PageResult;
import com.shop.pojo.vo.EmployeeVO;
import com.shop.serve.mapper.EmployeeMapper;
import com.shop.serve.service.EmployeeService;
import org.springframework.beans.BeanUtils;
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
        //采用基础实现
//        String username = employeeLoginDTO.();
//        String password = employeeLoginDTO.getPassword();


        return null;
    }


    /**
     * 新增员工
     */
    @Override
    public void save(EmployeeVO employeeVO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeVO, employee);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employeeMapper.insert(employee);
    }

    /**
     * 分页查询
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        return null;
    }


    /**
     * 编辑员工信息
     */
    public void update(EmployeeDTO employeeDTO) {

    }


}
