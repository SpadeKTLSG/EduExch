package com.shop.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户功能
 *
 * @author SK
 * @date 2024/05/31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_func")
public class UserFunc {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 状态 (0正常, 1封禁)
     */
    private Integer status;

    /**
     * 收款码pic
     */
    private String billcard;

    /**
     * 积分数量
     */
    private Long credit;

    /**
     * 红牌警告数量
     */
    private Integer redhit;

    /**
     * 嘉奖数量
     */
    private Integer godhit;

    /**
     * 已售出数量
     */
    private Integer solds;

    /**
     * 已获得数量
     */
    private Integer gains;

    /**
     * 粉丝数量
     */
    private Integer fans;

    /**
     * 关注人数量
     */
    private Integer followee;

    /**
     * 收藏集合, 存放商品id, ','分割
     */
    private String collections;

    /**
     * 持有的券集合, 存卷VoucherId, ','分割
     */
    private String vouchers;
}
