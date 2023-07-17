package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {

    // CREATE
    Restaurant registerRestaurant(String name, String address, String phoneNumber, List<Tags> tags, User owner, String facebook, String twitter, String instagram);
    boolean setImageByRestaurantId(Image image, long restaurantId);

    // READ
    Optional<Restaurant> findById(long id);
    Optional<Restaurant> findByIdWithMenu(long id, int menuPage, int amountOnMenuPage);
    int findByIdWithMenuCount(int amountOnMenuPage, long id);
    List<Restaurant> getRestaurantsFromOwner(int page, int amountOnPage, long userId);
    int getRestaurantsFromOwnerCount(long userId);
    List<Restaurant> getPopularRestaurants(int limit, int minValue);
    List<Restaurant> getRestaurantsFilteredBy(int page, int amountOnPage, String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice, Sorting sort, boolean desc, int lastDays);
    int getRestaurantsFilteredByCount(String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice);
    List<Restaurant> getHotRestaurants(int limit, int lastDays);

    // UPDATE
    Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber, List<Tags> tags, String facebook, String twitter, String instagram);

    // DESTROY
    boolean deleteRestaurantById(long id);

}
