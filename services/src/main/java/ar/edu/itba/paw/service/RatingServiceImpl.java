package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.AlreadyRatedException;
import ar.edu.itba.paw.model.exceptions.RatingNotFoundException;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.RatingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {
    @Autowired
    private RatingDao ratingDao;
    @Autowired
    private UserService userService;


    @Autowired
    private RestaurantService restaurantService;

    @Override
    @Transactional
    public Optional<Rating> getRating(long userId, long restaurantId) {
        return ratingDao.getRating(userId, restaurantId);
    }

    @Override
    @Transactional
    public List<Rating> getUserRatings(int page, int amountOnPage, long userId) {
        return ratingDao.getUserRatings(page, amountOnPage, userId);
    }

    @Override
    public int getUserRatingsCount(long userId) {
        return ratingDao.getUserRatingsCount(userId);
    }

    @Override
    public List<Rating> getRatingsByRestaurant(long userId, List<Long> restaurantIds) {
        return ratingDao.getUserRatingsByRestaurant(userId, restaurantIds);
    }

    @Override
    @Transactional
    public Rating rateRestaurant(long userId, long restaurantId, double rating) {
        Optional<Rating> maybeRating = ratingDao.getRating(userId, restaurantId);
        if (maybeRating.isPresent()) {
            throw new AlreadyRatedException();
        }

        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        return ratingDao.createRating(user, restaurant, rating);
    }

    @Override
    @Transactional
    public Rating updateRating(long userId, long restaurantId, double rating) {
        Rating ratingObj = ratingDao.getRating(userId, restaurantId).orElseThrow(RatingNotFoundException::new);
        ratingObj.setRating(rating);
        return ratingObj;
    }

    @Override
    @Transactional
    public void deleteRating(long userId, long restaurantId) {
        Rating ratingObj = ratingDao.getRating(userId, restaurantId).orElseThrow(RatingNotFoundException::new);
        ratingDao.deleteRating(ratingObj.getId());
    }

}
