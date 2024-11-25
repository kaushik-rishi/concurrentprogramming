package com.lld.singleprocessorexamples;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
Will be more efficient / asynchronous than the SingleThreaded example
 */
class MultiThreadedProcessor {
    private final BlockingQueue<String> readQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<ProcessedDocument> writeQueue = new LinkedBlockingQueue<>();

    static class ProcessedDocument {
        String content;
        String originalPath;

        ProcessedDocument(String content, String path) {
            this.content = content;
            this.originalPath = path;
        }
    }

    public void processDocuments(List<String> filePaths) {
        // Reader Thread - Handles I/O
        Thread readerThread = new Thread(() -> {
            for(String path : filePaths) {
                String content = readFile(path);
                try {
                    readQueue.put(content);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Processor Thread - Handles CPU work
        Thread processorThread = new Thread(() -> {
            while(true) {
                try {
                    String content = readQueue.take();
                    String processed = processContent(content);
                    writeQueue.put(new ProcessedDocument(processed, "result_path"));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // Writer Thread - Handles I/O
        Thread writerThread = new Thread(() -> {
            while(true) {
                try {
                    ProcessedDocument doc = writeQueue.take();
                    saveResult(doc.content);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        readerThread.start();
        processorThread.start();
        writerThread.start();
    }
}
