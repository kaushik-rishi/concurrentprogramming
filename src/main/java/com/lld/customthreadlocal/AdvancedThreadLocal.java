package com.lld.customthreadlocal;

public interface AdvancedThreadLocal<T> {
    // Core operations
    void set(T value);
    T get();
    void remove();

    // Initialize with a supplier for lazy initialization
    interface Supplier<S> {
        S get();
    }

    // Factory method with initial value supplier
    static <T> AdvancedThreadLocal<T> withInitial(Supplier<? extends T> supplier) {
        return null;
    }

    // Protected methods you'll need to implement
    T initialValue();  // Called when get() finds no value

    // Optional: For handling inheritance
    T childValue(T parentValue);  // For InheritableThreadLocal behavior
}
