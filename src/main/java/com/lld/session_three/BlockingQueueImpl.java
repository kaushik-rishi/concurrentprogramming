package com.lld.session_three;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

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

class IllegalStateException extends Exception {
    public IllegalStateException(String message) {
        super(message);
    }
}

class E2EBlockingQueueImpl {
    private final int capacity;
    private final Queue<Integer> queue = new LinkedList<>();

    public E2EBlockingQueueImpl(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public synchronized int getSize() {
        return queue.size();
    }

    public synchronized void add(int element) throws IllegalStateException {
        if (capacity <= queue.size()) throw new IllegalStateException("queue is full");
        queue.add(element);
    }

    public synchronized Integer offer(int element) {
        if (capacity > queue.size()) {
            queue.add(element);
            return element;
        }
        return null;
    }

    public synchronized void put(int element) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.add(element);
        notifyAll();
    }

    public synchronized boolean offer(int element, int time, TimeUnit unit) throws IllegalStateException, InterruptedException {
        long deadline = System.currentTimeMillis() + time;
        long remainingTime = deadline;

        if (unit != TimeUnit.MILLISECONDS)
            throw new IllegalStateException("time unit is only supposed to be MS");

        while (queue.size() == capacity) {
            if (remainingTime > 0)
                wait(remainingTime);
            else
                return false;
            remainingTime = deadline - System.currentTimeMillis();
        }

        queue.add(element);
        notifyAll();
        return true;
    }

    public synchronized int remove() throws IllegalStateException {
        if (queue.isEmpty())
            throw new IllegalStateException("queue is empty, cannot remove element");

        return queue.remove();
    }

    public synchronized Integer poll() {
        if (!queue.isEmpty()) {
            return queue.remove();
        }
        return null;
    }

    public synchronized int take() throws InterruptedException {
        while (queue.isEmpty()) wait();
        int element = queue.remove();
        notifyAll();
        return element;
    }

    public synchronized Integer poll(int time, TimeUnit unit) throws IllegalStateException, InterruptedException {
        long deadline = System.currentTimeMillis() + time;
        long remainingTime = deadline;

        while (queue.isEmpty()) {
            if (remainingTime > 0)
                wait(remainingTime);
            else
                return null;
            remainingTime = deadline - System.currentTimeMillis();
        }
        notifyAll();
        return queue.remove();
    }
}


















