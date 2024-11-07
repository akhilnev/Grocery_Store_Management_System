package store.model;

public class Store {
    private String storeId;
    private String name;
    private String location;
    private String managerName;

    public Store(String storeId, String name, String location, String managerName) {
        this.storeId = storeId;
        this.name = name;
        this.location = location;
        this.managerName = managerName;
    }

    // Getters
    public String getStoreId() { return storeId; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getManagerName() { return managerName; }
} 