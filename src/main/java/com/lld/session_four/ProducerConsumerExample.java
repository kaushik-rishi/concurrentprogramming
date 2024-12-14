package com.lld.session_four;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class ProducerConsumerExample {
    private static PriorityBlockingQueue<Task> taskQueue =
            new PriorityBlockingQueue<>(10,
                    Comparator.comparingInt(Task::getPriority).reversed());

    static class Task {
        private int priority;
        private String description;

        public Task(int priority, String description) {
            this.priority = priority;
            this.description = description;
        }

        public int getPriority() { return priority; }

        @Override
        public String toString() {
            return description + " (Priority: " + priority + ")";
        }
    }

    static class Producer implements Runnable {
        public void run() {
            try {
                while (true) {
                    int priority = (int)(Math.random() * 10);
                    Task task = new Task(priority, "Task-" + priority);
                    taskQueue.put(task);
                    System.out.println("Produced: " + task);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Consumer implements Runnable {
        public void run() {
            try {
                while (true) {
                    Task task = taskQueue.take();
                    System.out.println("Consumed: " + task);
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
