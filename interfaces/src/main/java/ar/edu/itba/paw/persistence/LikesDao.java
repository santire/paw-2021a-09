package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Restaurant;

import java.util.List;

public interface LikesDao {
    public boolean like(long userId, long restaurantId);
    public boolean dislike(long userId, long restaurantId);
    public List<Long> getLikedRestaurantsId(long userId);
    public boolean userLikesRestaurant(long userId, long restaurantId);
}
