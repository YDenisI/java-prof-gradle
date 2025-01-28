package ru.gpncr;

@SuppressWarnings({"java:S1126"})
public class BankCard {

    private double balance;

    public BankCard(double balance) {
        this.balance = balance;
    }

    public boolean canDispense(double amount) {
        if (balance >= amount) {
            return true;
        }
        return false;
    }

    public void dispense(double amount) {
        balance -= amount;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }
}
