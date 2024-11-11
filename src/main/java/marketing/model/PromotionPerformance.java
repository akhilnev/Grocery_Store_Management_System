package marketing.model;

public class PromotionPerformance {
    private String promotionId;
    private int totalSales;
    private double revenue;
    
    public PromotionPerformance(String promotionId) {
        this.promotionId = promotionId;
        this.totalSales = 0;
        this.revenue = 0.0;
    }

    public void addSale(double amount) {
        this.totalSales++;
        this.revenue += amount;
    }

    public int getTotalSales() { return totalSales; }
    public double getRevenue() { return revenue; }
    public String getPromotionId() { return promotionId; }
}