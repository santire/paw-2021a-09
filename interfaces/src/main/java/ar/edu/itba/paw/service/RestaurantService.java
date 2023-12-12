package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {

    // CREATE
    Restaurant registerRestaurant(String name, String address, String phoneNumber, List<Tags> tags, Long ownerId, String facebook, String twitter, String instagram);
    void setImageByRestaurantId(Image image, long restaurantId);
    // READ
    Optional<Restaurant> findById(long id);
    List<Restaurant> getPopularRestaurants(int limit, int minValue);
    List<Restaurant> getRestaurantsFilteredBy(int page, int amountOnPage, String name, List<Tags> tags, Long ownedBy, double minAvgPrice, double maxAvgPrice, Sorting sort, boolean desc, int lastDays);
    int getRestaurantsFilteredByCount(String name, List<Tags> tags, Long ownedBy, double minAvgPrice, double maxAvgPrice, int lastDays);
    List<Restaurant> getHotRestaurants(int limit, int lastDays);

    // UPDATE
    Restaurant updateRestaurant(long id, String name, String address, String phoneNumber, List<Tags> tags, String facebook, String twitter, String instagram);

    // DESTROY
    void deleteRestaurantById(long id);

}
