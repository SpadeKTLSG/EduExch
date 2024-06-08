package com.shop.guest.mq;


import com.shop.guest.config.RabbitMQTopicConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息发送者
 */
@Slf4j
@Service
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String ROUTINGKEY = "seckill.message";

    /**
     * 发送秒杀信息
     */
    public void sendSeckillMessage(String msg) {
        log.info("MQ发送消息" + msg);
        rabbitTemplate.convertAndSend(RabbitMQTopicConfig.EXCHANGE, ROUTINGKEY, msg);
    }
}
