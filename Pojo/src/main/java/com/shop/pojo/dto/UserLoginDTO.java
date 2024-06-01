package com.shop.pojo.dto;

import lombok.Data;

@Data
public class UserLoginDTO {


    /**
     * 账号
     */
    private String account;


    /**
     * 密码
     */
    private String password;


    /**
     * 手机(验证码)
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;

}
