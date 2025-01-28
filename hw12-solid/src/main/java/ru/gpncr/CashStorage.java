package ru.gpncr;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CashStorage {
    private NavigableMap<Integer, Banknote> banknotes;

    public CashStorage() {
        this.banknotes = new TreeMap<>(Collections.reverseOrder());
    }

    public void addBanknote(int denomination, int quantity) {
        banknotes.put(denomination, new Banknote(denomination, quantity));
    }

    public NavigableMap<Integer, Banknote> getAllBanknotes() {
        return banknotes;
    }

    public double getSum() {
        return banknotes.values().stream()
                .mapToInt(b -> b.getDenomination() * b.getQuantity())
                .sum();
    }
}
