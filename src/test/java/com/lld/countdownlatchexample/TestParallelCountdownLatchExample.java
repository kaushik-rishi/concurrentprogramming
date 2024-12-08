package com.lld.countdownlatchexample;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TestParallelCountdownLatchExample {
    @Test
    public void test1000ThreadsParallel() throws InterruptedException {
        List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch callingThreadBlocker = new CountDownLatch(1);
        CountDownLatch completedThreadCounter = new CountDownLatch(100);
        List<Thread> workers = Stream.generate(() -> new Thread(new WaitingWorker(outputScraper, new CountDownLatch(1000), callingThreadBlocker, completedThreadCounter))).limit(100).toList();

        workers.forEach(Thread::start);
        callingThreadBlocker.countDown();
        completedThreadCounter.await();
        outputScraper.add("Latch released");

        assertEquals(outputScraper, List.of(1));
        //         assertEquals(outputScraper)
//                .containsExactly(
//                        "Counted down",
//                        "Counted down",
//                        "Counted down",
//                        "Counted down",
//                        "Counted down",
//                        "Latch released"
//                );

    }
}
