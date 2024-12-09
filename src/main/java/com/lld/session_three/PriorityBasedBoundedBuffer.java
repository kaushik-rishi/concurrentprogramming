package com.lld.session_three;


import java.util.concurrent.Semaphore;

public class PriorityBasedBoundedBuffer {
    private final int capacity;
    private final Semaphore emptySlots;
    private final Semaphore fullSlots = new Semaphore(0);
    private final Semaphore highPriorityMutex = new Semaphore(1);
    private final Semaphore lowPriorityMutex = new Semaphore(1);
    private final int[] buffer;
    private int insertIndex;
    private int extractIndex;

    public PriorityBasedBoundedBuffer(int capacity) {
        this.capacity = capacity;
        buffer = new int[capacity];
        emptySlots = new Semaphore(capacity);
    }

    void putHighPriority(int value) throws InterruptedException {
        emptySlots.acquire();
        highPriorityMutex.acquire();
        try {
            while (!lowPriorityMutex.tryAcquire()) {
                // Preempt low priority operation
                Thread.yield();
            }
            buffer[insertIndex] = value;
            insertIndex = (insertIndex + 1) % capacity;
        } finally {
            lowPriorityMutex.release();
            highPriorityMutex.release();
        }
        fullSlots.release();
    }

    void putLowPriority(int value) throws InterruptedException {
        // Low priority must check if high priority is waiting
        if (highPriorityMutex.hasQueuedThreads()) {
            return; // Back off if high priority waiting
        }
        emptySlots.acquire();
        lowPriorityMutex.acquire();
        try {
            buffer[insertIndex] = value;
            insertIndex = (insertIndex + 1) % capacity;
        } finally {
            lowPriorityMutex.release();
        }
        fullSlots.release();
    }
}
