package com.lld.singleprocessorexamples;

import java.util.List;

public class SingleThreadedFileProcessor {
    public void processDocuments(List<String> paths) {
        for (String path: paths) {
            String fileContent = readFile(path);
            String processedContent = processContents(fileContent);
            writeFile(path, processedContent);
        }
    }

    private String readFile(String path) {
        System.out.printf("reading file %s\n", path);
        try {
            Thread.sleep(5000);
            return "dummy file content";
        } catch (InterruptedException e) {
            System.out.println("exception while reading the file");
            return "";
        }
    }

    private String processContents(String content) {
        try {
            Thread.sleep(5000);
            return "processed file content";
        } catch (InterruptedException e) {
            System.out.println("exception while processing the content");
            return "";
        }
    }

    private void writeFile(String path, String content) {
        try {
            Thread.sleep(4000);
            System.out.println("done writing to path");
        } catch (InterruptedException e) {
            System.out.println("write file");
        }
    }
}
