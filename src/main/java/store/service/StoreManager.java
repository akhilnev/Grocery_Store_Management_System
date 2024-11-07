package store.service;

import store.model.Store;
import java.io.*;
import java.util.*;

public class StoreManager {
    private Map<String, Store> stores;
    private static final String STORES_FILE = "./src/main/java/store/data/stores.txt";

    public StoreManager() {
        this.stores = new HashMap<>();
        loadStores();
    }

    private void loadStores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STORES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                stores.put(parts[0], new Store(parts[0], parts[1], parts[2], parts[3]));
            }
        } catch (IOException e) {
            System.err.println("Error loading stores: " + e.getMessage());
        }
    }

    public List<Store> getAllStores() {
        return new ArrayList<>(stores.values());
    }

    public Store getStore(String storeId) {
        return stores.get(storeId);
    }
}
