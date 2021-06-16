package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {

    // CREATE
    public Restaurant registerRestaurant(String name, String address, String phoneNumber, List<Tags> tags, User owner);
    public boolean setImageByRestaurantId(Image image, long restaurantId);

    // READ
    public Optional<Restaurant> findById(long id);

    public Optional<Restaurant> findByIdWithMenu(long id, int menuPage, int amountOnMenuPage);
    public int findByIdWithMenuPagesCount(int amountOnMenuPage, long id);

    public List<Restaurant> getAllRestaurants(int page, int amountOnPage, String searchTerm);
    public int getAllRestaurantPagesCount(int amountOnPage, String searchTerm);

    public List<Restaurant> getRestaurantsFromOwner(int page, int amountOnPage, long userId);
    public int getRestaurantsFromOwnerPagesCount(int amountOnPage, long userId);

    public List<Restaurant> getPopularRestaurants(int limit, int minValue);
    public List<Restaurant> getLikedRestaurantsPreview(int limit, long userId);


    public List<Restaurant> getRestaurantsFilteredBy(int page, int amountOnPage, String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice, Sorting sort, boolean desc, int lastDays);
    public int getRestaurantsFilteredByPageCount(int amountOnPage, String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice);

    public List<Restaurant> getHotRestaurants(int limit, int lastDays);


    // UPDATE
    public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber, List<Tags> tags);



    // DESTROY
    public boolean deleteRestaurantById(long id);






    public List<String> availableStringTime(long restaurantId);
}
