package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RatingDao {
    public Optional<Rating> getRating(long userId, long restaurantId);
    public List<Rating> getRatedRestaurantsByUserId(long userId);
    public Rating rateRestaurant(long userId, long restaurantId, int rating);
    public boolean modifyRestaurantRating(long userId, long restaurantId, int rating);
    public int getNumberOfRates(long restaurantId);
}
