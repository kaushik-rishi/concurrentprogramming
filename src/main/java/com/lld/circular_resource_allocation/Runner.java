package com.lld.circular_resource_allocation;

enum Resources {
    R1(new Object()),
    R2(new Object()),
    R3(new Object());

    private final Object lock;

    Resources(Object o) {
        this.lock = o;
    }

    public Object getLock() {
        return this.lock;
    }
}

class ResourcePair {
    private final Object firstResource;
    private final Object secondResource;

    public ResourcePair(Object firstResource, Object secondResource) {
        this.firstResource = firstResource;
        this.secondResource = secondResource;
    }

    public Object getFirstResource() {
        return firstResource;
    }

    public Object getSecondResource() {
        return secondResource;
    }
}

class Worker implements Runnable {
    private final int id;
    private final int JOB_DURATION = 4000; // 4000ms

    public Worker(int id) throws Exception {
        if (!(id >= 1 && id <= 3)) {
            throw new Exception("unable to instantiate worker, id must be beteween 1 and 3");
        }
        this.id = id;
    }

    public void run() {
        // 1. acquire locks
        // 2. perform tasks

        final ResourcePair rp;

        // decide which locks/resources to acquire
        if (id == 1) {
            rp = new ResourcePair(Resources.R1.getLock(), Resources.R2.getLock());
        } else if (id == 2) {
            rp = new ResourcePair(Resources.R2.getLock(), Resources.R3.getLock());
        } else if (id == 3) {
            rp = new ResourcePair(Resources.R3.getLock(), Resources.R1.getLock());
        }
    }
}


public class Runner {
}
