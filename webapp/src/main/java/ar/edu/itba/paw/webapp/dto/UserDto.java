package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.controller.UserController;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.ws.rs.core.UriInfo;
import java.net.URI;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private URI self;
    private URI likes;
    private URI ratings;
    private URI reservations;
    private URI comments;
    private URI restaurants;

    public static UserDto fromUser(User user, UriInfo uriInfo) {
        final UserDto dto = new UserDto();

        dto.userId = user.getId();
        dto.username = user.getUsername();
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();
        dto.email = user.getEmail();
        dto.phone = user.getPhone();

        dto.self = uriInfo.getBaseUriBuilder()
                .path(UserController.PATH)
                .path(user.getId().toString())
                .build();


        dto.likes = uriInfo.getBaseUriBuilder()
                .path(UserController.PATH)
                .path(user.getId().toString())
                .path("likes").build();

        dto.ratings = uriInfo.getBaseUriBuilder()
                .path(UserController.PATH)
                .path(user.getId().toString())
                .path("ratings")
                .build();

        dto.restaurants = uriInfo.getBaseUriBuilder()
                .path("restaurants")
                .queryParam("ownedBy", user.getId())
                .build();

        dto.reservations = uriInfo
                .getBaseUriBuilder()
                .path("reservations")
                .queryParam("madeBy", user.getId())
                .build();

        dto.comments = uriInfo.getBaseUriBuilder()
                .path("comments")
                .queryParam("madeBy", user.getId())
                .build();

        return dto;
    }

    public URI getComments() {
        return comments;
    }

    public void setComments(URI comments) {
        this.comments = comments;
    }

    public URI getRatings() {
        return ratings;
    }

    public void setRatings(URI ratings) {
        this.ratings = ratings;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public URI getReservations() {
        return reservations;
    }

    public void setReservations(URI reservations) {
        this.reservations = reservations;
    }

    public URI getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(URI restaurants) {
        this.restaurants = restaurants;
    }

    public URI getLikes() {
        return likes;
    }

    public void setLikes(URI likes) {
        this.likes = likes;
    }
}
