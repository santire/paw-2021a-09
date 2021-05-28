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
public class RestaurantServiceImpl implements RestaurantService{
    @Autowired
    private RestaurantDao restaurantDao;

    // @Autowired
    // private LikesDao likeDao;

    // @Autowired
    // private TagDao tagDao;

    // CREATE

    @Override
    @Transactional
    public Restaurant registerRestaurant(String name, String address, String phoneNumber,
                                         List<Tags> tags, User owner){
        return restaurantDao.registerRestaurant(name, address, phoneNumber, tags, owner);
    }

    @Override
    public boolean setImageByRestaurantId(Image image, long restaurantId) {
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
    public List<Restaurant> getAllRestaurants(int page, int amountOnPage){
        return null;
    }

    @Override
    @Transactional
    // TODO: Implement
    public List<Restaurant> getPopularRestaurants(int limit, int minValue){
        // return restaurantDao.getPopularRestaurants(limit,  minValue);
        return Collections.emptyList();
    }

    @Override
    @Transactional
    // TODO: Implement
    public Optional<Restaurant> findByIdWithMenu(long id, int menuPage, int amountOnMenuPage){
        // Optional<Restaurant> maybeRestaurant = restaurantDao.findByIdWithMenu(id, menuPage, amountOnMenuPage);
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
    // TODO: Implement
    public List<Restaurant> getRestaurantsFromOwner(int page, int amountOnPage, long userId) {
        // return restaurantDao.getRestaurantsFromOwner(page, amountOnPage, userId);
        return Collections.emptyList();
    }

    @Override
    @Transactional
    // TODO: Implement
    public int getRestaurantsFromOwnerPagesCount(int amountOnPage, long userId) {
        // return restaurantDao.getRestaurantsFromOwnerPagesCount(amountOnPage, userId);
        return 1;
    }


    @Override
    @Transactional
    public List<Restaurant> getAllRestaurants(int page, int amountOnPage, String searchTerm){
        return restaurantDao.getAllRestaurants(page, amountOnPage, searchTerm);
    }

    @Override
    @Transactional
    public int getAllRestaurantPagesCount(int amountOnPage, String searchTerm) {
        return restaurantDao.getAllRestaurantPagesCount(amountOnPage, searchTerm);
    }

    @Override
    @Transactional
    // TODO: Implement
    public List<Restaurant> getHotRestaurants(int lastDays) {
        // return this.restaurantDao.getHotRestaurants(lastDays);
        return Collections.emptyList();
    }

    // UPDATE
    // @Override
    // public void updateName(long id, String name) {
        // restaurantDao.updateName(id, name);
    // }

    // @Override
    // public void updateAddress(long id, String address) {
        // restaurantDao.updateAddress(id, address);
    // }

    // @Override
    // public void updatePhoneNumber(long id, String phoneNumber) {
        // restaurantDao.updatePhoneNumber(id, phoneNumber);
    // }

    @Override
    @Transactional
    //TODO: No longer optional
    public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber) {
        Restaurant restaurant = findById(id).orElseThrow(RestaurantNotFoundException::new);
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setPhoneNumber(phoneNumber);

       // return restaurantDao.updateRestaurant(id, name, address ,phoneNumber);
        return Optional.of(restaurant);
    }


    // DESTROY

    @Override
    @Transactional
    public boolean deleteRestaurantById(long id){
        return restaurantDao.deleteRestaurantById(id);
    }

    @Override
    @Transactional
    @Deprecated
    public boolean deleteRestaurantByName(String name){
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
    public List<LocalTime> availableTime(long restaurantId){
        LocalTime time;
        LocalTime currentTime = LocalTime.now();
        int min = 12;
        int max = 23;
        List<LocalTime> availableHours = new ArrayList<>();
        String str;
        for(int i = min; i <= max; i++){
            str = String.valueOf(i) + ':' + "00";
            time = LocalTime.parse(str);
            if(time.isAfter(currentTime)){
                availableHours.add(time);
            }
            str = String.valueOf(i) + ':' + "30";
            time = LocalTime.parse(str);
            if(time.isAfter(currentTime)){
                availableHours.add(time);
            }
        }
        return availableHours;
    }

    @Override
    public List<String> availableStringTime(long restaurantId){
        LocalTime time;
        int min = 12;
        int max = 23;
        List<String> afterLocalTime = new ArrayList<>();
        LocalTime localTime = LocalTime.now();
        String str;
        for(int i = min; i <= max; i++){
            str = String.valueOf(i) + ':' + "00";
            time = LocalTime.parse(str);
            if(time.isAfter(localTime)){
                afterLocalTime.add(str);
            }
            str = String.valueOf(i) + ':' + "30";
            time = LocalTime.parse(str);
            if(time.isAfter(localTime)){
                afterLocalTime.add(str);
            }
        }
        return afterLocalTime;
    }

    @Override
    @Deprecated
    public List<Restaurant> findByName(String name){
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

        if(tags.size() >= 3 || tags.contains(tag))
            return false;


        restaurantDao.addTag(restaurantId,tag.getValue());

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
    // TODO: Implement
    public List<Restaurant> getRestaurantsFilteredBy(int page, int amountOnPage, String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice, Sorting sort, boolean desc, int lastDays) {
        // return restaurantDao.getRestaurantsFilteredBy(page, amountOnPage, name,tags,minAvgPrice,maxAvgPrice,sort,desc,lastDays);
        return Collections.emptyList();
    }

    @Override
    @Transactional
    // TODO: Implement
    public int getRestaurantsFilteredByPageCount(int amountOnPage, String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice) {
        // return restaurantDao.getRestaurantsFilteredByPageCount(amountOnPage, name, tags, minAvgPrice, maxAvgPrice);
        return 1;
    }



}
