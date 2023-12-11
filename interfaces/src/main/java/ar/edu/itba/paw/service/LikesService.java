package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Like;

import java.util.List;
import java.util.Optional;

public interface LikesService {
    Optional<Like> getByUserAndRestaurant(long userId, long restaurantId);

    Like like(long userId, long restaurantId);
    void dislike(long userId, long restaurantId);
    List<Like> getUserLikes(int page, int amountOnPage, Long userId);
    int getUserLikesCount(Long userId);
    List<Like> userLikesRestaurants(long userId, List<Long> restaurantId);
}
