package ru.gpncr;

import java.util.Map;
import java.util.NavigableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"java:S115", "java:S1126", "java:S3776"})
class CashDispenser implements ICashDispenser {
    private static final Logger log = LoggerFactory.getLogger(CashDispenser.class);
    private final CashStorage cashStorage;

    public CashDispenser(CashStorage cashStorage) {
        this.cashStorage = cashStorage;
    }

    @Override
    public void withdraw(int amount) {
        NavigableMap<Integer, Banknote> allBanknote = cashStorage.getAllBanknotes();

        if (allBanknote != null) {
            log.info("Withdrawn banknotes: ");
            for (Map.Entry<Integer, Banknote> entry :
                    cashStorage.getAllBanknotes().entrySet()) {

                Banknote banknote = entry.getValue();
                int denomination = entry.getKey();
                int count = banknote.getQuantity();
                int cnt = 0;
                for (int i = 0; i < count; i++) {
                    if ((amount - denomination) >= 0) {
                        amount -= denomination;
                        banknote.removeQuantity(1);
                        cnt++;
                    } else break;
                }
                if (cnt > 0) log.info("{} denomination {}", cnt, denomination);
                if (amount == 0) break;
            }
        }
    }

    @Override
    public boolean canDispense(int amount) {
        if (cashStorage.getSum() >= amount) {
            return true;
        }
        return false;
    }

    public CashStorage getCashStorage() {
        return cashStorage;
    }
}
