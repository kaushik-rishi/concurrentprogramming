package com.lld.chapter_three;

public class NoVisibility {
    private static boolean ready;
    private static int value;

    private static class ReaderThread extends Thread {
        @Override
        public void run() {
            while (!ready) Thread.yield();
            System.out.println(value);
        }
    }

    public static void main(String[] args) {
        new ReaderThread().start();
        value = 42;
        ready = true;
    }
}
