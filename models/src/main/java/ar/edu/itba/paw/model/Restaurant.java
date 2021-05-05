package ar.edu.itba.paw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Restaurant {
    private final long id;
    private String name;
    private String address;
    private String phoneNumber;
    private float rating;
    private long userId;
    private int likes;

    private List<Tags> tags;
    private List<MenuItem> menu;
    private Optional<Image> profileImage;

    public Restaurant(long id, String name, String address, String phoneNumber, float rating, long userId){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.userId = userId;
        this.likes = 0;
        this.menu = new ArrayList<>();
    }

    public Restaurant(long id, String name, String address, String phoneNumber, float rating, long userId, int likes){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.userId = userId;
        this.likes = likes;
        this.menu = new ArrayList<>();
    }

    public Restaurant(long id, String name, String address, String phoneNumber, long userId){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.likes=0;
        this.menu = new ArrayList<>();
    }

    public long getId(){ return this.id; }
    public String getName(){ return this.name; }
    public String getAddress(){ return this.address; }
    public String getPhoneNumber(){ return this.phoneNumber; }
    public float getRating() { return rating; }
    public long getUserId() { return userId; }
    public int getLikes() { return likes; }
    public List<MenuItem> getMenu() { return this.menu; }
    public List<Tags> getTags() {return this.tags;}
    public Optional<Image> getMaybeProfileImage() { return this.profileImage; }

    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setRating(float rating) { this.rating = rating; }
    public void setUserId(long userId) { this.userId = userId; }
    public void setLikes(int likes) { this.likes = likes; }
    public void setTags(List<Tags> tags) { this.tags = tags; }
    public void addMenuItem(MenuItem menuItem) { this.menu.add(menuItem); }
    public void setProfileImage(Image profileImage) { this.profileImage = Optional.ofNullable(profileImage); }
}
