package com.lld.session_three;

import java.util.ArrayDeque;
import java.util.Queue;

public class ProducerSingleConsumerBusyWait {
    private static volatile Queue<String> orders = new ArrayDeque<>();

    public static void main(String[] args) {
        int max_queue_capacity = 10;

        Thread producer = new Thread(() -> {
            for (int i = 0; i < 100; ++i) {
                System.out.printf("producing order %s\n", i);
                if (orders.size() >= max_queue_capacity) {
                    System.out.println("queue looks full, producer waiting for it to clear up");
                    while (orders.size() >= max_queue_capacity) {
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

        Thread consumer = new Thread(() -> {
            while (true) {
                while (orders.size() == 0) {
                    // busy-wait
                }

                // new item in the Q

                String orderName = orders.poll();
                System.out.printf("[%s] preparing\n", orderName);

                try {
                    Thread.sleep(10000); // simulate order preparation time
                } catch (InterruptedException ignored) {}

                System.out.printf("[%s] prepared\n", orderName);
            }
        }, "consumer");

        producer.start();
        consumer.start();
    }
}
