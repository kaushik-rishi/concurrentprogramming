package com.lld;

public class TestingSynchronizedBlocks {
    private static final Object resource1 = new Object();
    private static final Object resource2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread wt1 = new Thread(() -> {
            System.out.println("t1 - start");
            synchronized (resource1) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("sleep t1 - done");
                synchronized (resource2) {
                    System.out.println("t1 - done");
                }
            }
        });

        Thread wt2 = new Thread(() -> {
            System.out.println("t2 - start");
            synchronized (resource2) {
                try {
                    Thread.sleep(15 * 1000);
                } catch (InterruptedException e){
                    System.out.println("ex");
                }
                System.out.println("t2 - done");
            }
        });

        wt1.start();
        wt2.start();

        wt1.join();
        wt2.join();
    }
}
