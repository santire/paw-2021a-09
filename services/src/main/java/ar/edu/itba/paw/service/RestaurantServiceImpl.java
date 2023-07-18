package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
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

    @Override
    @Transactional
    public Restaurant registerRestaurant(String name, String address, String phoneNumber, List<Tags> tags, User owner, String facebook, String twitter, String instagram) {
        return restaurantDao.registerRestaurant(name, address, phoneNumber, tags, owner, facebook, twitter, instagram);
    }

    @Override
    @Transactional
    public boolean setImageByRestaurantId(Image image, long restaurantId) {
        restaurantDao.setImageByRestaurantId(image, restaurantId);
        return true;
    }

    // READ
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
    public Optional<Restaurant> findByIdWithMenu(long id, int menuPage, int amountOnMenuPage) {
        return restaurantDao.findByIdWithMenu(menuPage, amountOnMenuPage, id);
    }

    @Override
    @Transactional
    public int findByIdWithMenuCount(long id) {
        return restaurantDao.findByIdWithMenuCount( id);
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
    public List<Restaurant> getRestaurantsFilteredBy(int page, int amountOnPage, String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice, Sorting sort, boolean desc, int lastDays) {
        return restaurantDao.getRestaurantsFilteredBy(page, amountOnPage, name, tags, minAvgPrice, maxAvgPrice, sort, desc, lastDays);
    }

    @Override
    @Transactional
    public int getRestaurantsFilteredByCount(String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice) {
        return restaurantDao.getRestaurantsFilteredByCount(name, tags, minAvgPrice, maxAvgPrice);
    }

    // UPDATE
    @Override
    @Transactional
    public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber, List<Tags> tags, String facebook, String twitter, String instagram) {
        Restaurant restaurant = findById(id).orElseThrow(RestaurantNotFoundException::new);
        if (name != null) restaurant.setName(name);
        if (address != null) restaurant.setAddress(address);
        if (phoneNumber != null) restaurant.setPhoneNumber(phoneNumber);
        if (tags != null) restaurant.setTags(tags);
        if (facebook != null) restaurant.setFacebook(facebook);
        if (twitter != null) restaurant.setTwitter(twitter);
        if (instagram != null) restaurant.setInstagram(instagram);

        return Optional.of(restaurant);
    }

    // DESTROY

    @Override
    @Transactional
    public boolean deleteRestaurantById(long id) {
        return restaurantDao.deleteRestaurantById(id);
    }

}
