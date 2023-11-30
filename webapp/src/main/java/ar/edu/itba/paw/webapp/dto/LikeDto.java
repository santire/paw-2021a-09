package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Like;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class LikeDto {
    private URI self;
    private URI restaurant;
    private URI user;


    public static LikeDto fromLike(Like like, UriInfo uriInfo) {
        final LikeDto dto = new LikeDto();

        dto.self = uriInfo.getAbsolutePathBuilder().path(like.getRestaurant().getId().toString()).build();

        dto.restaurant = uriInfo.getBaseUriBuilder().path("restaurants").path(like.getRestaurant().getId().toString()).build();
        dto.user = uriInfo.getBaseUriBuilder().path("users").path(like.getUser().getId().toString()).build();
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
}
