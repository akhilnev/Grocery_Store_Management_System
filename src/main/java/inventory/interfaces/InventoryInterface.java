package inventory.interfaces;

public interface InventoryInterface {
    boolean updateStock();
    boolean checkLowStock();
    boolean trackExpiry();
    boolean removeObsoleteProducts();
}