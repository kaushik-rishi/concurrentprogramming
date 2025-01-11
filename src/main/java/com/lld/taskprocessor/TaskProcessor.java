package com.lld.taskprocessor;

/*
blocking queue - submitted tasks are added here // unbounded
pool of worker threads - pick tasks from the queue and start executing them
shutdown - do not pick new tasks, complete the ones already in process
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

class Worker implements Runnable {
    private final BlockingQueue<Runnable> taskQueue;
    private final AtomicBoolean isShutdownInitiated;

    public Worker(BlockingQueue<Runnable> taskQueue, AtomicBoolean isShutdownInitiated) {
        this.taskQueue = taskQueue;
        this.isShutdownInitiated = isShutdownInitiated;
    }

    @Override
    public void run() {
        System.out.printf("starting %s", Thread.currentThread().getName());
        while (!isShutdownInitiated.get()) {
            try {
                Runnable task = taskQueue.take();
                System.out.println("executing task");
                task.run();
            } catch (InterruptedException ex) {
                System.out.printf("Exception while pulling a task from the queue - %s\n", ex.getMessage());
            }
        }
        System.out.printf("gracefully terminating %s\n", Thread.currentThread().getName());
    }
}

public class TaskProcessor {
    private final BlockingQueue<Runnable> mainTaskQueue;
    private final AtomicBoolean isShutdownInitiated;
    private final int numberOfWorkers;
    private final List<Thread> workerThreads;

    public TaskProcessor(int numberOfWorkers) {
        this.numberOfWorkers = numberOfWorkers;
        isShutdownInitiated = new AtomicBoolean(false);
        mainTaskQueue = new LinkedBlockingQueue<>();
        workerThreads = new ArrayList<>();
        initializeWorkerPool();
    }

    private void initializeWorkerPool() {
        for (int i = 0; i < numberOfWorkers; ++i) {
            Thread worker = new Thread(new Worker(mainTaskQueue, isShutdownInitiated), String.format("Worker-%s", i));
            workerThreads.add(worker);
        }

        for (Thread worker: workerThreads) {
            worker.start();
        }
    }

    public void submitTask(Runnable task) {
        try {
            mainTaskQueue.put(task);
        } catch (InterruptedException ex) {
            System.out.printf("Exception while submitting the task : %s\n", ex.getMessage());
        }
    }

    public void shutdown() {
        isShutdownInitiated.set(true);
        System.out.printf("shutdown triggered: will complete the tasks in progress and shutdown the processor. Number of tasks waiting - %s\n", mainTaskQueue.size());
    }

    public static void main(String[] args) {
        List<Runnable> tasks = new ArrayList<>();

        // creating 10 tasks which take 5 seconds each to complete.
        for (int i = 0; i < 10; ++i) {
            final int taskId = i;
            tasks.add(() -> {
                System.out.printf("starting task - %s\n", taskId);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    System.out.printf("task-%s interrupted while simulating sleep\n", taskId);
                }
                System.out.printf("finished task - %s\n", taskId);
            });
        }

        // has 3 workers
        TaskProcessor tp = new TaskProcessor(3);
        for(Runnable task: tasks) {
            tp.submitTask(task);
        }

        try {
            Thread.sleep(6000);
        } catch (InterruptedException ex) {
            System.out.printf("Error while sleeping in main function - %s\n", ex.getMessage());
        }

        tp.shutdown();
    }
}
