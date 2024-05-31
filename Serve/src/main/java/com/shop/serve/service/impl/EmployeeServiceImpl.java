package com.shop.serve.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.dto.EmployeeDTO;
import com.shop.pojo.dto.EmployeeLoginDTO;
import com.shop.pojo.dto.EmployeePageQueryDTO;
import com.shop.pojo.entity.Employee;
import com.shop.pojo.result.PageResult;
import com.shop.pojo.vo.EmployeeVO;
import com.shop.serve.mapper.EmployeeMapper;
import com.shop.serve.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return null;
    }


    /**
     * 新增员工
     */
    @Override
    public void save(EmployeeVO employeeVO) {
        Employee employee = new Employee();
        employee.builder()
                .username(employeeVO.getUsername())
                .password(employeeVO.getPassword())
                .name(employeeVO.getName())
                .build();
        employeeMapper.insert();
    }

    /**
     * 分页查询
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        return null;
    }


    /**
     * 根据id查询员工
     */
    public Employee getById(Long id) {
        return null;
    }

    /**
     * 编辑员工信息
     */
    public void update(EmployeeDTO employeeDTO) {

    }


}
