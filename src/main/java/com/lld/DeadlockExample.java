package com.lld;

// WONT CAUSE A DEADLOCK - MESSUP ON MY END
public class DeadlockExample {
    public static void main(String[] args) {
        // Create two resources

//      "this" -> resource
        SharedResource resource = new SharedResource();

        // Create first thread
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                resource.method1();
            }
        }, "Thread 1");

        // Create second thread
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                resource.method2();
            }
        }, "Thread 2");

        // Start both threads
        thread1.start();
        thread2.start();
        // m1
        // m2
        // m1
        // m2
        // m1
        // m1
        // m2
        // m1
        // m2
        // m2
    }
}

class SharedResource {
    public synchronized void method1() {
        System.out.println(Thread.currentThread().getName() + ": Acquired lock for method1");
        try {
            Thread.sleep(100); // Simulate some work
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // This will cause deadlock as we're trying to call method2 while holding method1's lock
        method2();

        System.out.println(Thread.currentThread().getName() + ": Finished method1");
    }

    public synchronized void method2() {
        System.out.println(Thread.currentThread().getName() + ": Acquired lock for method2");
        try {
            Thread.sleep(100); // Simulate some work
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // This will cause deadlock as we're trying to call method1 while holding method2's lock
        method1();

        System.out.println(Thread.currentThread().getName() + ": Finished method2");
    }
}
