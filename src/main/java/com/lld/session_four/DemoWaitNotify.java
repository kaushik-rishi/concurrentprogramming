package com.lld.session_four;

// PURELY POC - FOR SESSION PURPOSE - check recording
public class DemoWaitNotify {
    public static void main(String[] args) throws InterruptedException {
        Object lc = new Object();

        Thread t1 = new Thread(() -> {
            try {
                synchronized (lc) {
                    // file io
//                    new File().read();
                    Thread.sleep(3000);
                    // 10 second computation
                }
            } catch (InterruptedException ignored) {}
        });

        t1.start();
    }
}
