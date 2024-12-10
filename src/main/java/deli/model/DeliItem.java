package deli.model;

public class DeliItem {
    private String itemId;
    private String name;
    private double pricePerUnit;
    private String unitType; 
    private int stockLevel;

    public DeliItem(String itemId, String name, double pricePerUnit, String unitType, int stockLevel) {
        this.itemId = itemId;
        this.name = name;
        this.pricePerUnit = pricePerUnit;
        this.unitType = unitType;
        this.stockLevel = stockLevel;
    }

    public static DeliItem fromLine(String line) {
        // Format: D001|Turkey Slices|8.99|lb|50
        String[] parts = line.split("\\|");
        if (parts.length < 5) return null;
        String id = parts[0].trim();
        String name = parts[1].trim();
        double price = Double.parseDouble(parts[2].trim());
        String utype = parts[3].trim();
        int stock = Integer.parseInt(parts[4].trim());

        return new DeliItem(id, name, price, utype, stock);
    }

    public String getItemId() { return itemId; }
    public String getName() { return name; }
    public double getPricePerUnit() { return pricePerUnit; }
    public String getUnitType() { return unitType; }
    public int getStockLevel() { return stockLevel; }

    public void reduceStock(int amount) {
        this.stockLevel = Math.max(0, this.stockLevel - amount);
    }
}
