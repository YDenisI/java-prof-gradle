package ru.gpncr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("java:S2699")
class ATMTest {
    private ATM atm;
    private CashStorage cashStorage;
    private BankCard bankCard;

    @BeforeEach
    void setUp() {
        cashStorage = new CashStorage();
        cashStorage.addBanknote(1000, 10);
        cashStorage.addBanknote(500, 1);
        cashStorage.addBanknote(2000, 6);
        cashStorage.addBanknote(100, 100);
        cashStorage.addBanknote(200, 15);
        cashStorage.addBanknote(5000, 2);

        atm = new ATM(cashStorage);
        bankCard = new BankCard(50021.6);
    }

    @Test
    void testWithdrawSuccess() {

        atm.withdraw(bankCard, 2300);
        assertEquals(47721.6, atm.checkBalance(bankCard));
        assertEquals(43200.0, atm.getTotalAmount());
    }

    @Test
    void testWithdrawNotEnoughMoneyBankcard() {
        assertThrows(ATMException.class, () -> atm.withdraw(bankCard, 120000));
    }

    @Test
    void testWithdrawNotEnoughMoneyATM() {
        assertThrows(ATMException.class, () -> atm.withdraw(bankCard, 47000));
    }

    @Test
    void testWithdrawIncorrectCashATM() {
        assertThrows(ATMException.class, () -> atm.withdraw(bankCard, 333));
    }

    @Test
    void testAddIncorrectDenomination() {
        assertThrows(ATMException.class, () -> atm.addCash(bankCard, 333, 1));
    }
}
