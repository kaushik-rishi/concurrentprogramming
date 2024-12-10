package com.lld.session_three;

// https://claude.ai/chat/674ccaf4-6b52-4e50-8e32-04cb30e60f57
class Semaphore {
    private boolean acquired = false;

    public synchronized void acquire() throws InterruptedException {
        while(acquired) wait();
        this.acquired = true;
    }

    public synchronized void release() throws InterruptedException {
        this.acquired = false;
        notifyAll();
    }

    public synchronized boolean tryAcquire(int timeoutInMs) throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutInMs;
        long remainingWaitTime = timeoutInMs;

        while (acquired) {
            if (remainingWaitTime <= 0) {
                return false;
            }
            wait(remainingWaitTime);
            remainingWaitTime = deadline - System.currentTimeMillis();
//            remainingWaitTime = remainingWaitTime - (System.currentTimeMillis() - startAcquisitionTime);
        }
        this.acquired = true;
        return true;
    }
}

class CountingSemaphore {
    private int availableLocks;

    public CountingSemaphore(int availableLocks) {
        this.availableLocks = availableLocks;
    }

    public synchronized void acquire() throws InterruptedException {
        while (availableLocks == 0) wait();
        --availableLocks;
    }

    public synchronized void release() {
        ++availableLocks;
        notifyAll();
    }

    public synchronized boolean tryAcquire(int timeoutInMs) throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutInMs;
        long remainingWait = timeoutInMs;

        while (!(availableLocks > 0)) {
            if (remainingWait <= 0) return false;
            wait(remainingWait);
            remainingWait = deadline - System.currentTimeMillis();
        }

        --availableLocks;
        return true;
    }
}

public class CustomSemaphoreImplementation {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore();

        Thread t1 = new Thread(() -> {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.printf("%s acquired lock\n", Thread.currentThread().getName());

            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}

            try {
                semaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.printf("%s released lock\n", Thread.currentThread().getName());
        }, "t1");

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}

            System.out.printf("%s tryna acquire\n", Thread.currentThread().getName());

            try {
                System.out.println(semaphore.tryAcquire(1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //            try {
//                semaphore.acquire();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//            System.out.printf("%s acquired lock\n", Thread.currentThread().getName());
//
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException ignored) {}
//
//            try {
//                semaphore.release();
//                System.out.printf("%s released lock\n", Thread.currentThread().getName());
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }, "t2");

        t1.start();
        t2.start();
    }
}
