package com.shop.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录
 *
 * @author SK
 * @date 2024/06/01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
