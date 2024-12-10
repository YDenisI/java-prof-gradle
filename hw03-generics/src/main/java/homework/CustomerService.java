package homework;

import java.util.*;

public class CustomerService {
    private final TreeMap<Long, List<Customer>> customerScoresMap = new TreeMap<>();
    private final Map<Long, Customer> customerDataMap = new HashMap<>();

    @SuppressWarnings("unused")
    public void add(Customer customer, String data) {
        customerDataMap.put(customer.getId(), customer);
        customerScoresMap
                .computeIfAbsent(customer.getScores(), k -> new ArrayList<>())
                .add(customer);
    }

    public Map.Entry<Customer, String> getSmallest() {
        if (customerScoresMap.isEmpty()) {
            return null;
        }
        Map.Entry<Long, List<Customer>> smallestEntry = customerScoresMap.firstEntry();
        Customer smallestCustomer = smallestEntry.getValue().get(0); // Берем первого клиента
        return new AbstractMap.SimpleEntry<>(
                new Customer(smallestCustomer.getId(), smallestCustomer.getName(), smallestCustomer.getScores()),
                "Data for " + smallestCustomer.getName());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        long score = customer.getScores();
        Map.Entry<Long, List<Customer>> nextEntry = customerScoresMap.higherEntry(score);
        if (nextEntry != null) {
            Customer nextCustomer = nextEntry.getValue().get(0);
            return new AbstractMap.SimpleEntry<>(
                    new Customer(nextCustomer.getId(), nextCustomer.getName(), nextCustomer.getScores()),
                    "Data for " + nextCustomer.getName());
        }
        return null;
    }
}
