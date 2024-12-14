package com.lld.session_three;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerBlockingQueue {
    private static final BlockingQueue<String> orders = new ArrayBlockingQueue<>(10);

    public static void main(String[] args) {
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 100; ++i) {
                try {
                    System.out.printf("producing order %s\n", i);
                    orders.put("order #%s".formatted(i));  // Will automatically block if queue is full
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }, "producer");

        Object o = new Object();

        // Create multiple consumers
        int numConsumers = 3;
        Thread[] consumers = new Thread[numConsumers];

        for (int i = 0; i < numConsumers; i++) {
            final int consumerId = i + 1;
            consumers[i] = new Thread(() -> {
                while (true) {
                    try {
                        String orderName = orders.take();  // Will automatically block if queue is empty
                        System.out.printf("[Consumer-%d] [%s] preparing\n", consumerId, orderName);
                        Thread.sleep(10000); // simulate order preparation time
                        System.out.printf("[Consumer-%d] [%s] prepared\n", consumerId, orderName);
                    } catch (InterruptedException ignored) {}
                }
            }, "consumer-" + consumerId);
        }

        producer.start();
        for (Thread consumer : consumers) {
            consumer.start();
        }
    }
}
