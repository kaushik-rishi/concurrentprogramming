package com.lld.testingexceptionhandling;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadRestart {

    private static final int MAX_RETRIES = 3;
    private static final int BACKOFF_MILLIS = 1000;
    private static BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
    private static final AtomicInteger taskCounter = new AtomicInteger(0);


    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        for(int i = 0; i < 10; i++){
            queue.add(i);
        }

        AtomicReference<Thread>  consumerThread = new AtomicReference<>();
        Runnable createConsumer = () -> {
            int retries = 0;
            while (retries <= MAX_RETRIES){
                try{
                    Integer item = queue.take();
                    taskCounter.incrementAndGet();
                    if(item % 2 == 0){
                        throw new IllegalArgumentException("Item is Even, error");
                    }
                    System.out.println("Item processed " + item);
                    retries = 0;
                } catch (IllegalArgumentException e){
                    retries++;
                    System.err.println("Task failed, Retrying " + retries + " Reason " + e.getMessage());
                } catch(InterruptedException e){
                    System.err.println("Task Interrupted! " + e.getMessage());
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e){
                    System.err.println("Error processing items " + e.getMessage());
                    retries++;
                }
                if(retries > 0){
                    try {
                        Thread.sleep(BACKOFF_MILLIS * retries);
                    } catch(InterruptedException e){
                        System.err.println("Wait was Interrupted " + e.getMessage());
                    }
                }

            }
            if(retries > MAX_RETRIES){
                System.err.println("Retries exhausted, task failed after max retries");
            }
        };

        consumerThread.set(null);
        if(queue.size() > 0){
            consumerThread.set( new Thread(createConsumer));
            executor.submit(consumerThread.get());
        }

        consumerThread.set( new Thread(createConsumer));
        executor.submit(consumerThread.get());
        executor.shutdown();
    }
}
