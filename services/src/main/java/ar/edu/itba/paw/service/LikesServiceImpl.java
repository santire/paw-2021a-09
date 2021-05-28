package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.persistence.LikesDao;
import ar.edu.itba.paw.persistence.RestaurantDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// @Service
// public class LikesServiceImpl implements LikesService {
    // @Autowired
    // private LikesDao likesDao;

    // @Autowired
    // private RestaurantDao restaurantDao;

    // @Override
    // public boolean like(long userId, long restaurantId){
        // return likesDao.like(userId, restaurantId);
    // }

    // @Override
    // public boolean dislike(long userId, long restaurantId){
        // if(likesDao.userLikesRestaurant(userId, restaurantId)){
            // return likesDao.dislike(userId, restaurantId);
        // }
        // return false;
    // }

    // @Override
    // public boolean userLikesRestaurant(long userId, long restaurantId){
        // return likesDao.userLikesRestaurant(userId, restaurantId);
    // }

    // @Override
    // public List<Restaurant> getLikedRestaurants(long userId){
        // List<Long> restaurantsId = likesDao.getLikedRestaurantsId(userId);
        // Optional<Restaurant> restaurant = java.util.Optional.empty();
        // List<Restaurant> restaurants = new ArrayList<>();

        // for(int i = 0; i < restaurantsId.size(); i++){
            // restaurant = restaurantDao.findById(restaurantsId.get(i));
            // if(restaurant.isPresent()){
                // restaurants.add(restaurant.get());
            // }
        // }
        // return restaurants;
    // }
// }
