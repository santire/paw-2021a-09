package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.persistence.RatingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {
    @Autowired
    private RatingDao ratingDao;

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

}
