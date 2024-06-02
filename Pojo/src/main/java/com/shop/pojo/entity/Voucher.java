package com.shop.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 券核心
 *
 * @author SK
 * @date 2024/05/31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("voucher")
public class Voucher {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 名称
     */
    private String name;

    /**
     * 副名称
     */
    private String subname;

    /**
     * 数量
     */
    private Integer stock;

    /**
     * 状态(0 未使用, 1 已使用, 2 已过期)
     */
    private Integer status;

    /**
     * 类型(一般 0 / 秒杀 1)
     */
    private Integer type;

    /**
     * 可使用对象(卖方 0 / 买方 1)
     */
    private Integer user;

    /**
     * 功能字段(基础 0 / 高级 1 / 超级 2)
     */
    private Integer func;

    /**
     * 积分价值
     */
    private Integer value;

    /**
     * 生效时间
     */
    private LocalDateTime beginTime;

    /**
     * 失效时间
     */
    private LocalDateTime endTime;

    /**
     * 描述
     */
    private String description;


    /**
     * 对应用户id
     */
    private Long userId;
}
