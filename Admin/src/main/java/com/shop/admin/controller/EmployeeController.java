package com.shop.admin.controller;

import com.shop.pojo.result.Result;
import com.shop.pojo.vo.EmployeeVO;
import com.shop.serve.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    @Operation(summary = "test")   //test swagger
    @Parameters()
    @PostMapping("/test")
    public void test() {
        log.info("test");
    }

    /**
     * 新增员工
     *
     * @param employeeDTO 员工DTO
     * @return Result
     */
    @PostMapping
    @Operation(summary = "新增员工")
    public Result<Object> save(@RequestBody EmployeeVO employeeVO) {
        log.info("新增员工：{}", employeeVO);
        employeeService.save(employeeVO);//该方法后续步骤会定义
        return Result.success();
    }
}
