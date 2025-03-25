package ru.gpncr.cachehw;

import java.util.*;
import ru.gpncr.crm.model.ClientKey;

@SuppressWarnings({"java:S6201", "java:S1905"})
public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        V value = cache.remove(key);
        if (value != null) {
            notifyListeners(key, value, "remove");
        }
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        for (HwListener<K, V> listener : listeners) {
            listener.notify(key, value, action);
        }
    }

    public Collection<V> values() {
        return cache.values();
    }

    public boolean containsKey(Long clientId) {
        for (K key : cache.keySet()) {
            if (key instanceof ClientKey) {
                ClientKey existingKey = (ClientKey) key;
                if (existingKey.getClientId().equals(clientId)) {
                    return true; // Ключ найден
                }
            }
        }
        return false;
    }
}
