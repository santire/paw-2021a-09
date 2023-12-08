package ar.edu.itba.paw.persistence.models;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import org.springframework.jdbc.core.RowMapper;

public class RestaurantRow {
    public static RowMapper<RestaurantRow> rowMapper = ((rs, rowNum) -> new RestaurantRow(
            rs.getLong("restaurant_id"),
            rs.getString("name"),
            rs.getString("address"),
            rs.getString("phone_number"),
            rs.getFloat("rating"),
            rs.getLong("user_id"),
            rs.getString("facebook"),
            rs.getString("instagram"),
            rs.getString("twitter")));
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private Float rating;
    private Long ownerId;
    private String facebook;
    private String instagram;
    private String twitter;

    public RestaurantRow(Long id, String name, String address, String phoneNumber, Float rating, Long ownerId, String facebook, String instagram, String twitter) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.ownerId = ownerId;
        this.facebook = facebook;
        this.instagram = instagram;
        this.twitter = twitter;
    }

    public Restaurant toRestaurant(User owner) {
        return new Restaurant(id, name, address, phoneNumber, null, owner, facebook, twitter, instagram);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
