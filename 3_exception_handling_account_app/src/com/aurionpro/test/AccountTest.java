package com.aurionpro.test;

import com.aurionpro.exception.InsufficientFundsException;
import com.aurionpro.exception.NegativeAmountException;
import com.aurionpro.model.BankAccount;

public class AccountTest {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(500.00);

        System.out.println("\n--- Testing Deposit Operations ---");

        double deposit1 = 200.00;
        try {
            account.deposit(deposit1);
        } catch (NegativeAmountException e) {
            System.err.println("Deposit Error: Tried to deposit " + deposit1 + ". " + e.getMessage());
        }

        double deposit2 = -50.00;
        try {
            account.deposit(deposit2);
        } catch (NegativeAmountException e) {
            System.err.println("Deposit Error: Tried to deposit " + deposit2 + ". " + e.getMessage());
        }

        System.out.println("\n--- Testing Withdrawal Operations ---");

        double withdraw1 = 100.00;
        try {
            account.withdraw(withdraw1);
        } catch (NegativeAmountException | InsufficientFundsException e) {
            System.err.println("Withdrawal Error: Tried to withdraw " + withdraw1 + ". " + e.getMessage());
        }

        double withdraw2 = 700.00;
        try {
            account.withdraw(withdraw2);
        } catch (NegativeAmountException | InsufficientFundsException e) {
            System.err.println("Withdrawal Error: Tried to withdraw " + withdraw2 + ". " + e.getMessage());
        }

        double withdraw3 = -20.00;
        try {
            account.withdraw(withdraw3);
        } catch (NegativeAmountException | InsufficientFundsException e) {
            System.err.println("Withdrawal Error: Tried to withdraw " + withdraw3 + ". " + e.getMessage());
        }

        System.out.println("\n--- Final Account Status ---");
        System.out.println("Current Balance: " + account.getBalance());
    }
}
