package com.shop.common.constant;

/**
 * RabbitMQ常量
 *
 * @author SK
 * @date 2024/06/08
 */
public class RabbitMQConstant {

    /**
     * 队列名称
     */
    public static final String QUEUE = "seckillQueue";

    /**
     * 交换机名称
     */
    public static final String EXCHANGE = "seckillExchange";

    /**
     * 路由键
     */
    public static final String ROUTINGKEY = "seckill.#";

    /**
     * 发送者路由键
     */
    public static final String SENDER_ROUTINGKEY = "seckill.message";
}
