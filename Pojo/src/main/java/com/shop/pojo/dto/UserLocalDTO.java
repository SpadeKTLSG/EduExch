package com.shop.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 用来保存用户登录信息
 *
 * @author SK
 * @date 2024/06/01
 */
@Data
public class UserLocalDTO {


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 账号
     */
    private String account;

}
