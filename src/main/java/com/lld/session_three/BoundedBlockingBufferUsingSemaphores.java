package com.lld.session_three;

import java.util.concurrent.Semaphore;

public class BoundedBlockingBufferUsingSemaphores {
    private final int capacity;
    private final Semaphore emptySlots;
    private final Semaphore fullSlots = new Semaphore(0);
    private final Semaphore mutex = new Semaphore(1);
    private final int[] buffer;
    private int insertIndex;
    private int extractIndex;

    public BoundedBlockingBufferUsingSemaphores(int capacity) {
        this.capacity = capacity;
        buffer = new int[capacity];
        emptySlots = new Semaphore(capacity);
        insertIndex = 0;
        extractIndex = 0;
    }

    void put(int value) throws InterruptedException {
        emptySlots.acquire();
        mutex.acquire();

        try {
            buffer[insertIndex] = value;
            insertIndex = (insertIndex + 1) % capacity;
        } finally {
            mutex.release();
        }

        fullSlots.release();
    }

    int take() throws InterruptedException {
        fullSlots.acquire();
        mutex.acquire();

        int item;

        try {
            item = buffer[extractIndex];
            extractIndex = (extractIndex + 1) % capacity;
        } finally {
            mutex.release();
        }

        emptySlots.release();
        return item;
    }
}
