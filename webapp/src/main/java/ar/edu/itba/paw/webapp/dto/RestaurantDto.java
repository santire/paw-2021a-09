package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

public class RestaurantDto {

    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private double rating;
    private User owner;
    private List<Tags> tags;
    private List<MenuItem> menu;
    private Image profileImage;
    private int likes;
    private boolean userLikesRestaurant;
    private List<Rating> ratings;
    private String facebook;
    private String instagram;
    private String twitter;
    private List<MenuItem> menuPage;
    private String url;

    public static RestaurantDto fromRestaurant(Restaurant restaurant, String url){
        final RestaurantDto dto = new RestaurantDto();
        dto.id = restaurant.getId();
        dto.name = restaurant.getName();
        dto.address = restaurant.getAddress();
        dto.phoneNumber = restaurant.getPhoneNumber();
        dto.rating = restaurant.getRating();
        dto.likes = restaurant.getLikes();
        dto.owner = restaurant.getOwner();
        dto.tags = restaurant.getTags();
        dto.menu = restaurant.getMenu();
        dto.ratings = restaurant.getRatings();
        dto.instagram = restaurant.getInstagram();
        dto.facebook = restaurant.getFacebook();
        dto.twitter = restaurant.getTwitter();
        dto.url = url;

        return dto;
    }



    public Long getId() {return id;}

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public User getOwner() {
        return owner;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public double getRating() {
        return rating;
    }


    public Image getProfileImage() {
        return profileImage;
    }

    public int getLikes() {
        return likes;
    }

    public List<MenuItem> getMenuPage() {
        return menuPage;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setMenu(List<MenuItem> menu) {
        this.menu = menu;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setMenuPage(List<MenuItem> menuPage) {
        this.menuPage = menuPage;
    }
}


