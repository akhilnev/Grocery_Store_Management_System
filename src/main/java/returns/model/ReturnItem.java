package returns.model;

import inventory.model.Product;

public class ReturnItem {
    private Product product;
    private int quantity;
    private boolean damaged;
    private double refundAmount;

    public ReturnItem(Product product, int quantity, boolean damaged) {
        this.product = product;
        this.quantity = quantity;
        this.damaged = damaged;
        this.refundAmount = calculateRefund();
    }

    private double calculateRefund() {
        return product.getPrice() * quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public Product getProduct() {
        return product;
    }

    public double getRefundAmount() {
        return refundAmount;
    }
}
