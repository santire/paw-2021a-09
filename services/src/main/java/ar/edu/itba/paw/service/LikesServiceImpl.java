package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.AlreadyLikedException;
import ar.edu.itba.paw.model.exceptions.NotLikedException;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.LikesDao;
import ar.edu.itba.paw.persistence.RestaurantDao;
import ar.edu.itba.paw.persistence.UserDao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikesServiceImpl implements LikesService {
    @Autowired
    private LikesDao likesDao;

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public boolean like(long userId, long restaurantId) {
        User user = userDao.findById(userId).orElseThrow(UserNotFoundException::new);
        Restaurant restaurant = restaurantDao.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        if (userLikesRestaurant(userId, restaurantId)) {
            throw new AlreadyLikedException();
        }
        return likesDao.like(user, restaurant);
    }

    @Override
    @Transactional
    public boolean dislike(long userId, long restaurantId) {
        User user = userDao.findById(userId).orElseThrow(UserNotFoundException::new);
        Restaurant restaurant = restaurantDao.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        if (!userLikesRestaurant(userId, restaurantId)) {
            throw new NotLikedException();
        }
        return likesDao.dislike(userId, restaurantId);
    }

    @Override
    @Transactional 
    public List<Like> getUserLikes(long userId){
        return likesDao.getLikesByUserId(userId);
    }

    @Override
    @Transactional
    public boolean userLikesRestaurant(long userId, long restaurantId){
    return likesDao.userLikesRestaurant(userId, restaurantId);
    }
    @Override
    @Transactional
    public List<Like> userLikesRestaurants(long userId, List<Long> restaurantIds){
        return likesDao.userLikesRestaurants(userId, restaurantIds);
    }
}
