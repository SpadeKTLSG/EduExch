package com.shop.pojo.vo;

import lombok.Data;

/**
 * 员工登录VO
 *
 * @author SK
 * @date 2024/06/01
 */
@Data
public class EmployeeLoginVO {

    /**
     * 主键值
     */
    private Long id;


    /**
     * 账号
     */
    private String account;


    /**
     * 姓名
     */
    private String name;

    /**
     * jwt令牌
     */
    private String token;

}
