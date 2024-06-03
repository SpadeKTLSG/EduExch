package com.shop.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.common.constant.JwtClaimsConstant;
import com.shop.common.constant.PasswordConstant;
import com.shop.common.properties.JwtProperties;
import com.shop.common.utils.JwtUtil;
import com.shop.pojo.Result;
import com.shop.pojo.dto.EmployeeDTO;
import com.shop.pojo.dto.EmployeeLoginDTO;
import com.shop.pojo.entity.Employee;
import com.shop.pojo.vo.EmployeeLoginVO;
import com.shop.pojo.vo.EmployeeVO;
import com.shop.serve.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.shop.common.utils.NewBeanUtils.getNullPropertyNames;
import static com.shop.common.utils.SystemConstants.MAX_PAGE_SIZE;


/**
 * 员工控制
 *
 * @author SK
 * @date 2024/05/31
 */
@Slf4j
@Tag(name = "Employee", description = "员工")
@RequestMapping("/admin/employee")
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;


    //! Func

    /**
     * 登录
     */
    @PostMapping("/login")
    @Operation(summary = "登录")
    @Parameters(@Parameter(name = "employeeLoginDTO", description = "员工登录DTO", required = true))
    public Result login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());

        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = new EmployeeLoginVO();
        BeanUtils.copyProperties(employee, employeeLoginVO);
        employeeLoginVO.setToken(token);

        return Result.success(employeeLoginVO);
    }
    //http://localhost:8085/admin/employee/login

    /**
     * 退出
     */
    @PostMapping("/logout")
    @Operation(summary = "退出")
    public Result logout() {
        //退出的实现:
        //1、前端自己删除token
        //2、后端不做处理
        return Result.success();
    }
    //http://localhost:8085/admin/employee/logout


    //! ADD

    /**
     * 新增员工
     */
    @PostMapping("/save")
    @Operation(summary = "新增员工")
    @Parameters(@Parameter(name = "employeeDTO", description = "员工DTO", required = true))
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        //判断这个account是否存在
        if (employeeService.query().eq("account", employeeDTO.getAccount()).count() > 0) {
            return Result.success("账号已存在");
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employeeService.save(employee);
        return Result.success();
    }
    //http://localhost:8085/admin/employee/save


    //! DELETE

    /**
     * 删除员工, 通过员工账号
     */
    @DeleteMapping("/delete/{account}")
    @Operation(summary = "删除员工")
    @Parameters(@Parameter(name = "account", description = "员工账号", required = true))
    public Result delete(@PathVariable("account") String account) {
        Employee employee = employeeService.getOne(Wrappers.<Employee>lambdaQuery().eq(Employee::getAccount, account));
        if (employee == null) {
            return Result.error("员工不存在");
        }
        employeeService.removeById(employee.getId());
        return Result.success();
    }
    //http://localhost:8085/admin/employee/delete


    //! UPDATE

    /**
     * 选择性更新员工信息
     */
    @PutMapping("/update")
    @Operation(summary = "选择性更新员工信息")
    @Parameters(@Parameter(name = "employee", description = "员工", required = true))
    public Result update(@RequestBody Employee employee) {
        //? 选择性更新字段示例

        Optional<Employee> optionalEmployee = Optional.ofNullable(employeeService.getOne(Wrappers.<Employee>lambdaQuery().eq(Employee::getAccount, employee.getAccount())));
        if (optionalEmployee.isEmpty()) {
            return Result.error("员工不存在");
        }

        Employee e2 = optionalEmployee.get();
        String[] nullPropertyNames = getNullPropertyNames(employee);
        BeanUtils.copyProperties(employee, e2, nullPropertyNames);

        Optional.ofNullable(employee.getPassword()) //手动调整密码生成
                .ifPresent(password -> e2.setPassword(DigestUtils.md5DigestAsHex(password.getBytes())));

        employeeService.updateById(e2);
        return Result.success();
    }
    //http://localhost:8085/admin/employee/update


    //! QUERY

    /**
     * Account查员工
     */
    @GetMapping("/{account}")
    @Operation(summary = "Account查员工")
    @Parameters(@Parameter(name = "account", description = "员工账号", required = true))
    public Result getByAccount(@PathVariable("account") String account) {
        Employee employee = employeeService.getOne(Wrappers.<Employee>lambdaQuery().eq(Employee::getAccount, account));
        if (employee == null) {
            return Result.error("员工不存在");
        }
        EmployeeVO employeeVO = new EmployeeVO();
        BeanUtils.copyProperties(employee, employeeVO);
        return Result.success(employeeVO);
    }
    //http://localhost:8085/admin/employee/Account查员工


    /**
     * 分页查全部员工
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    @Parameters(@Parameter(name = "current", description = "当前页", required = true))
    public Result pageQuery(@RequestParam(value = "current", defaultValue = "1") Integer current) {

        Page<EmployeeVO> employeeVOPage = (Page<EmployeeVO>) employeeService.page(new Page<>(current, MAX_PAGE_SIZE)).convert(employee -> {
            EmployeeVO employeeVO = new EmployeeVO();
            BeanUtils.copyProperties(employee, employeeVO);
            return employeeVO;
        });

        return Result.success(employeeVOPage);
    }
    //http://localhost:8085/admin/employee/page
}
