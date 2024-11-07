package inventory.interfaces;

/**
 * Interface defining the core inventory management operations
 * that must be implemented by inventory managers.
 *
 * @author Akhilesh Nevatia
 */
public interface InventoryInterface {
    /**
     * Updates the stock levels in the inventory
     * @return boolean indicating update success
     */
    boolean updateStock();

    /**
     * Checks for products with low stock levels
     * @return boolean indicating if any products are low in stock
     */
    boolean checkLowStock();

    /**
     * Tracks products nearing expiration
     * @return boolean indicating if any products are expiring soon
     */
    boolean trackExpiry();

    /**
     * Removes obsolete products from inventory
     * @return boolean indicating removal success
     */
    boolean removeObsoleteProducts();
}