package com.shop.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 用户
 *
 * @author SK
 * @date 2024/05/31
 */
@Data
public class EmployeeDTO {

    /**
     * 主键 Employee唯一
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账号
     */
    private String account;


    /**
     * 姓名
     */
    private String name;


}
