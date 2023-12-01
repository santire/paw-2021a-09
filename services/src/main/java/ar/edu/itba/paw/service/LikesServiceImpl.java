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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Like like(long userId, long restaurantId) {
        User user = userDao.findById(userId).orElseThrow(UserNotFoundException::new);
        Restaurant restaurant = restaurantDao.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        if (likesDao.userLikesRestaurant(user.getId(), restaurant.getId())) {
            throw new AlreadyLikedException();
        }
        return likesDao.like(user, restaurant);
    }

    @Override
    @Transactional
    public boolean dislike(long userId, long restaurantId) {
        User user = userDao.findById(userId).orElseThrow(UserNotFoundException::new);
        Restaurant restaurant = restaurantDao.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        if (!likesDao.userLikesRestaurant(user.getId(), restaurant.getId())) {
            throw new NotLikedException();
        }
        return likesDao.dislike(user.getId(), restaurant.getId());
    }

    @Override
    @Transactional
    public List<Like> getUserLikes(int page, int amountOnPage, Long userId) {
        return likesDao.getLikesByUserId(page, amountOnPage, userId);
    }

    @Override
    public int getUserLikesCount(Long userId) {
        return likesDao.getLikesByUserIdCount(userId);
    }

    @Override
    @Transactional
    public List<Like> userLikesRestaurants(long userId, List<Long> restaurantIds) {
        return likesDao.userLikesRestaurants(userId, restaurantIds);
    }
}
