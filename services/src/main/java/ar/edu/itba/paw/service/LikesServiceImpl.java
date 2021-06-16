package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.LikesDao;
import ar.edu.itba.paw.persistence.RestaurantDao;
import ar.edu.itba.paw.persistence.UserDao;

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
        return likesDao.like(user, restaurant);
    }

    @Override
    @Transactional
    public boolean dislike(long userId, long restaurantId) {
        return likesDao.dislike(userId, restaurantId);
    }

    @Override
    @Transactional
    public boolean userLikesRestaurant(long userId, long restaurantId){
    return likesDao.userLikesRestaurant(userId, restaurantId);
    }
}
