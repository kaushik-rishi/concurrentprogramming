package com.lld.session_three;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class CPUConsumptionDemo {
    static volatile boolean keepRunning = true;  // Prevent JVM optimization
    static volatile double result = 0;  // Prevent JVM optimization

    public static void main(String[] args) throws InterruptedException {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

        // High CPU Thread
        Thread highCPUThread = new Thread(() -> {
            long threadId = Thread.currentThread().getId();
            long startCPUTime = threadBean.getThreadCpuTime(threadId);

            // Run CPU-intensive operations for 5 seconds
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 5000) {
                for (int i = 0; i < 100000; i++) {
                    result += Math.sin(i) * Math.cos(i); // Complex calculation
                }
            }

            long cpuTime = (threadBean.getThreadCpuTime(threadId) - startCPUTime) / 1_000_000;
            System.out.println("High CPU Thread actual CPU time: " + cpuTime + "ms");
        }, "HighCPUThread");

        // Low CPU Thread
        Thread lowCPUThread = new Thread(() -> {
            long threadId = Thread.currentThread().getId();
            long startCPUTime = threadBean.getThreadCpuTime(threadId);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long cpuTime = (threadBean.getThreadCpuTime(threadId) - startCPUTime) / 1_000_000;
            System.out.println("Low CPU Thread actual CPU time: " + cpuTime + "ms");
        }, "LowCPUThread");

        System.out.println(highCPUThread.getState());
        System.out.println(lowCPUThread.getState());

        // Start threads
        System.out.println("Starting threads...");
        highCPUThread.start();
        lowCPUThread.start();

        // Wait for completion
        highCPUThread.join();
        lowCPUThread.join();

        // Print final result to prevent JVM optimization
        System.out.println("Final result (to prevent optimization): " + result);
    }
}
