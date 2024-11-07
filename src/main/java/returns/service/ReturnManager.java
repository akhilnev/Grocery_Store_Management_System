package returns.service;

import returns.model.Return;
import inventory.service.InventoryManager;
import inventory.model.Product;
import java.util.*;
import java.io.*;
import java.time.LocalDateTime;

public class ReturnManager {
    private Map<String, Return> returns;
    private String storeId;
    private InventoryManager inventoryManager;

    public ReturnManager(String storeId, InventoryManager inventoryManager) {
        this.storeId = storeId;
        this.inventoryManager = inventoryManager;
        this.returns = new HashMap<>();
        loadReturns();
    }

    public boolean validateOrder(String orderId) {
        // Read from sales data to validate order
        String fileName = "./src/main/java/store/data/" + storeId + "_sales.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[1].equals(orderId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error validating order: " + e.getMessage());
        }
        return false;
    }

    public boolean processReturn(Return returnOrder) {
        returns.put(returnOrder.getReturnId(), returnOrder);
        saveReturns();
        return true;
    }

    public boolean approveReturn(String returnId) {
        Return ret = returns.get(returnId);
        if (ret != null && !ret.isApproved()) {
            ret.approve();
            saveReturns();
            return true;
        }
        return false;
    }

    public boolean processApprovedReturns() {
        boolean updated = false;
        for (Return ret : returns.values()) {
            if (ret.isApproved()) {
                for (Map.Entry<Product, Integer> entry : ret.getItems().entrySet()) {
                    if (!ret.isDamaged()) {
                        Product product = entry.getKey();
                        int quantity = entry.getValue();
                        inventoryManager.restockProduct(product.getId(), quantity);
                        updated = true;
                    }
                }
            }
        }
        return updated;
    }

    public Map<String, Return> getAllReturns() {
        return Collections.unmodifiableMap(returns);
    }

    private void loadReturns() {
        String fileName = "./src/main/java/store/data/" + storeId + "_returns.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Format: returnId,orderId,refundAmount,approved,damaged
                String[] parts = line.split(",");
                Return ret = new Return(parts[0], parts[1], storeId);
                ret.setRefundMethod(parts[2]);
                if (Boolean.parseBoolean(parts[3])) {
                    ret.approve();
                }
                returns.put(ret.getReturnId(), ret);
            }
        } catch (IOException e) {
            System.err.println("No returns history found for store: " + storeId);
        }
    }

    private void saveReturns() {
        String fileName = "./src/main/java/store/data/" + storeId + "_returns.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Return ret : returns.values()) {
                writer.write(String.format("%s,%s,%s,%b,%b\n",
                    ret.getReturnId(),
                    ret.getOriginalOrderId(),
                    ret.getRefundMethod(),
                    ret.isApproved(),
                    ret.isDamaged()));
            }
        } catch (IOException e) {
            System.err.println("Error saving returns: " + e.getMessage());
        }
    }
}