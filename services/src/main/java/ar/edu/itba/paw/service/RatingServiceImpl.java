package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.persistence.RatingDao;
import ar.edu.itba.paw.persistence.RestaurantDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {
    @Autowired
    private RatingDao ratingDao;
    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private RestaurantService restaurantService;

    @Override
    public Optional<Rating> getRating(long userId, long restaurantId){
        return ratingDao.getRating(userId, restaurantId);
    }

    @Override
    public List<Rating> getRatedRestaurantsByUserId(long userId){
        return ratingDao.getRatedRestaurantsByUserId(userId);
    }

    @Override
    public Rating rateRestaurant(long userId, long restaurantId, int rating){
        return ratingDao.rateRestaurant(userId, restaurantId, rating);
    }

    @Override
    public boolean modifyRestaurantRating(long userId, long restaurantId, int rating){
        return ratingDao.modifyRestaurantRating(userId, restaurantId, rating);
    }

    @Override
    public boolean updateAvgRating(long restaurantId, int rating){
        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        if (restaurant.isPresent()){
            int numberOfRates = ratingDao.getNumberOfRates(restaurantId);
            float currentAvgRating = restaurant.get().getRating();
            float sumOfRatings = numberOfRates * currentAvgRating;
            sumOfRatings+=rating;
            int newAvg =Math.round(sumOfRatings/(numberOfRates+1));
            restaurantDao.updateRating(restaurantId, newAvg);
        }
        return false;
    }

}
