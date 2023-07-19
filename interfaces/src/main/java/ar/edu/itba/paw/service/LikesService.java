package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Restaurant;

import java.util.List;

public interface LikesService {
    boolean like(long userId, long restaurantId);
    boolean dislike(long userId, long restaurantId);
    boolean userLikesRestaurant(long userId, long restaurantId);
    List<Like> userLikesRestaurants(long userId, List<Long> restaurantId);
    List<Long> getLikesByUserId(long userId);
}
