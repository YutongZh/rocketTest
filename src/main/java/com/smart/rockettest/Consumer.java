package com.smart.rockettest;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
//@RocketMQMessageListener(consumerGroup = "spring-consumer", topic = "spring")
public class Consumer {

    public static final Logger log = LoggerFactory.getLogger(Consumer.class);

//    @Override
//    public void onMessage(String s) {
//        log.info("收到消息:{}", s);
//    }

    private  DefaultLitePullConsumer litePullConsumer;


    @PostConstruct
    public void start() throws MQClientException {
        DefaultLitePullConsumer litePullConsumer = new
                DefaultLitePullConsumer("litepullcustomer");
        litePullConsumer.setNamesrvAddr("39.99.149.131:9876");
        litePullConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        litePullConsumer.subscribe("spring", "*");
        litePullConsumer.setAutoCommit(true); //该值默认为 true
        litePullConsumer.setPollTimeoutMillis(100);
        litePullConsumer.setPullBatchSize(40);
        litePullConsumer.start();
        this.litePullConsumer = litePullConsumer;
    }

//    @Resource
//    private DefaultLitePullConsumer pullConsumer;

    public void  pullMsg() throws InterruptedException {
//        while(true) {
//            List<MessageExt> messageExtList = pullConsumer.poll();
//            for (MessageExt messageExt : messageExtList) {
//                // 真正的业务处理
//                System.out.println(new String(messageExt.getBody()));
//            }
//        }

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        CountDownLatch countDownLatch = new CountDownLatch(5);
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(20);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        threadPool.scheduleAtFixedRate(() -> {
            for (int i = 0; i < 5; i++) {
                int finalI = i;
                executorService.submit(()->{
                    try {
                        //log.info("{} 阻塞中...", finalI);
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    List<MessageExt> messageExtList = litePullConsumer.poll();
                    //log.info("线程 {} 拉取到消息数：{}" ,Thread.currentThread().getName() , messageExtList.size());
                    for (MessageExt messageExt : messageExtList) {
                        // 真正的业务处理
                        log.info("第{}条消息， 消息内容：{}", atomicInteger.incrementAndGet(), new String(messageExt.getBody()));
                    }
                });
                countDownLatch.countDown();
        }}, 2L, // 3s 后开始执行定时任务
        1L, // 定时任务的执行间隔为 1s
        TimeUnit.SECONDS); // 描述上面两个参数的时间单位
    }
}
