package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitmqConsumerApplication implements CommandLineRunner {
    private final MyConsumer consumer;

    @Autowired
    public RabbitmqConsumerApplication(MyConsumer consumer) {
        this.consumer = consumer;
    }

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqConsumerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        consumer.waitAndReport();
    }
}
