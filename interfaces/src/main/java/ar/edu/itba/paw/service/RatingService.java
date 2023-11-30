package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingService {
    Optional<Rating> getRating(long userId, long restaurantId);

    Rating rateRestaurant(long userId, long restaurantId, double rating);

    Rating updateRating(long userId, long restaurantId, double rating);

    List<Rating> getUserRatings(int page, int amountOnPage, long userId);
    int getUserRatingsCount(long userId);

    List <Rating> getRatingsByRestaurant(long userId, List<Long> restaurantIds);

    void deleteRating(long userId, long restaurantId);
}
