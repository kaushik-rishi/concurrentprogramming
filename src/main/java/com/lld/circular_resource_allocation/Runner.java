package com.lld.circular_resource_allocation;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class Resource {
    private final String name;
    private final ReentrantLock lock;
    private Thread owner;

    public Resource(String name) {
        this.name = name;
        this.lock = new ReentrantLock(true);
        this.owner = null;
    }

    private static void log(String message) {
//        System.out.println("[" + System.currentTimeMillis()/1000 + "] " + message);
    }

    public boolean acquire(long timeout) {
        try {
            boolean isLockAcquired = lock.tryLock(timeout, TimeUnit.MILLISECONDS);
            if (isLockAcquired) {
                owner = Thread.currentThread();
                log(owner.getName() + " acquired " + name);
            }
            return isLockAcquired;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public void release() {
        if (lock.isHeldByCurrentThread()) {
            owner = null;
            lock.unlock();
            log(Thread.currentThread().getName() + " release " + name);
        }
    }

    public String getName() {
        return name;
    }

    public Thread getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return name;
    }
}

class Worker implements Runnable {
    private final String name;
    private final Resource firstResource;
    private final Resource secondResource;
    private final int RESOURCE_ACQUISITION_TIMEOUT = 15000;

    public Worker(String name, Resource firstResource, Resource secondResource) {
        this.name = name;
        this.firstResource = firstResource;
        this.secondResource = secondResource;
    }

    private void log(String message) {
        System.out.println("[ " + name + " " + System.currentTimeMillis()/1000 + "] " + message);
    }

    private boolean acquireResources() {
        log("tryna acq " + firstResource);
        if (firstResource.acquire(RESOURCE_ACQUISITION_TIMEOUT)) {
            log("acquired " + firstResource);
            log("tryna acq " + secondResource);
            if (secondResource.acquire(RESOURCE_ACQUISITION_TIMEOUT)) {
                log("acquired " + secondResource);
                return true;
            } else {
                log("releasing " + firstResource);
                firstResource.release();
            }
        }
        return false;
    }

    private void releaseResources() {
        firstResource.release();
        log("released " + firstResource);
        secondResource.release();
        log("released " + secondResource);
    }

    @Override
    public void run() {
        boolean successfulExecutionYet = false;
        for (int i = 0; i < 3 && !successfulExecutionYet; ++i) {
            if (!acquireResources()) {
                log("unable to acquire resources this cycle");
                continue;
            }

            try {
                log("working with resources");
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                log("interruption caused due to sleep");
                Thread.currentThread().interrupt();
                continue;
            } finally {
                releaseResources();
            }

            successfulExecutionYet = true;
        }
    }
}

public class Runner {
    public static void main(String[] args) throws Exception {
        Resource r1 = new Resource("R1");
        Resource r2 = new Resource("R2");
        Resource r3 = new Resource("R3");

        Worker w1 = new Worker("W1", r1, r2);
        Worker w2 = new Worker("W2", r2, r3);
        Worker w3 = new Worker("W3", r1, r3);

        Thread t1 = new Thread(w1, "T-1");
        Thread t2 = new Thread(w2, "T-2");
        Thread t3 = new Thread(w3, "T-3");

        t1.start();
        t2.start();
        t3.start();
    }
}
