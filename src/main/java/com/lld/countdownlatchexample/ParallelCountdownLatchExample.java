package com.lld.countdownlatchexample;

import java.util.List;
import java.util.concurrent.CountDownLatch;


// https://www.baeldung.com/java-countdown-latch
// https://learning.oreilly.com/library/view/97-things-every/9781491952689/ch15.html#Alexey_Soshin
// For advanced and flexible latches - use `CyclicBarrier`
class WaitingWorker implements Runnable {
    private List<String> outputScraper;
    private CountDownLatch readyThreadCounter;
    private CountDownLatch callingThreadBlocker;
    private CountDownLatch completedThreadCounter;

    public WaitingWorker(
            List<String> outputScraper,
            CountDownLatch readyThreadCounter,
            CountDownLatch callingThreadBlocker,
            CountDownLatch completedThreadCounter) {

        this.outputScraper = outputScraper;
        this.readyThreadCounter = readyThreadCounter;
        this.callingThreadBlocker = callingThreadBlocker;
        this.completedThreadCounter = completedThreadCounter;
    }

    private void doSomeWork() {
        outputScraper.add("working " + completedThreadCounter.getCount());
    }

    @Override
    public void run() {
        readyThreadCounter.countDown();
        try {
            callingThreadBlocker.await();
            doSomeWork();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            completedThreadCounter.countDown();
        }
    }
}
