package com.lld.session_four;

import java.util.concurrent.Semaphore;

public class NonReentrantSemaphoreDemo {
    private final Semaphore semaphore = new Semaphore(1);

    public void outerMethod() {
        try {
            semaphore.acquire();  // First acquisition
            System.out.println("Outer method acquired lock");

            // Try to call inner method
            innerMethod();  // This will deadlock!

            System.out.println("This line will never be reached!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }

    private void innerMethod() throws InterruptedException {
        semaphore.acquire();  // Will block here forever
        try {
            System.out.println("Inner method acquired lock - This will never print!");
        } finally {
            semaphore.release();
        }
    }

    public static void main(String[] args) {
        NonReentrantSemaphoreDemo demo = new NonReentrantSemaphoreDemo();
        demo.outerMethod();
    }
}
