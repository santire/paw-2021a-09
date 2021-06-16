package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.RatingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Optional<Rating> getRating(long userId, long restaurantId){
        return ratingDao.getRating(userId, restaurantId);
    }

    @Override
    @Transactional
    public Rating rateRestaurant(long userId, long restaurantId, double rating){
        Optional<Rating> maybeRating = ratingDao.getRating(userId, restaurantId);
        if (maybeRating.isPresent()) {
            Rating ratingObj = maybeRating.get();
            ratingObj.setRating(rating);
            return ratingObj;
        }

        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        return ratingDao.createRating(user, restaurant, rating);
    }
}
