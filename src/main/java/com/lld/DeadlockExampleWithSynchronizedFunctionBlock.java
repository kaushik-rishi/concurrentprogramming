package com.lld;

public class DeadlockExampleWithSynchronizedFunctionBlock {
    static class Resource {
        private String name;

        public Resource(String name) {
            this.name = name;
        }

        public synchronized void accessResource(Resource other) {
            System.out.println(Thread.currentThread().getName() +
                    " acquired " + this.name);

            try {
                Thread.sleep(100); // Simulate some work
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Try to access the other resource
            other.accessOtherResource(this);
        }

        public synchronized void accessOtherResource(Resource other) {
            System.out.println(Thread.currentThread().getName() +
                    " acquired " + this.name + " after " + other.name);
        }
    }

    public static void main(String[] args) {
        final Resource resourceA = new Resource("Resource A");
        final Resource resourceB = new Resource("Resource B");

        // Thread 1 tries to acquire resources in order: A -> B
        Thread thread1 = new Thread(() -> {
            resourceA.accessResource(resourceB);
        }, "Thread-1");

        // Thread 2 tries to acquire resources in order: B -> A
        Thread thread2 = new Thread(() -> {
            resourceB.accessResource(resourceA);
        }, "Thread-2");

        thread1.start();
        thread2.start();
    }
}
