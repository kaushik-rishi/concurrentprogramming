package com.lld.threadlocaldemo;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

class SharedMapWithUserContext implements Runnable {
    public static final Map<Integer, Context> userContextPerUserId = new ConcurrentHashMap<>();
    private final Integer userId;
    private final UserRepository userRepository = new UserRepository();

    public SharedMapWithUserContext(int userId) {
        this.userId = userId;
    }

    @Override
    public void run() {
        String username = this.userRepository.getUserNameForUserId(this.userId);
        userContextPerUserId.put(userId, new Context(username));
    }
}

class ThreadLocalSharedUserContext implements Runnable {
    public static final ThreadLocal<Context> userContextPerUserId = new ThreadLocal<>();

    private final Integer userId;
    private final UserRepository userRepository = new UserRepository();

    public ThreadLocalSharedUserContext(int userId) {
        this.userId = userId;
    }

    @Override
    public void run() {
        String username = this.userRepository.getUserNameForUserId(this.userId);
        userContextPerUserId.set(new Context(username));
    }
}

class Context {
    private final String username;

    public Context(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

class UserRepository {
    public String getUserNameForUserId(Integer userId) {
        return "someUserName";
    }
}

public class ThreadLocalDemo {
    // ThreadLocal to store user context for each thread
    private static ThreadLocal<UserContext> userContextThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<Long> requestContext = new ThreadLocal<>(); // request id that propagates through the entire (thread/request) lifecycle
//    private static Map<Long, UserContext> userContextThreadLocal = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        // Create two threads simulating different user requests
        Thread userThread1 = new Thread(() -> {
            userContextThreadLocal.set(new UserContext("John", "123"));
//            userContextThreadLocal.put(Thread.currentThread().getId(), new UserContext("John", "123"));
//            Thread.currentThread().getMap().set("userContext", "John");
            processUserRequest();
//            userContextThreadLocal.remove(Thread.currentThread().getId()); // Clean up
        }, "UserThread-1");

        Thread userThread2 = new Thread(() -> {
            userContextThreadLocal.set(new UserContext("Alice", "456"));
//            Thread.currentThread().getMap().set("userContext", "Alice");
//            userContextThreadLocal.put(Thread.currentThread().getId(), new UserContext("Alice", "456"));
            processUserRequest();
//            userContextThreadLocal.remove(Thread.currentThread().getId()); // Clean up
        }, "UserThread-2");

        userThread1.start();
        userThread2.start();
    }

    private static void processUserRequest() {
        // First service call
        System.out.println(Thread.currentThread().getName() +
                " - Service1: Processing request for user: " +
                userContextThreadLocal.get().getUsername());

        logUserRequestIntoDB();

        // Simulate some processing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Second service call
        System.out.println(Thread.currentThread().getName() +
                " - Service2: Processing request for user: " +
                userContextThreadLocal.get().getUsername());
    }

    private static void logUserRequestIntoDB() {
        System.out.println(userContextThreadLocal.get().getUsername());
    }
}

// Class to hold user-specific data
class UserContext {
    private String username;
    private String userId;

    public UserContext(String username, String userId) {
        this.username = username;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }
}
