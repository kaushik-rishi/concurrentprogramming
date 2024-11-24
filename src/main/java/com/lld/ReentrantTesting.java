package com.lld;

import java.util.concurrent.Semaphore;

/*
Re-entrant locking - Allows a thread to acquire the same lock multiple times
 */
public class ReentrantTesting {
    private final Object lock = new Object();
    private final Semaphore semaphoreLock = new Semaphore(1);

    // synchronized blocks are re-entrant by default
    private void recursiveFunctionSynchronizedKeyword(int n) {
        if (n == 0) return;

        synchronized (lock) {
            System.out.println(n);
            recursiveFunctionSynchronizedKeyword(n - 1);
        }
    }

    // semaphores are not re-entrant
    // this will cause a deadlock
    private void recursiveFunctionSemaphoreLocking(int n) throws InterruptedException {
        if (n == 0) return;

        semaphoreLock.acquire();

        System.out.println(n);
        recursiveFunctionSemaphoreLocking(n - 1);

        semaphoreLock.release();
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantTesting reentr = new ReentrantTesting();
        reentr.recursiveFunctionSynchronizedKeyword(10);
        reentr.recursiveFunctionSemaphoreLocking(10);
    }
}
