package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.persistence.LikesDao;
import ar.edu.itba.paw.persistence.RestaurantDao;
import ar.edu.itba.paw.persistence.TagDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    @Autowired
    private RestaurantDao restaurantDao;

    // @Autowired
    // private LikesDao likeDao;

    // @Autowired
    // private TagDao tagDao;

    // CREATE

    @Override
    @Transactional
    public Restaurant registerRestaurant(String name, String address, String phoneNumber, List<Tags> tags, User owner) {
        return restaurantDao.registerRestaurant(name, address, phoneNumber, tags, owner);
    }

    @Override
    @Transactional
    // TODO: move to object instead of id?
    public boolean setImageByRestaurantId(Image image, long restaurantId) {
        Restaurant restaurant = findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        image.setRestaurant(restaurant);
        restaurant.setProfileImage(image);
        // return restaurantDao.setImageByRestaurantId(image, restaurantId);
        return true;
    }

    // READ

    @Override
    @Transactional
    public Optional<Restaurant> findById(long id) {
        return restaurantDao.findById(id);
    }

    @Override
    @Deprecated
    @Transactional
    public List<Restaurant> getAllRestaurants(int page, int amountOnPage) {
        return null;
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
    // TODO: Implement
    public Optional<Restaurant> findByIdWithMenu(long id, int menuPage, int amountOnMenuPage) {
        // Optional<Restaurant> maybeRestaurant = restaurantDao.findByIdWithMenu(id,
        // menuPage, amountOnMenuPage);
        // if (maybeRestaurant.isPresent()) {
        // Restaurant restaurant = maybeRestaurant.get();
        // int likes = likeDao.getLikesByRestaurantId(restaurant.getId());
        // List<Tags> tags = tagDao.getTagsByRestaurantId(restaurant.getId());
        // restaurant.setLikes(likes);
        // restaurant.setTags(tags);
        // return Optional.of(restaurant);
        // }
        // return Optional.empty();
        return findById(id);
    }

    @Override
    @Transactional
    // TODO: Implement
    public int findByIdWithMenuPagesCount(int amountOnMenuPage, long id) {
        // return restaurantDao.findByIdWithMenuPagesCount(amountOnMenuPage, id);
        return 1;
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
        return this.restaurantDao.getHotRestaurants(limit, lastDays);
        // return Collections.emptyList();
    }

    // UPDATE

    @Override
    @Transactional
    // TODO: No longer optional
    public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber,
            List<Tags> tags) {
        Restaurant restaurant = findById(id).orElseThrow(RestaurantNotFoundException::new);
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setPhoneNumber(phoneNumber);
        restaurant.setTags(tags);

        // return restaurantDao.updateRestaurant(id, name, address ,phoneNumber);
        return Optional.of(restaurant);
    }

    // DESTROY

    @Override
    @Transactional
    public boolean deleteRestaurantById(long id) {
        return restaurantDao.deleteRestaurantById(id);
    }

    @Override
    @Transactional
    @Deprecated
    public boolean deleteRestaurantByName(String name) {
        // return restaurantDao.deleteRestaurantByName(name);
        return true;
    }

    // ??

    @Override
    @Transactional
    @Deprecated
    public Optional<User> findRestaurantOwner(long id) {
        Restaurant restaurant = findById(id).orElseThrow(RestaurantNotFoundException::new);
        // return restaurantDao.findRestaurantOwner(id);
        return Optional.of(restaurant.getOwner());
    }

    @Override
    public List<LocalTime> availableTime(long restaurantId) {
        LocalTime time;
        LocalTime currentTime = LocalTime.now();
        int min = 12;
        int max = 23;
        List<LocalTime> availableHours = new ArrayList<>();
        String str;
        for (int i = min; i <= max; i++) {
            str = String.valueOf(i) + ':' + "00";
            time = LocalTime.parse(str);
            if (time.isAfter(currentTime)) {
                availableHours.add(time);
            }
            str = String.valueOf(i) + ':' + "30";
            time = LocalTime.parse(str);
            if (time.isAfter(currentTime)) {
                availableHours.add(time);
            }
        }
        return availableHours;
    }

    @Override
    public List<String> availableStringTime(long restaurantId) {
        LocalTime time;
        int min = 12;
        int max = 23;
        List<String> afterLocalTime = new ArrayList<>();
        LocalTime localTime = LocalTime.now();
        String str;
        for (int i = min; i <= max; i++) {
            str = String.valueOf(i) + ':' + "00";
            time = LocalTime.parse(str);
            if (time.isAfter(localTime)) {
                afterLocalTime.add(str);
            }
            str = String.valueOf(i) + ':' + "30";
            time = LocalTime.parse(str);
            if (time.isAfter(localTime)) {
                afterLocalTime.add(str);
            }
        }
        return afterLocalTime;
    }

    @Override
    @Deprecated
    public List<Restaurant> findByName(String name) {
        return Collections.emptyList();
        // return restaurantDao.findByName(name);
    }

    @Override
    @Transactional
    // TODO: Just use object
    public boolean addTag(long restaurantId, Tags tag) {

        Restaurant restaurant = findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        List<Tags> tags = restaurant.getTags();
        // List<Tags> tags = restaurantDao.tagsInRestaurant(restaurantId);

        if (tags.size() >= 3 || tags.contains(tag))
            return false;

        restaurantDao.addTag(restaurantId, tag.getValue());

        return true;
    }

    @Override
    @Transactional
    public boolean removeTag(long restaurantId, Tags tag) {

        Restaurant restaurant = findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        List<Tags> tags = restaurant.getTags();
        List<Tags> newTags = tags.stream().filter((t) -> t != tag).collect(Collectors.toList());
        restaurant.setTags(newTags);
        return true;

        // return restaurantDao.removeTag(restaurantId,tag.getValue());
    }

    @Override
    @Transactional
    @Deprecated
    public List<Tags> tagsInRestaurant(long restaurantId) {
        Restaurant restaurant = findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        return restaurant.getTags();
    }

    @Override
    @Transactional
    @Deprecated
    public List<Restaurant> getRestaurantsWithTags(List<Tags> tags) {
        // return restaurantDao.getRestaurantsWithTags(tags);
        return Collections.emptyList();
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
