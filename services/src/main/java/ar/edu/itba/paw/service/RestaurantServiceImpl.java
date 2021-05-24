package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.LikesDao;
import ar.edu.itba.paw.persistence.RestaurantDao;
import ar.edu.itba.paw.persistence.TagDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// @Service
// public class RestaurantServiceImpl implements RestaurantService{
    // @Autowired
    // private RestaurantDao restaurantDao;

    // @Autowired
    // private LikesDao likeDao;

    // @Autowired
    // private TagDao tagDao;

    // CREATE

    // @Override
    // public Restaurant registerRestaurant(String name, String address, String phoneNumber,
                                         // float rating, long userId){

        // return restaurantDao.registerRestaurant(name, address, phoneNumber, rating, userId);
    // }

    // @Override
    // public boolean setImageByRestaurantId(Image image, long restaurantId) {
        // return restaurantDao.setImageByRestaurantId(image, restaurantId);
    // }

    // READ

    // @Override
    // public Optional<Restaurant> findById(long id) {
        // return restaurantDao.findById(id);
    // }

    // @Override
    // public List<Restaurant> getAllRestaurants(int page, int amountOnPage){
        // return restaurantDao.getAllRestaurants(page, amountOnPage);
    // }

    // @Override
    // public List<Restaurant> getPopularRestaurants(int limit, int minValue){
        // return restaurantDao.getPopularRestaurants(limit,  minValue);
    // }

    // @Override
    // public Optional<Restaurant> findByIdWithMenu(long id, int menuPage, int amountOnMenuPage){
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
    // }
    
    // @Override
    // public int findByIdWithMenuPagesCount(int amountOnMenuPage, long id) {
        // return restaurantDao.findByIdWithMenuPagesCount(amountOnMenuPage, id);
    // }

    // @Override
    // public List<Restaurant> getRestaurantsFromOwner(int page, int amountOnPage, long userId) {
        // return restaurantDao.getRestaurantsFromOwner(page, amountOnPage, userId);
    // }

    // @Override
    // public int getRestaurantsFromOwnerPagesCount(int amountOnPage, long userId) {
        // return restaurantDao.getRestaurantsFromOwnerPagesCount(amountOnPage, userId);
    // }


    // @Override
    // public List<Restaurant> getAllRestaurants(int page, int amountOnPage, String searchTerm){
        // return restaurantDao.getAllRestaurants(page, amountOnPage, searchTerm);
    // }

    // @Override
    // public int getAllRestaurantPagesCount(int amountOnPage, String searchTerm) {
        // return restaurantDao.getAllRestaurantPagesCount(amountOnPage, searchTerm);
    // }

    // @Override
    // public List<Restaurant> getHotRestaurants(int lastDays) {
        // return this.restaurantDao.getHotRestaurants(lastDays);
    // }

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

    // @Override
    // public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber) {
       // return restaurantDao.updateRestaurant(id, name, address ,phoneNumber);
    // }


    // DESTROY

    // @Override
    // public boolean deleteRestaurantById(long id){
        // return restaurantDao.deleteRestaurantById(id);
    // }

    // @Override
    // public boolean deleteRestaurantByName(String name){
        // return restaurantDao.deleteRestaurantByName(name);
    // }

    // ??

    // @Override
    // public Optional<User> findRestaurantOwner(long id) {
        // return restaurantDao.findRestaurantOwner(id);
    // }


    // @Override
    // public List<LocalTime> availableTime(long restaurantId){
        // LocalTime time;
        // LocalTime currentTime = LocalTime.now();
        // int min = 12;
        // int max = 23;
        // List<LocalTime> availableHours = new ArrayList<>();
        // String str;
        // for(int i = min; i <= max; i++){
            // str = String.valueOf(i) + ':' + "00";
            // time = LocalTime.parse(str);
            // if(time.isAfter(currentTime)){
                // availableHours.add(time);
            // }
            // str = String.valueOf(i) + ':' + "30";
            // time = LocalTime.parse(str);
            // if(time.isAfter(currentTime)){
                // availableHours.add(time);
            // }
        // }
        // return availableHours;
    // }

    // @Override
    // public List<String> availableStringTime(long restaurantId){
        // LocalTime time;
        // int min = 12;
        // int max = 23;
        // List<String> afterLocalTime = new ArrayList<>();
        // LocalTime localTime = LocalTime.now();
        // String str;
        // for(int i = min; i <= max; i++){
            // str = String.valueOf(i) + ':' + "00";
            // time = LocalTime.parse(str);
            // if(time.isAfter(localTime)){
                // afterLocalTime.add(str);
            // }
            // str = String.valueOf(i) + ':' + "30";
            // time = LocalTime.parse(str);
            // if(time.isAfter(localTime)){
                // afterLocalTime.add(str);
            // }
        // }
        // return afterLocalTime;
    // }

    // @Override
    // public List<Restaurant> findByName(String name){
        // return restaurantDao.findByName(name);
    // }

    // @Override
    // public boolean addTag(long restaurantId, Tags tag) {

        // List<Tags> tags = restaurantDao.tagsInRestaurant(restaurantId);

        // if(tags.size() >= 3 || tags.contains(tag))
            // return false;


        // restaurantDao.addTag(restaurantId,tag.getValue());

        // return true;
    // }

    // @Override
    // public boolean removeTag(long restaurantId, Tags tag) {


        // return restaurantDao.removeTag(restaurantId,tag.getValue());
    // }

    // @Override
    // public List<Tags> tagsInRestaurant(long restaurantId) {
        // return restaurantDao.tagsInRestaurant(restaurantId);
    // }

    // @Override
    // public List<Restaurant> getRestaurantsWithTags(List<Tags> tags) {
        // return restaurantDao.getRestaurantsWithTags(tags);
    // }

    // @Override
    // public List<Restaurant> getRestaurantsFilteredBy(int page, int amountOnPage, String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice, Sorting sort, boolean desc, int lastDays) {
        // return restaurantDao.getRestaurantsFilteredBy(page, amountOnPage, name,tags,minAvgPrice,maxAvgPrice,sort,desc,lastDays);
    // }
    // public int getRestaurantsFilteredByPageCount(int amountOnPage, String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice) {
        // return restaurantDao.getRestaurantsFilteredByPageCount(amountOnPage, name, tags, minAvgPrice, maxAvgPrice);
    // }



// }
