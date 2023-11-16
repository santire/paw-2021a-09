package ar.edu.itba.paw.webapp.dto;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.Like;

public class LikeDto {
    private static final String USERS_PATH = "users/";
    private static final String LIKES_PATH = "likes/";
    private long restaurantId;
    private URI self;
    private boolean liked;

    public LikeDto() {}

    public LikeDto(boolean liked, long restaurantId, long userId, String url, UriInfo uriInfo) {
        this.liked = liked;
        this.restaurantId = restaurantId;
        this.self = uriInfo.getBaseUriBuilder()
            .path(USERS_PATH).path(String.valueOf(userId))
            .path(LIKES_PATH).path(String.valueOf(restaurantId)).build();
    }

    public static LikeDto fromLike(Like like, String url, UriInfo uriInfo){
        final LikeDto dto = new LikeDto();

        dto.restaurantId = like.getRestaurant().getId();
        dto.self = uriInfo.getBaseUriBuilder()
            .path(USERS_PATH).path(String.valueOf(like.getUser().getId()))
            .path(LIKES_PATH).path(String.valueOf(dto.restaurantId)).build();
        return dto;
    }


    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public URI getSelf(){
        return self;
    }

    public void setSelf(URI selfUri){
        this.self = selfUri;
    }

    public boolean getLiked(){
        return liked;
    }

    public void setLiked(boolean liked){
        this.liked = liked;
    }
}
