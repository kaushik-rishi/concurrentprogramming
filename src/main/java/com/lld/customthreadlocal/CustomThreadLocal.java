package com.lld.customthreadlocal;

public interface CustomThreadLocal<T> {
    void set(T value);
    T get();
    void remove();
}
