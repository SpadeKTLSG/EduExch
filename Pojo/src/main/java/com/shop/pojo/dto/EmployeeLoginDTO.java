package com.shop.pojo.dto;

import lombok.Data;

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
