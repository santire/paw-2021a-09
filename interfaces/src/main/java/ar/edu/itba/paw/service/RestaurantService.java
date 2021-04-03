package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    public Optional<Restaurant> findById(long id);
    public List<Restaurant> findByName(String name);
    public Restaurant registerRestaurant(String name, String address, String phoneNumber,
                                         float rating, long userId);
    public List<Restaurant> getAllRestaurants();
    public boolean deleteRestaurantById(long id);
    public boolean deleteRestaurantByName(String name);
}
