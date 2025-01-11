package com.lld.testingexceptionhandling;

public class CheckBehaviourOnExceptions {
    private static void checkIndependentChildThreadFailure() throws InterruptedException {
        System.out.println("initial default exception handler");
        System.out.println(Thread.getDefaultUncaughtExceptionHandler());

        /*
        as static Thread.UncaughtExceptionHandler is a `@FunctionalInterface`, we can replace it with a lambda
        (thread, throwable) -> {}
        but here we are just being verbose
         */
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.printf("Found exception %s in thread %s\n", e.getMessage(), t.getName());
            }
        });

        Thread t = new Thread(() -> {
            int result = 10/0;
            System.out.println(result);
        });

        System.out.println("starting thread that causes an ArithmeticException");
        t.start();
        System.out.println("completed the problematic thread");
        Thread.sleep(1000);
        System.out.println("still able to execute code");
    }

    /*
    if we `join()`, still the main doesn't fail, main just waits for it complete but not to return something
     */
    private static void checkDependentedChildThreadFailure() throws InterruptedException {
        Thread t = new Thread(() -> {
            int result = 10/0;
            System.out.println(result);
        });

        System.out.println("starting thread that causes an ArithmeticException");
        t.start();
        t.join();

        Thread.sleep(1000);
        System.out.println("still able to execute code");
    }

    public static void main(String[] args) throws InterruptedException {
//        checkIndependentChildThreadFailure();
        checkDependentedChildThreadFailure();
    }
}
