package com.shop.guest.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.shop.common.constant.RabbitMQConstant.*;


/**
 * RabbitMQ配置
 *
 * @author SK
 * @date 2024/06/08
 */
@Configuration
@EnableRabbit
public class RabbitMQTopicConfig {


    /**
     * 队列
     */
    @Bean
    public Queue myQueue() {
        return new Queue(QUEUE);
    }

    /**
     * 交换机
     */
    @Bean
    public FanoutExchange easyExchange() {
        return new FanoutExchange(EXCHANGE);
    }

    /**
     * 绑定
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(myQueue()).to(easyExchange());
    }

}
