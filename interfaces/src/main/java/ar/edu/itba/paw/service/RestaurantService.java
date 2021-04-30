package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {

    // CREATE
    public Restaurant registerRestaurant(String name, String address, String phoneNumber, float rating, long userId);
    public boolean setImageByRestaurantId(Image image, long restaurantId);

    // READ
    public Optional<Restaurant> findById(long id);
    public List<Restaurant> findByName(String name);
    public List<Restaurant> getAllRestaurants(int page, int amountOnPage);
    public List<Restaurant> getAllRestaurants(int page, int amountOnPage, String searchTerm);
    public List<Restaurant> getPopularRestaurants(int limit, int minValue);
    public int getAllRestaurantPagesCount(int amountOnPage, String searchTerm);

    // UPDATE
    public void updateName(long id, String name);
    public void updateAddress(long id, String address);
    public void updatePhoneNumber(long id, String phoneNumber);
    public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber);

    // DESTROY
    public boolean deleteRestaurantById(long id);
    public boolean deleteRestaurantByName(String name);

    public Optional<User> findRestaurantOwner(long id);
    public List<Restaurant> getRestaurantsFromOwner(long userId);
}
