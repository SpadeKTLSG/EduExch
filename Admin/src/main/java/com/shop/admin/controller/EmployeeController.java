package com.shop.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.common.constant.JwtClaimsConstant;
import com.shop.common.properties.JwtProperties;
import com.shop.common.utils.JwtUtil;
import com.shop.pojo.dto.EmployeeLoginDTO;
import com.shop.pojo.entity.Employee;
import com.shop.pojo.result.Result;
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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.shop.common.utils.SystemConstants.MAX_PAGE_SIZE;


/**
 * 员工
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

    @Operation(summary = "test")   //test swagger
    @Parameters()
    @PostMapping("/test")
    public void test() {
        log.info("test");
    }

    /**
     * 登录
     *
     * @param employeeLoginDTO 员工登录DTO
     */
    @PostMapping("/login")
    @Operation(summary = "登录")
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
    //url: http://localhost:8085/admin/employee/login


    /**
     * 新增员工
     */
    @PostMapping
    @Operation(summary = "新增员工")
    public Result save(@RequestBody EmployeeVO employeeVO) {
        employeeService.save(employeeVO);
        return Result.success();
    }

    /**
     * 分页查全部员工
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    @Parameters(@Parameter(name = "current", description = "当前页", required = true))
    public Result pageQuery(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        Page<Employee> resultPage = employeeService.page(new Page<>(current, MAX_PAGE_SIZE));
        return Result.success(resultPage);
    }


    /**
     * 退出
     */
    @PostMapping("/logout")
    @Operation(summary = "退出")
    public Result logout() {
        //TODO 实现退出功能
        return Result.success();
    }

    /**
     * id查员工
     */
    @GetMapping("/{id}")
    @Operation(summary = "id查员工")
    @Parameters(@Parameter(name = "id", description = "员工id", required = true))
    public Result getById(@PathVariable("id") Long id) {
        Employee employee = employeeService.getById(id);
        if (employee == null) {
            return Result.error("员工不存在");
        }
        return Result.success(employee);
    }


}
