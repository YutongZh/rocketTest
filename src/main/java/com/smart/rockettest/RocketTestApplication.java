package com.smart.rockettest;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class RocketTestApplication  implements ApplicationListener<ContextRefreshedEvent> {
    public static final Logger log = LoggerFactory.getLogger(RocketTestApplication.class);


    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private Consumer consumer;

    public static void main(String[] args) {
        System.setProperty("rocketmq.client.logLevel","DEBUG");
        SpringApplication.run(RocketTestApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        for (int i = 0; i < 1000; i++) {
//            rocketMQTemplate.convertAndSend("spring", ("hello rocketMQ 【" + i +"】").getBytes(StandardCharsets.UTF_8));
//        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("开始消费...");

        try {
            consumer.pullMsg();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
