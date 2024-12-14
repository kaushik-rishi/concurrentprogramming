package com.lld.session_four;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
    private final ReentrantLock lock = new ReentrantLock();

    public void outerMethod() {
        lock.lock();  // First lock acquisition
        try {
            System.out.println("Outer method acquired lock");
            System.out.println("Hold count: " + lock.getHoldCount());

            // Same thread calls inner method which also requires lock
            innerMethod();

            System.out.println("Outer method finishing");
        } finally {
            lock.unlock();
        }
    }

    private void innerMethod() {
        lock.lock();  // Second lock acquisition by same thread
        try {
            System.out.println("Inner method acquired lock");
            System.out.println("Hold count: " + lock.getHoldCount());

            // Simulate some work
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        demo.outerMethod();
    }
}
