package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.persistence.RestaurantDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    @Autowired
    private RestaurantDao restaurantDao;

    @Override
    @Transactional
    public Restaurant registerRestaurant(String name, String address, String phoneNumber, List<Tags> tags, User owner) {
        return restaurantDao.registerRestaurant(name, address, phoneNumber, tags, owner);
    }

    @Override
    @Transactional
    public boolean setImageByRestaurantId(Image image, long restaurantId) {
        Restaurant restaurant = findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        image.setRestaurant(restaurant);
        restaurant.setProfileImage(image);
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
    public List<Restaurant> getLikedRestaurantsPreview(int limit, long userId) {
        return restaurantDao.getLikedRestaurantsPreview(limit, userId);
    }

    @Override
    @Transactional
    public Optional<Restaurant> findByIdWithMenu(long id, int menuPage, int amountOnMenuPage) {
        return restaurantDao.findByIdWithMenu(menuPage, amountOnMenuPage, id);
    }

    @Override
    @Transactional
    public int findByIdWithMenuPagesCount(int amountOnMenuPage, long id) {
        return restaurantDao.findByIdWithMenuPageCount(amountOnMenuPage, id);
    }

    @Override
    @Transactional
    public List<Restaurant> getRestaurantsFromOwner(int page, int amountOnPage, long userId) {
        return restaurantDao.getRestaurantsFromOwner(page, amountOnPage, userId);
    }

    @Override
    @Transactional
    public int getRestaurantsFromOwnerPagesCount(int amountOnPage, long userId) {
        return restaurantDao.getRestaurantsFromOwnerPagesCount(amountOnPage, userId);
    }

    @Override
    @Transactional
    public List<Restaurant> getAllRestaurants(int page, int amountOnPage, String searchTerm) {
        return restaurantDao.getAllRestaurants(page, amountOnPage, searchTerm);
    }

    @Override
    @Transactional
    public int getAllRestaurantPagesCount(int amountOnPage, String searchTerm) {
        return restaurantDao.getAllRestaurantPagesCount(amountOnPage, searchTerm);
    }

    @Override
    @Transactional
    public List<Restaurant> getHotRestaurants(int limit, int lastDays) {
        return restaurantDao.getHotRestaurants(limit, lastDays);
    }

    // UPDATE

    @Override
    @Transactional
    public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber,
            List<Tags> tags) {
        Restaurant restaurant = findById(id).orElseThrow(RestaurantNotFoundException::new);
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setPhoneNumber(phoneNumber);
        restaurant.setTags(tags);

        return Optional.of(restaurant);
    }

    // DESTROY

    @Override
    @Transactional
    public boolean deleteRestaurantById(long id) {
        return restaurantDao.deleteRestaurantById(id);
    }

    // Util


    @Override
    public List<String> availableStringTime(long restaurantId) {
        int minHour = 12;
        int maxHour = 23;
        int stepMinutes = 30;
        List<String> availableHours = new ArrayList<>();
        for (int i = minHour; i <= maxHour; i++) {
            for (int j=0; j<60; j+=stepMinutes) {
                availableHours.add(String.format("%02d", i) + ":" + String.format("%02d", j));
            }
        }
        return availableHours;
    }


    @Override
    @Transactional
    public List<Restaurant> getRestaurantsFilteredBy(int page, int amountOnPage, String name, List<Tags> tags,
            double minAvgPrice, double maxAvgPrice, Sorting sort, boolean desc, int lastDays) {
        return restaurantDao.getRestaurantsFilteredBy(page, amountOnPage,
        name,tags,minAvgPrice,maxAvgPrice,sort,desc,lastDays);
    }

    @Override
    @Transactional
    public int getRestaurantsFilteredByPageCount(int amountOnPage, String name, List<Tags> tags, double minAvgPrice,
            double maxAvgPrice) {
        return restaurantDao.getRestaurantsFilteredByPageCount(amountOnPage, name,
        tags, minAvgPrice, maxAvgPrice);
    }

}
