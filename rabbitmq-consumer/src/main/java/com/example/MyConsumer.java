package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@RabbitListener(
        queues = "foobar"
)
@Slf4j
@Component
public class MyConsumer {
    private CountDownLatch latch = new CountDownLatch(1_000_000);
    private long start = System.currentTimeMillis();

    @RabbitHandler
    public void work(@org.springframework.messaging.handler.annotation.Payload MyMessage message) {
        latch.countDown();
    }

    public void waitAndReport() {
        while (true) {
            try {
                boolean await = latch.await(10, TimeUnit.SECONDS);
                if (await) {
                    long finished = System.currentTimeMillis();
                    log.info("Processed 1,000,000 jobs in {} milliseconds",
                            finished - start);
                    System.exit(0);
                } else {
                    log.info("Remains: {}",
                            latch.getCount());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
