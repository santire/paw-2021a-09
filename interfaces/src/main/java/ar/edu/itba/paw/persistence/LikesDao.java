package ar.edu.itba.paw.persistence;



import java.util.List;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

public interface LikesDao {
    public boolean like(User user, Restaurant restaurant);
    public boolean dislike(long userId, long restaurantId);
    public boolean userLikesRestaurant(long userId, long restaurantId);
    public List<Long> getLikesByUserId(long userId);
}
