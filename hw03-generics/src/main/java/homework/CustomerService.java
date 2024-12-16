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

        Map.Entry<Customer, String> maxEntry = navMap.higherEntry(customer);
        if (maxEntry == null) return maxEntry;

        Customer copy = new Customer(
                maxEntry.getKey().getId(),
                maxEntry.getKey().getName(),
                maxEntry.getKey().getScores());
        return new AbstractMap.SimpleEntry<>(copy, maxEntry.getValue());
    }
}
