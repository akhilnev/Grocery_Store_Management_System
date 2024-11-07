package returns.service;

import returns.model.Return;
import inventory.service.InventoryManager;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class that manages return operations and inventory updates
 * for returned products.
 *
 * @author Hrishikesha
 */
public class ReturnManager {
    private Map<String, Return> returns;
    private String storeId;
    private InventoryManager inventoryManager;

    /**
     * Creates a new return manager for a store
     * @param storeId Store identifier
     * @param inventoryManager Inventory manager instance
     */
    public ReturnManager(String storeId, InventoryManager inventoryManager) {
        this.storeId = storeId;
        this.inventoryManager = inventoryManager;
        this.returns = new HashMap<>();
    }

    public Map<String, Return> getPendingReturns() {
        return returns.entrySet().stream()
            .filter(entry -> !entry.getValue().isApproved())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Return> getAllReturns() {
        return new HashMap<>(returns);
    }

    public boolean processReturn(Return returnOrder) {
        returns.put(returnOrder.getReturnId(), returnOrder);
        return true;
    }

    public void approveReturn(String returnId) {
        Return ret = returns.get(returnId);
        if (ret != null) {
            ret.setApproved(true);
        }
    }

    public boolean processApprovedReturns() {
        boolean anyUpdates = false;
        for (Return ret : returns.values()) {
            if (ret.isApproved()) {
                anyUpdates = true;
            }
        }
        return anyUpdates;
    }
}