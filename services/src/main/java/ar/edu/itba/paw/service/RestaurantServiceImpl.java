package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.RestaurantDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService{
    @Autowired
    private RestaurantDao restaurantDao;

    // CREATE

    @Override
    public Restaurant registerRestaurant(String name, String address, String phoneNumber,
                                         float rating, long userId){

        return restaurantDao.registerRestaurant(name, address, phoneNumber, rating, userId);
    }

    @Override
    public boolean setImageByRestaurantId(Image image, long restaurantId) {
        return restaurantDao.setImageByRestaurantId(image, restaurantId);
    }

    // READ

    @Override
    public Optional<Restaurant> findById(long id) {
        return restaurantDao.findById(id);
    }

    @Override
    public List<Restaurant> getAllRestaurants(int page, int amountOnPage){
        return restaurantDao.getAllRestaurants(page, amountOnPage);
    }

    @Override
    public List<Restaurant> getPopularRestaurants(int limit, int minValue){
        return restaurantDao.getPopularRestaurants(limit,  minValue);
    }

    @Override
    public Optional<Restaurant> findByIdWithMenu(long id, int menuPage, int amountOnMenuPage){
        return restaurantDao.findByIdWithMenu(id, menuPage, amountOnMenuPage);
    }
    
    @Override
    public int findByIdWithMenuPagesCount(int amountOnMenuPage, long id) {
        return restaurantDao.findByIdWithMenuPagesCount(amountOnMenuPage, id);
    }

    @Override
    public List<Restaurant> getRestaurantsFromOwner(int page, int amountOnPage, long userId) {
        return restaurantDao.getRestaurantsFromOwner(page, amountOnPage, userId);
    }

    @Override
    public int getRestaurantsFromOwnerPagesCount(int amountOnPage, long userId) {
        return restaurantDao.getRestaurantsFromOwnerPagesCount(amountOnPage, userId);
    }


    @Override
    public List<Restaurant> getAllRestaurants(int page, int amountOnPage, String searchTerm){
        return restaurantDao.getAllRestaurants(page, amountOnPage, searchTerm);
    }

    @Override
    public int getAllRestaurantPagesCount(int amountOnPage, String searchTerm) {
        return restaurantDao.getAllRestaurantPagesCount(amountOnPage, searchTerm);
    }

    // UPDATE
    @Override
    public void updateName(long id, String name) {
        restaurantDao.updateName(id, name);
    }

    @Override
    public void updateAddress(long id, String address) {
        restaurantDao.updateAddress(id, address);
    }

    @Override
    public void updatePhoneNumber(long id, String phoneNumber) {
        restaurantDao.updatePhoneNumber(id, phoneNumber);
    }

    @Override
    public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber) {
       return restaurantDao.updateRestaurant(id, name, address ,phoneNumber);
    }


    // DESTROY

    @Override
    public boolean deleteRestaurantById(long id){
        return restaurantDao.deleteRestaurantById(id);
    }

    @Override
    public boolean deleteRestaurantByName(String name){
        return restaurantDao.deleteRestaurantByName(name);
    }

    // ??

    @Override
    public Optional<User> findRestaurantOwner(long id) {
        return restaurantDao.findRestaurantOwner(id);
    }

    // For now, returns default available hours.
    @Override
    public List<LocalTime> availableTime(long restaurantId){
        LocalTime time;
        List<String> times = Arrays.asList("19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30");
        List<LocalTime> availableHours = new ArrayList<>();
        for(String str : times){
            time = LocalTime.parse(str);
            availableHours.add(time);
        }
        return availableHours;
    }

    // For now, returns default available hours.
    @Override
    public List<String> availableStringTime(long restaurantId){
        List<String> times = Arrays.asList("19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30");
        return times;
    }

    @Override
    public List<Restaurant> findByName(String name){
        return restaurantDao.findByName(name);
    }

}
