package ru.gpncr;

import java.util.Arrays;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"java:S2184"})
public class ATM {
    private static final Logger log = LoggerFactory.getLogger(ATM.class);
    private CashDispenser cashDispenser;

    public ATM(CashStorage cashStorage) {
        this.cashDispenser = new CashDispenser(cashStorage);
    }

    public void withdraw(BankCard card, int amount) throws ATMException {
        if (amount % 100 != 0) {
            throw new ATMException("Amount must be a multiple of 100");
        }

        if (!card.canDispense(amount)) {
            throw new ATMException("Insufficient funds in the account for withdrawal!");
        }

        if (!cashDispenser.canDispense(amount)) {
            throw new ATMException("Insufficient cash in the ATM to perform the operation!");
        }

        cashDispenser.withdraw(amount);
        card.dispense(amount);
        log.info("Operation completed.");
    }

    public double checkBalance(BankCard card) {
        log.info(
                "Card balance: {} {}",
                card.getBalance(),
                cashDispenser.getCashStorage().getSum());
        return card.getBalance();
    }

    public void addCash(BankCard bankcard, int denomination, int quantity) {

        if (!doesDenominationExist(denomination)) throw new ATMException("Incorrect denomination entered");

        Banknote entry = cashDispenser.getCashStorage().getAllBanknotes().get(denomination);
        int count = entry.getQuantity();
        cashDispenser.getCashStorage().addBanknote(denomination, count + quantity);
        bankcard.deposit(denomination * quantity);
        int s = denomination * quantity;
        log.info("Added {} banknotes of denomination {} {}", quantity, denomination, s);
    }

    public void displayAvailableCash() {
        log.info("Available banknotes in the ATM.");
        for (Map.Entry<Integer, Banknote> entry :
                cashDispenser.getCashStorage().getAllBanknotes().entrySet()) {
            int denomination = entry.getKey();
            int quantity = entry.getValue().getQuantity();
            log.info("Denomination: {}, Quantity: {}", denomination, quantity);
        }
    }

    public double getTotalAmount() {
        return cashDispenser.getCashStorage().getSum();
    }

    public static boolean doesDenominationExist(int value) {
        return Arrays.stream(Denomination.values()).anyMatch(denomination -> denomination.value == value);
    }
}
