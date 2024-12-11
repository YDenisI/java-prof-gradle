package homework;

import java.util.*;

public class CustomerService {
    private final NavigableMap<Customer, String> navMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public void add(Customer customer, String data) {
        Customer customerCopy = new Customer(customer.getId(), customer.getName(), customer.getScores());
        navMap.put(customerCopy, data);
    }

    public Map.Entry<Customer, String> getSmallest() {

        Map.Entry<Customer, String> smallestEntry = navMap.firstEntry();

        Customer copy = new Customer(
                smallestEntry.getKey().getId(),
                smallestEntry.getKey().getName(),
                smallestEntry.getKey().getScores());
        return new AbstractMap.SimpleEntry<>(copy, smallestEntry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        boolean found = false;

        for (Map.Entry<Customer, String> entry : navMap.entrySet()) {
            if (found) {
                return entry;
            }
            if (entry.getKey().equals(customer)) {
                found = true;
            }
        }
        return navMap.tailMap(new Customer(0, "", customer.getScores()), false).firstEntry();
    }
}
