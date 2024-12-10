package com.lld.session_three;

public interface IBinarySemaphore {
    /**
     * Releases the binary semaphore, allowing another thread to acquire it.
     *
     * Semantics:
     * - Releases exclusive access to the shared resource
     * - Wakes up a waiting thread if any
     * - Only meaningful if the semaphore was previously acquired
     */
    void release();

    /**
     * Acquires the binary semaphore, blocking if already held.
     *
     * Behavior:
     * - Blocks if semaphore is currently held by another thread
     * - Grants exclusive access to the shared resource
     *
     * @throws InterruptedException if thread is interrupted while waiting
     */
    void acquire() throws InterruptedException;

    /**
     * Attempts to acquire the semaphore with a timeout.
     *
     * Requirements:
     * - Tries to acquire the semaphore immediately
     * - Waits up to specified timeout if unavailable
     * - Returns true if semaphore acquired within timeout
     * - Returns false if timeout expires before acquiring
     *
     * @param timeoutInMs maximum time to wait for semaphore (in milliseconds)
     * @return true if semaphore acquired, false otherwise
     * @throws InterruptedException if thread is interrupted during waiting
     */
    boolean tryAcquire(long timeoutInMs) throws InterruptedException;
}
