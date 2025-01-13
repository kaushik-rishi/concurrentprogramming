package com.lld.customthreadlocal;

public interface CustomThreadLocal<T> {
    /*
    Thread.currentThread().getId();
     */
    void set(T value);
    T get();
    void remove();
}
