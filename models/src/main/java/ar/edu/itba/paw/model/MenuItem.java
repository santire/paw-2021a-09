package ar.edu.itba.paw.model;

public class MenuItem {

    private final long id;
    private String name;
    private String description;
    private float price;
    private long restaurantId;

    public MenuItem(long id, String name, String description, float price, long restaurantId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.restaurantId = restaurantId;
    }
    
    public MenuItem(long id, String name, String description, float price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

}
