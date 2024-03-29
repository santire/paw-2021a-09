package ar.edu.itba.paw.persistence;



import java.util.List;

import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

public interface LikesDao {
    Like like(User user, Restaurant restaurant);

    Like getByUserAndRestaurant(User user, Restaurant restaurant);

    void dislike(long userId, long restaurantId);
    boolean userLikesRestaurant(long userId, long restaurantId);
     List<Like> userLikesRestaurants(long userId, List<Long> restaurantId);
    List<Like> getLikesByUserId(int page, int amountOnPage, Long userId);
    int getLikesByUserIdCount(Long userId);
}
