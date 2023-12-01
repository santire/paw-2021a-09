package ar.edu.itba.paw.webapp.dto;


import ar.edu.itba.paw.model.Restaurant;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


public class RestaurantDto {

    private static final String PATH = "restaurants/";
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private Double rating;
    private Integer likes;
    private String facebook;
    private String instagram;
    private String twitter;
    private List<String> tags;
    private int reservationsCount;

    private URI image;
    private URI menu;

    private URI owner;
    private URI reservations;
    private URI comments;

    public static RestaurantDto fromRestaurant(Restaurant restaurant, UriInfo uriInfo) {
        final RestaurantDto dto = new RestaurantDto();
        Long v = 0L;
        if (restaurant.getProfileImage() != null) {
            v = restaurant.getProfileImage().getVersion();
        }
        dto.id = restaurant.getId();
        dto.name = restaurant.getName();
        dto.address = restaurant.getAddress();
        dto.phoneNumber = restaurant.getPhoneNumber();
        dto.rating = restaurant.getRating();
        dto.facebook = restaurant.getFacebook();
        dto.instagram = restaurant.getInstagram();
        dto.twitter = restaurant.getTwitter();
        dto.likes = restaurant.getLikes();
        dto.tags = restaurant.getTags().stream().map(Enum::name).collect(Collectors.toList());
        dto.reservationsCount = restaurant.getReservationsCount();
        dto.image = uriInfo.getBaseUriBuilder().path(PATH).path(String.valueOf(restaurant.getId())).path("image").queryParam("v", String.valueOf(v)).build();

        dto.menu = uriInfo.getBaseUriBuilder().path(PATH).path(String.valueOf(restaurant.getId())).path("menu").build();
        dto.owner = uriInfo.getBaseUriBuilder().path("users").path(restaurant.getOwner().getId().toString()).build();
        dto.reservations = uriInfo.getBaseUriBuilder().path("reservations").queryParam("madeTo", restaurant.getId()).build();
        dto.comments = uriInfo.getBaseUriBuilder().path("comments").queryParam("madeTo", restaurant.getId()).build();

        return dto;
    }

    public URI getComments() {
        return comments;
    }

    public void setComments(URI comments) {
        this.comments = comments;
    }

    public URI getReservations() {
        return reservations;
    }

    public void setReservations(URI reservations) {
        this.reservations = reservations;
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getReservationsCount() {
        return reservationsCount;
    }

    public void setReservationsCount(int reservationsCount) {
        this.reservationsCount = reservationsCount;
    }

    public URI getMenu() {
        return menu;
    }

    public void setMenu(URI menu) {
        this.menu = menu;
    }

    public URI getImage() {
        return image;
    }

    public void setImage(URI image) {
        this.image = image;
    }

    public URI getOwner() {
        return owner;
    }

    public void setOwner(URI owner) {
        this.owner = owner;
    }

    public void setReservationCount(int reservationsCount) {
        this.reservationsCount = reservationsCount;
    }

}


