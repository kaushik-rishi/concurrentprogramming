package com.lld.session_three;

// Guarded suspension pattern
public class AdvancedBlockingBuffer<T> {
    private final T[] buffer;
    private int count = 0;
    private int takePtr = 0;
    private int putPtr = 0;
    private boolean isClosed = false;

    @SuppressWarnings("unchecked")
    public AdvancedBlockingBuffer(int capacity) {
        buffer = (T[]) new Object[capacity];
    }

    public synchronized void put(T item) throws InterruptedException {
        // Using while to handle multiple conditions
        while (count == buffer.length || isClosed) {
            if (isClosed) {
                throw new InterruptedException("Buffer is closed");
            }
            wait();
        }
        buffer[putPtr] = item;
        putPtr = (putPtr + 1) % buffer.length;
        count++;
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (count == 0 && !isClosed) {
            wait();
        }
        if (count == 0 && isClosed) {
            return null; // Signal end of stream
        }
        T item = buffer[takePtr];
        takePtr = (takePtr + 1) % buffer.length;
        count--;
        notifyAll();
        return item;
    }

    public synchronized void close() {
        isClosed = true;
        notifyAll();
    }
}
