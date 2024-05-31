package com.shop.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 管理员员工
 *
 * @author SK
 * @date 2024/05/31
 */
@Slf4j
@Tag(name = "Employee", description = "员工")
@RequestMapping("/admin/employee")
@RestController
public class EmployeeController {


    @Operation(summary = "test")   //test swagger
    @Parameters()
    @PostMapping("/test")
    public void test() {
        log.info("test");
    }
}
