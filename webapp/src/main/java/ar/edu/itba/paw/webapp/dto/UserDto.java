package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.ws.rs.core.UriInfo;
import java.net.URI;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long userId;
    private String username;
    private String password;
    private String url;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private URI likes;
    private URI ratings;
    private URI reservations;
    private URI restaurants;


    public static UserDto fromUser(User user, String url, UriInfo uriInfo) {
        final UserDto dto = new UserDto();

        dto.userId = user.getId();
        dto.username = user.getUsername();
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();
        dto.email = user.getEmail();
        dto.phone = user.getPhone();

        dto.url = url;


        dto.likes = uriInfo.getAbsolutePathBuilder().path("likes").build();
        dto.ratings = uriInfo.getAbsolutePathBuilder().path("ratings").build();

        dto.restaurants = uriInfo.getAbsolutePathBuilder().path("restaurants").build();
        dto.reservations = uriInfo.getBaseUriBuilder().path("reservations").queryParam("madeBy", user.getId()).build();

        return dto;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
