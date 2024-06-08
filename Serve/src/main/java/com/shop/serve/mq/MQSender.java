package com.shop.serve.mq;



import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.shop.common.constant.RabbitMQConstant.EXCHANGE;
import static com.shop.common.constant.RabbitMQConstant.SENDER_ROUTINGKEY;

/**
 * 消息发送者
 */
@Slf4j
@Service
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送秒杀信息
     */
    public void sendSeckillMessage(String msg) {
        log.info("MQ发送消息" + msg);
        rabbitTemplate.convertAndSend(EXCHANGE, SENDER_ROUTINGKEY, msg);
    }

}
