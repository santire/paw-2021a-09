package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface RatingDao {
    public Optional<Rating> getRating(long userId, long restaurantId);
    public Rating createRating(User user, Restaurant restaurant, double rating);
}
