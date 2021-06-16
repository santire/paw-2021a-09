package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;

import java.util.List;
import java.util.Optional;

public interface RestaurantDao {

    // CREATE

    public Restaurant registerRestaurant(String name, String address, String phoneNumber, List<Tags> tags, User owner);
    public boolean setImageByRestaurantId(Image image, long restaurantId);

    // READ
    public Optional<Restaurant> findById(long id);
    public Optional<Restaurant> findByIdWithMenu(int page, int amountOnPage, long id);
    public int findByIdWithMenuPageCount(int amountOnPage, long id);

    public List<Restaurant> getAllRestaurants(int page, int amountOnPage, String searchTerm);
    public int getAllRestaurantPagesCount(int amountOnPage, String searchTerm);

    public List<Restaurant> getAllLikedRestaurants(int page, int amountOnPage);
    public List<Restaurant> getAllLikedRestaurantsPagesCount(int amountOnPage);

    public List<Restaurant> getRestaurantsFilteredBy(int page, int amountOnPage, String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice, Sorting sort, boolean desc, int lastDays);
    public int getRestaurantsFilteredByPageCount(int amountOnPage, String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice);

    public List<Restaurant> getPopularRestaurants(int limit, int minValue);
    public List<Restaurant> getHotRestaurants(int limit, int lastDays);
    public List<Restaurant> getLikedRestaurantsPreview(int limit, long userId);

    public List<Restaurant> getRestaurantsFromOwner(int page, int amountOnPage, long userId);
    public int getRestaurantsFromOwnerPagesCount(int amountOnPage, long userId);


    public boolean menuBelongsToRestaurant(long restaurantId, long menuId);


    // UPDATE
    public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber);
    public void updateRating(long id, int rating);


    // DESTROY
    public boolean deleteRestaurantById(long id);
    public boolean addTag(long restaurantId, int tagId);

}
