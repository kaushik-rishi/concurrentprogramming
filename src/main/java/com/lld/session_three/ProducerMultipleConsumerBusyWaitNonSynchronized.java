package com.lld.session_three;

import java.util.Queue;
import java.util.ArrayDeque;

public class ProducerMultipleConsumerBusyWaitNonSynchronized {
    private static volatile Queue<String> orders = new ArrayDeque<>();

    public static void main(String[] args) {
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 100; ++i) {
                System.out.printf("producing order %s\n", i);
                if (orders.size() >= 10) {
                    System.out.println("queue looks full, producer waiting for it to clear up");
                    while (orders.size() >= 10) {
                        // busy-wait
                    }
                    System.out.println("GOT SOME PLACE IN THE QUEUE, PLACING ORDER!!");
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}

                orders.add("order #%s".formatted(i));
            }
        }, "producer");

        // Create multiple consumers
        int numConsumers = 3;
        Thread[] consumers = new Thread[numConsumers];

        for (int i = 0; i < numConsumers; i++) {
            final int consumerId = i + 1;
            consumers[i] = new Thread(() -> {
                while (true) {
                    while (orders.size() == 0) {
                        // busy-wait
                    }

                    String orderName = orders.poll();

                    System.out.printf("[Consumer-%d] [%s] preparing\n", consumerId, orderName);

                    try {
                        Thread.sleep(10000); // simulate order preparation time
                    } catch (InterruptedException ignored) {}

                    System.out.printf("[Consumer-%d] [%s] prepared\n", consumerId, orderName);
                }
            }, "consumer-" + consumerId);
        }

        producer.start();
        for (Thread consumer : consumers) {
            consumer.start();
        }
    }
}
