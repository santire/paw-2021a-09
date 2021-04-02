package ar.edu.itba.paw.model;

public class Restaurant {
    private final long id;
    private String name;
    private String address;
    private String phoneNumber;
    private float rating;
    private long userId;

    public Restaurant(long id, String name, String address, String phoneNumber, float rating, long userId){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.userId = userId;
    }

    public Restaurant(long id, String name, String address, String phoneNumber, long userId){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
    }

    public long getId(){ return this.id; }
    public String getName(){ return this.name; }
    public String getAddress(){ return this.address; }
    public String getPhoneNumber(){ return this.phoneNumber; }
    public float getRating() { return rating; }
    public long getUserId() { return userId; }

    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setRating(float rating) { this.rating = rating; }
    public void setUserId(long userId) { this.userId = userId; }
}
