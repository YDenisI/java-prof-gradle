package ru.gpncr;

public class Banknote {
    private int denomination;
    private int quantity;

    public Banknote(int denomination, int quantity) {
        this.denomination = denomination;
        this.quantity = quantity;
    }

    public int getDenomination() {
        return denomination;
    }

    public int getQuantity() {
        return quantity;
    }

    public void removeQuantity(int count) {
        this.quantity -= count;
    }
}
