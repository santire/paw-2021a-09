package ar.edu.itba.paw.model;

public class Rating {
    private long userId;
    private long restaurantId;
    private int rating;

    public Rating(long userId, long restaurantId, int rating){
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.rating = rating;
    }

    public int getRating() { return rating; }

    public long getRestaurantId() { return restaurantId; }

    public long getUserId() { return userId; }

    public void setRating(int rating) { this.rating = rating; }

    public void setRestaurantId(long restaurantId) { this.restaurantId = restaurantId; }

    public void setUserId(long userId) { this.userId = userId; }
}
