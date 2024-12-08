package com.lld.session_three;

public class BlockingQueueImpl {
    private volatile Queue<Integer> q;

    void put(int x) {
        if (q.size() == capacity) {
            wait();
        }
        q.add(x);
        notifyAll();
    }

    void take() {
        if (q.size() == 0) {
            wait();
        }
        if (q.size() == capacity) {
            notifyAll();
        }
        return q.poll();
    }
}
