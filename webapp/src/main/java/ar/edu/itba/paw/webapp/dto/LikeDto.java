package ar.edu.itba.paw.webapp.dto;

public class LikeDto {
    private boolean isLiked;
    private long restaurantId;
    private long userId;

    public LikeDto(boolean isLiked, long restaurantId, long userId) {
        this.isLiked = isLiked;
        this.restaurantId = restaurantId;
        this.userId = userId;
    }

    public LikeDto(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
