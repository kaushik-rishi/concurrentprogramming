package com.lld.threadlocaldemo;

import java.util.HashMap;

public class WebScrapingWorker {
    /*
    whatever object I create of this class ---- should have a db connection of its own.
     */

//    static Map<Integer, Integer> mp = new HashMap<>();
//    ThreadLocal<DBConnection> conn = new ThreadLocal<DBConnection>(() -> new DBConnection());
//
//    static void performScraping(String url) {
//        DBConnection conn = conn.get();
//
//        String html = "<h1></h1>"; // post scraping
//        conn.getConn().query("sql");
//    }
}


class Main {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            WebScrapingWorker.performScraping("google.com");
            WebScrapingWorker.performScraping("google.com");
        });
        Thread t2 = new Thread(() -> {
            WebScrapingWorker.performScraping("linkedin.com");
        });
    }
}
