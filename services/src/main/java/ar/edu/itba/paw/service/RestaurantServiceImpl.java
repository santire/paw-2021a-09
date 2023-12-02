package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.RestaurantDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    @Autowired
    private RestaurantDao restaurantDao;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Restaurant registerRestaurant(String name, String address, String phoneNumber, List<Tags> tags, Long ownerId, String facebook, String twitter, String instagram) {
        User owner = userService.findById(ownerId).orElseThrow(UserNotFoundException::new);
        return restaurantDao.registerRestaurant(name, address, phoneNumber, tags, owner, facebook, twitter, instagram);
    }

    @Override
    @Transactional
    public boolean setImageByRestaurantId(Image image, long restaurantId) {
        restaurantDao.setImageByRestaurantId(image, restaurantId);
        return true;
    }

    @Override
    @Transactional
    public Optional<Restaurant> findById(long id) {
        return restaurantDao.findById(id);
    }

    @Override
    @Transactional
    public List<Restaurant> getPopularRestaurants(int limit, int minValue) {
        return restaurantDao.getPopularRestaurants(limit, minValue);
    }

    @Override
    @Transactional
    public List<Restaurant> getRestaurantsFromOwner(int page, int amountOnPage, long userId) {
        return restaurantDao.getRestaurantsFromOwner(page, amountOnPage, userId);
    }

    @Override
    @Transactional
    public int getRestaurantsFromOwnerCount(long userId) {
        return restaurantDao.getRestaurantsFromOwnerCount(userId);
    }

    @Override
    @Transactional
    public List<Restaurant> getHotRestaurants(int limit, int lastDays) {
        return restaurantDao.getHotRestaurants(limit, lastDays);
    }

    @Override
    @Transactional
    public List<Restaurant> getRestaurantsFilteredBy(int page, int amountOnPage, String name, List<Tags> tags, Long userId, double minAvgPrice, double maxAvgPrice, Sorting sort, boolean desc, int lastDays) {
        return restaurantDao.getRestaurantsFilteredBy(page, amountOnPage, name, tags,userId, minAvgPrice, maxAvgPrice, sort, desc, lastDays);
    }

    @Override
    @Transactional
    public int getRestaurantsFilteredByCount(String name, List<Tags> tags, Long userId, double minAvgPrice, double maxAvgPrice, int lastDays) {
        return restaurantDao.getRestaurantsFilteredByCount(name, tags, userId, minAvgPrice, maxAvgPrice, lastDays);
    }

    @Override
    @Transactional
    public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber, List<Tags> tags, String facebook, String twitter, String instagram) {
        Restaurant restaurant = restaurantDao.findById(id).orElseThrow(RestaurantNotFoundException::new);
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setPhoneNumber(phoneNumber);
        restaurant.setTags(tags);
        restaurant.setFacebook(facebook);
        restaurant.setTwitter(twitter);
        restaurant.setInstagram(instagram);

        return Optional.of(restaurant);
    }

    @Override
    @Transactional
    public boolean deleteRestaurantById(long id) {
        return restaurantDao.deleteRestaurantById(id);
    }

}
