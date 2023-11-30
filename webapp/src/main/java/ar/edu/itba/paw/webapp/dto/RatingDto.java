package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Rating;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class RatingDto {
    private double rating;
    private URI self;
    private URI restaurant;
    private URI user;


    public static RatingDto fromRating(Rating rating, UriInfo uriInfo) {
        final RatingDto dto = new RatingDto();

        dto.rating = rating.getRating();
        dto.self = uriInfo.getBaseUriBuilder()
                .path("users")
                .path(rating.getUser().getId().toString()).path("ratings")
                .path(rating.getRestaurant().getId().toString())
                .build();
        dto.restaurant = uriInfo.getBaseUriBuilder()
                .path("restaurants")
                .path(rating.getRestaurant().getId().toString())
                .build();
        dto.user = uriInfo.getBaseUriBuilder()
                .path("users")
                .path(rating.getUser().getId().toString())
                .build();

        return dto;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(URI restaurant) {
        this.restaurant = restaurant;
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
