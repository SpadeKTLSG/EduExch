package com.shop.guest.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
public class RabbitMQTopicConfig {


    /**
     * 队列  seckillQueue
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    /**
     * 交换机  seckillExchange
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    /**
     * 绑定  seckillQueue  seckillExchange  seckill.#
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(topicExchange()).with(ROUTINGKEY);
    }

}
