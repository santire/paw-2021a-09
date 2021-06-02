package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.RatingDao;
import ar.edu.itba.paw.persistence.RestaurantDao;
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

    // @Autowired
    // private RestaurantDao restaurantDao;

    @Autowired
    private RestaurantService restaurantService;

    @Override
    public Optional<Rating> getRating(long userId, long restaurantId){
        return ratingDao.getRating(userId, restaurantId);
    }

    // @Override
    // public List<Rating> getRatedRestaurantsByUserId(long userId){
        // return ratingDao.getRatedRestaurantsByUserId(userId);
    // }

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

    // @Override
    // public boolean modifyRestaurantRating(long userId, long restaurantId, int rating){
        // return ratingDao.modifyRestaurantRating(userId, restaurantId, rating);
    // }

    // @Override
    // public boolean updateAvgRating(long restaurantId, int rating){
        // Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        // if (restaurant.isPresent()){
            // int numberOfRates = ratingDao.getNumberOfRates(restaurantId);
            // float currentAvgRating = restaurant.get().getRating();
            // float sumOfRatings = numberOfRates * currentAvgRating;
            // sumOfRatings+=rating;
            // int newAvg =Math.round(sumOfRatings/(numberOfRates+1));
            // restaurantDao.updateRating(restaurantId, newAvg);
        // }
        // return false;
    // }

}
