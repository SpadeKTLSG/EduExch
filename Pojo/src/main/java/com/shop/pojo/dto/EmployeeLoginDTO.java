package com.shop.pojo.dto;

import lombok.Data;

/**
 * 员工登录DTO
 *
 * @author SK
 * @date 2024/06/01
 */
@Data
public class EmployeeLoginDTO {

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;
}
