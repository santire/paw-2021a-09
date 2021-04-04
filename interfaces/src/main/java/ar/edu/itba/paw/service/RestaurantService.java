package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    public Optional<Restaurant> findById(long id);

    public List<Restaurant> findByName(String name);

    public Restaurant registerRestaurant(String name, String address, String phoneNumber, float rating, long userId);

    public List<Restaurant> getAllRestaurants();

    public List<Restaurant> getAllRestaurants(String searchTerm);

    public List<Restaurant> getPopularRestaurants();

    public boolean deleteRestaurantById(long id);

    public boolean deleteRestaurantByName(String name);

    public Optional<User> findRestaurantOwner(long id);
}