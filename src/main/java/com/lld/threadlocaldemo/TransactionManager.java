package com.lld.threadlocaldemo;

class Transaction {}

public class TransactionManager {
    private static ThreadLocal<Transaction> transactionContext = new ThreadLocal<>();

    public static void beginTransaction() {
        // good practise: check for existing transactions
        if (transactionContext.get() != null) {
            throw new IllegalStateException("transaction already exists");
        }

        transactionContext.set(new Transaction());
    }

    public static void clearTransaction() {
        transactionContext.remove(); // do not set(null)
    }

    // An interface to enable try-with-resources

    public static class TransactionScope implements AutoCloseable {
        public TransactionScope() {
            TransactionManager.beginTransaction();
        }

        @Override
        public void close() {
            clearTransaction();
        }
    }
}

class TestTransactionManager {
    public static void main(String[] args) {
        // try-with-resources, performs auto cleanup
        try(var scope = new TransactionManager.TransactionScope()) {

        }
    }
}
