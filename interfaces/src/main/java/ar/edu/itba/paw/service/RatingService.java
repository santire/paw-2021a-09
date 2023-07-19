package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RatingService {
    Optional<Rating> getRating(long userId, long restaurantId);
    Rating rateRestaurant(long userId, long restaurantId, double rating);

}
