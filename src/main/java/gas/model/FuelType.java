package gas.model;

public enum FuelType {
    REGULAR_87("Regular", 87, 3.49),
    PLUS_89("Plus", 89, 3.79),
    PREMIUM_93("Premium", 93, 4.09);

    private String name;
    private int octane;
    private double pricePerGallon;

    FuelType(String name, int octane, double pricePerGallon) {
        this.name = name;
        this.octane = octane;
        this.pricePerGallon = pricePerGallon;
    }

    public String getName() { return name; }
    public int getOctane() { return octane; }
    public double getPricePerGallon() { return pricePerGallon; }
}