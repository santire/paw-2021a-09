package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RatingService {
    public Optional<Rating> getRating(long userId, long restaurantId);
    // public List<Rating> getRatedRestaurantsByUserId(long userId);
    public Rating rateRestaurant(long userId, long restaurantId, double rating);
    // public boolean modifyRestaurantRating(long userId, long restaurantId, double rating);
    // public boolean updateAvgRating(long restaurantId, double rating);
}
