package com.smart.rockettest;


import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketMQConfig {

//    @Bean
//    DefaultLitePullConsumer litePullConsumer() throws MQClientException {
//        DefaultLitePullConsumer litePullConsumer = new DefaultLitePullConsumer("spring-customer");
//        litePullConsumer.setNamesrvAddr("39.99.149.131:9876");
//        litePullConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
//        litePullConsumer.subscribe("spring", "*");
//        litePullConsumer.setAutoCommit(true); //该值默认为 true
//        litePullConsumer.start();
//        return litePullConsumer;
//    }
}
