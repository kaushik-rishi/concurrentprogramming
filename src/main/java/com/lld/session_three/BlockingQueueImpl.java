package com.lld.session_three;

import java.util.Queue;

public class BlockingQueueImpl {
    private final int capacity;
    private Queue<Integer> q;

    public BlockingQueueImpl(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSize() {
        return this.q.size();
    }

    void put(int x) throws InterruptedException {
        while (q.size() == capacity) {
            wait();
        }
        q.add(x);
        notifyAll();
    }

    int take() throws InterruptedException {
        while (q.isEmpty()) {
            wait();
        }
        return q.poll();
    }
}
