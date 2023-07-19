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
    private URI reviews;
    private URI owner;

   
    public static RestaurantDto fromRestaurant(Restaurant restaurant, UriInfo uriInfo){
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
        dto.image = uriInfo.getBaseUriBuilder().path(PATH).path(String.valueOf(restaurant.getId())).path("image")
                .queryParam("v",String.valueOf(v) ).build();
        dto.menu = uriInfo.getBaseUriBuilder().path(PATH).path(String.valueOf(restaurant.getId())).path("menu").build();
        dto.reviews = uriInfo.getBaseUriBuilder().path(PATH).path(String.valueOf(restaurant.getId())).path("reviews").build();
        dto.owner = uriInfo.getBaseUriBuilder().path("users").path(restaurant.getOwner().getId().toString()).build();

        return  dto;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Double getRating() {
        return rating;
    }

    public Integer getLikes() {
        return likes;
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

    public List<String> getTags() {
        return tags;
    }

    public int getReservationsCount() {
        return reservationsCount;
    }

    public URI getMenu() {
        return menu;
    }

    public URI getReviews() {
        return reviews;
    }

    public URI getImage() {
        return image;
    }

    public URI getOwner() {
        return owner;
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

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setMenu(URI menu) {
        this.menu = menu;
    }

    public void setReviews(URI reviews) {
        this.reviews = reviews;
    }

    public void setImage(URI image) {
        this.image = image;
    }

    public void setOwner(URI owner) {
        this.owner = owner;
    }

    public void setReservationCount(int reservationsCount) {
        this.reservationsCount = reservationsCount;
    }

}

