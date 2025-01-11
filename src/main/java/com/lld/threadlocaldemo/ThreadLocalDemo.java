package com.lld.threadlocaldemo;

import java.util.Map;
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
    public static void main(String[] args) {
    }
}
