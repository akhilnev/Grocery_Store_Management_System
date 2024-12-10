package deli.model;

import java.util.List;

public class DeliOrder {
    private String orderId;
    private List<String> items;
    private double totalPrice;

    public DeliOrder(String orderId, List<String> items, double totalPrice) {
        this.orderId = orderId;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public String getOrderId() { return orderId; }
    public List<String> getItems() { return items; }
    public double getTotalPrice() { return totalPrice; }

    public String toLine() {
        // Format: O001|Item1, Item2|TotalPrice
        String itemList = String.join(", ", items);
        return orderId + "|" + itemList + "|" + totalPrice;
    }
}
