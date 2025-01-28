package ru.gpncr;

public interface ICashDispenser {
    boolean canDispense(int amount);

    void withdraw(int amount);
}
