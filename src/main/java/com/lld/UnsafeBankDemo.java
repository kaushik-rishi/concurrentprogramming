package com.lld;

class UnsafeBankAccount {
    private double balance;  // Shared resource without protection
    private final String accountNumber;

    public UnsafeBankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    // Unsafe deposit method without synchronization
    public void deposit(double amount) {
        System.out.println(Thread.currentThread().getName() + " trying to deposit");

        // Simulate reading the current balance
        double currentBalance = balance;

        try {
            // Simulate some processing time - This makes race conditions more likely
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Update the balance - This operation is not atomic!
        balance = currentBalance + amount;

        System.out.println(Thread.currentThread().getName() + " deposited " + amount +
                ". Supposed New balance: " + balance);
    }

    // Unsafe withdraw method without synchronization
    public void withdraw(double amount) {
        System.out.println(Thread.currentThread().getName() + " trying to withdraw");

        // Simulate reading the current balance
        double currentBalance = balance;

        try {
            // Simulate some processing time
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (currentBalance >= amount) {  // Race condition possible here!
            // Between the check and the actual withdrawal, balance might have changed
            balance = currentBalance - amount;
            System.out.println(Thread.currentThread().getName() + " withdrew " + amount +
                    ". Supposed New balance: " + balance);
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " failed to withdraw due to insufficient funds");
        }
    }

    public double checkBalance() {
        return balance;  // Even this simple read could get an inconsistent value
    }
}

public class UnsafeBankDemo {
    public static void main(String[] args) {
        // Create an account with initial balance of 1000
        UnsafeBankAccount account = new UnsafeBankAccount("ACC001", 1000.0);

        // Create multiple threads performing concurrent operations
        Thread[] depositThreads = new Thread[5];
        Thread[] withdrawThreads = new Thread[5];

        // Create 5 deposit threads, each depositing 100
        for(int i = 0; i < 5; i++) {
            depositThreads[i] = new Thread(() -> {
                account.deposit(100.0);
            }, "DepositThread-" + i);
        }

        // Create 5 withdraw threads, each withdrawing 100
        for(int i = 0; i < 5; i++) {
            withdrawThreads[i] = new Thread(() -> {
                account.withdraw(100.0);
            }, "WithdrawThread-" + i);
        }

        // Start all threads
        for(int i = 0; i < 5; i++) {
            depositThreads[i].start();
            withdrawThreads[i].start();
        }

        // Wait for all threads to complete
        try {
            for(int i = 0; i < 5; i++) {
                depositThreads[i].join();
                withdrawThreads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // The final balance should be 1000 (initial) + 500 (deposits) - 500 (withdrawals) = 1000
        // But it won't be due to race conditions!
        System.out.println("Expected final balance: 1000");
        System.out.println("Actual final balance: " + account.checkBalance());
    }
}
