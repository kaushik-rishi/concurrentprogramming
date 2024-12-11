package com.lld.simpledateformat_race_condition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleDateFormatRaceConditionDemo {
    private static final SimpleDateFormat SHARED_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private static final ThreadLocal<SimpleDateFormat> TL_FORMATTER = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
    private static final List<String> testDates = List.of("2024-01-01", "2024-02-01", "2024-03-01", "2024-04-01");

    private static void demonstrateRaceCondition() throws InterruptedException {

        int numberOfThreads = 10;
        int iterationsPerThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        List<ParseException> exceptions = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < iterationsPerThread; j++) {
                        // Each thread repeatedly parses different dates
                        for (String dateStr : testDates) {
                            try {
                                Date date;
                                String formatted;

                                // Parse the date
                                synchronized (SHARED_FORMATTER) {
                                    date = SHARED_FORMATTER.parse(dateStr);
                                }
                                synchronized (SHARED_FORMATTER) {
                                    formatted = SHARED_FORMATTER.format(date);
                                }

                                // Verify if we got corruption
                                if (!formatted.equals(dateStr)) {
                                    System.out.println("Data corruption detected!");
                                    System.out.printf("Original: %s, Corrupted: %s\n", dateStr, formatted);
                                }
                            } catch (ParseException e) {
                                exceptions.add(e);
                            }
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        System.out.println("Race condition demonstration completed.");
        System.out.println("Number of parse exceptions: " + exceptions.size());
    }

    public static void main(String[] args) throws InterruptedException {
        demonstrateRaceCondition();
    }
}
