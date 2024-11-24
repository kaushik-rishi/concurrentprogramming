package com.lld;

class BankAccount {
    private double balance;
    private final Object lockObject = new Object(); // Explicit lock object
    private final String accountNumber;

    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    // Method using synchronized keyword
    public synchronized void deposit(double amount) {
        System.out.println(Thread.currentThread().getName() + " trying to deposit");
        try {
            Thread.sleep(100); // Simulate some processing time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        balance += amount;
        System.out.println(Thread.currentThread().getName() + " deposited " + amount +
                ". New balance: " + balance);
    }

    // Method using synchronized block with explicit lock object
    public void withdraw(double amount) {
        synchronized(lockObject) {
            System.out.println(Thread.currentThread().getName() + " trying to withdraw");
            try {
                Thread.sleep(100); // Simulate some processing time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (balance >= amount) {
                balance -= amount;
                System.out.println(Thread.currentThread().getName() + " withdrew " + amount +
                        ". New balance: " + balance);
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " failed to withdraw due to insufficient funds");
            }
        }
    }

    // Method using synchronized block on 'this'
    public double checkBalance() {
        synchronized(this) {
            return balance;
        }
    }
}

public class SynchronizationDemo {
    public static void main(String[] args) {
        BankAccount account = new BankAccount("ACC001", 1000.0);

        // Create multiple threads performing concurrent operations
        Thread t1 = new Thread(() -> {
            for(int i = 0; i < 3; i++) {
                account.deposit(100.0);
            }
        }, "DepositThread-1");

        Thread t2 = new Thread(() -> {
            for(int i = 0; i < 3; i++) {
                account.withdraw(200.0);
            }
        }, "WithdrawThread-1");

        Thread t3 = new Thread(() -> {
            for(int i = 0; i < 3; i++) {
                account.deposit(150.0);
            }
        }, "DepositThread-2");

        // Start all threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for all threads to complete
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final balance: " + account.checkBalance());
    }
}
