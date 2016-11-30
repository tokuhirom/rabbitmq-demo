package com.example;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyProducer {
    private final
    RabbitAdmin rabbitAdmin;
    private AmqpTemplate amqpTemplate;

    @Autowired
    public MyProducer(RabbitAdmin rabbitAdmin, AmqpTemplate amqpTemplate) {
        this.rabbitAdmin = rabbitAdmin;
        this.amqpTemplate = amqpTemplate;
    }

    public void run() {
        // キューの定義
        boolean durable = true; // disk に保存してくれ
        Queue queue = new Queue("foobar", durable);
        rabbitAdmin.declareQueue(queue);

        // insert してみる
        int numJobs = 1_000_000;
        long start = System.currentTimeMillis();
        String queueName = queue.getName();
        for (int i = 0; i < numJobs; ++i) {
            amqpTemplate.convertAndSend(queueName, new MyMessage("hoge"));
            if (i%100_000 == 0) {
                log.info("Inserted {} jobs", i);
            }
        }
        long finished = System.currentTimeMillis();
        log.info("Inserted {} jobs in {} milliseconds",
                numJobs,
                finished - start);
    }
}
