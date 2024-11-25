package com.lld.singleprocessorexamples;

import java.util.List;

public class Runner {
    public static void main(String[] args) {
        List<String> files = List.of("file1", "file2", "file3");

        SingleThreadedFileProcessor sfp = new SingleThreadedFileProcessor();
        sfp.processDocuments(files);
    }
}
