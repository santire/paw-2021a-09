package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface RatingDao {
    Optional<Rating> getRating(long userId, long restaurantId);
    List<Rating> getUserRatings(int page, int amountOnPage, long userId);
    int getUserRatingsCount(long userId);

    List<Rating> getUserRatingsByRestaurant(long userId, List<Long> restaurantIds);

    Rating createRating(User user, Restaurant restaurant, double rating);

    void deleteRating(long ratingId);
}
