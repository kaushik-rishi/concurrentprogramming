package com.lld.customthreadlocal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadLocalBasic<T> implements CustomThreadLocal<T> {
    private final Map<Thread, T> storage = new ConcurrentHashMap<>();

    @Override
    public T get() {
        return storage.get(Thread.currentThread());
    }

    @Override
    public void set(T value) {
        storage.put(Thread.currentThread(), value);
    }

    @Override
    public void remove() {
        storage.remove(Thread.currentThread());
    }
}
