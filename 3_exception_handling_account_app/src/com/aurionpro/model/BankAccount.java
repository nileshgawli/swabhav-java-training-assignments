package com.aurionpro.model;

import com.aurionpro.exception.InsufficientFundsException;
import com.aurionpro.exception.NegativeAmountException;

public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
        System.out.println("Account created with initial balance: " + initialBalance);
    }

    public void deposit(double amount) throws NegativeAmountException {
        if (amount < 0) {
            throw new NegativeAmountException("Deposit amount cannot be negative.");
        }
        this.balance += amount;
        System.out.println("Deposited: " + amount + ". New balance: " + balance);
    }

    public void withdraw(double amount) throws NegativeAmountException, InsufficientFundsException {
        if (amount < 0) {
            throw new NegativeAmountException("Withdrawal amount cannot be negative.");
        }
        if (this.balance < amount) {
            throw new InsufficientFundsException("Insufficient funds. Current balance: " + balance + ", Requested: " + amount);
        }
        this.balance -= amount;
        System.out.println("Withdrew: " + amount + ". New balance: " + balance);
    }

    public double getBalance() {
        return balance;
    }
}
