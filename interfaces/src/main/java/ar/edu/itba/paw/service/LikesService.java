package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Restaurant;

import java.util.List;

public interface LikesService {
    public boolean like(long userId, long restaurantId);
    public boolean dislike(long userId, long restaurantId);
    public boolean userLikesRestaurant(long userId, long restaurantId);
}
