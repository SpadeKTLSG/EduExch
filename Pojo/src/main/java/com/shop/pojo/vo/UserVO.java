package com.shop.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户VO")
public class UserVO {

    /**
     * 账号
     */
    private String account;


    /**
     * 真实姓名
     */
    private String name;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机
     */
    private String phone;

}
