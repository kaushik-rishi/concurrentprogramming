package com.lld;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CheckInterruptibleLock {
    // Helper method to get current time for logging
    private static String currentTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
    }

    // Helper method for consistent log formatting
    private static void log(String message) {
        System.out.printf("%s [%s] %s%n",
                currentTime(),
                Thread.currentThread().getName(),
                message);
    }

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            log("Starting execution");
            try {
                log("Attempting to acquire lock interruptibly");
                lock.lock();  // This is where interruption can happen
                try {
                    log("Successfully acquired lock");
                    log("Starting critical section work");
                    Thread.sleep(10000);  // Simulate long work
                    log("Completed critical section work");
                } finally {
                    log("Releasing lock");
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                log("Interrupted! Abandoning lock acquisition");
                // If you need to preserve interrupted status:
                // Thread.currentThread().interrupt();
            }
            log("Thread complete");
        }, "WorkerThread");

        Thread t2 = new Thread(() -> {
            log("Starting execution");
            log("Acquiring lock normally");
            lock.lock();  // Non-interruptible lock
            try {
                log("Successfully acquired lock");
                log("Waiting 2 seconds before interrupting worker thread");
                try {
                    Thread.sleep(2000);
                    log("Interrupting worker thread");
                    t1.interrupt();
                } catch (InterruptedException e) {
                    log("Unexpectedly interrupted while waiting");
                }
            } finally {
                log("Releasing lock");
                lock.unlock();
            }
            log("Thread complete");
        }, "InterruptingThread");

        // Start the threads in a specific order
        log("Starting interrupting thread first");
        t2.start();

        Thread.sleep(500); // Small delay to ensure order

        log("Starting worker thread");
        t1.start();

        // Wait for both threads to complete
        t2.join();
        t1.join();
        log("Demo complete");
    }
}
