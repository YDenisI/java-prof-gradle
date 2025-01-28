package ru.gpncr;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            CashStorage cashStorage = new CashStorage();
            cashStorage.addBanknote(1000, 10); // 10 банкнот по 1000
            cashStorage.addBanknote(500, 1); // 1 банкнот по 500
            cashStorage.addBanknote(2000, 6); // 12 банкнот по 2000
            cashStorage.addBanknote(100, 100); // 200 банкнот по 100
            cashStorage.addBanknote(200, 15); // 30 банкнот по 200
            cashStorage.addBanknote(5000, 2); // 10 банкнот по 50001

            // Создаем банкомат
            ATM atm = new ATM(cashStorage);

            // Создаем банковскую карту с начальными средствами
            BankCard bankCard = new BankCard(50021.6); // баланс карты 5000

            Scanner scanner = new Scanner(System.in);
            while (true) {
                log.info("1. Check balance");
                log.info("2. Withdraw cash");
                log.info("3. Add cash to the Card");
                log.info("4. Показать доступные банкноты в банкомате");
                log.info("0. Exit");
                log.info("Select an operation: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        atm.checkBalance(bankCard);
                        break;
                    case 2:
                        log.info("Enter the amount to withdraw: ");
                        int amountToWithdraw = scanner.nextInt();
                        atm.withdraw(bankCard, amountToWithdraw);
                        break;
                    case 3:
                        log.info("Enter the denomination of the banknote: ");
                        int denominationToAdd = scanner.nextInt();
                        log.info("Enter the number of banknotes");
                        int quantityToAdd = scanner.nextInt();
                        atm.addCash(bankCard, denominationToAdd, quantityToAdd);
                        break;
                    case 4:
                        atm.displayAvailableCash();
                        break;
                    case 0:
                        log.info("Exit");
                        scanner.close();
                        return;
                    default:
                        log.info("Invalid choice. Please try again..");
                }
            }
        } catch (ATMException e) {
            log.info(e.getMessage());
        }
    }
}
