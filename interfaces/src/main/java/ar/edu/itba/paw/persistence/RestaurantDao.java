package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;

import java.util.List;
import java.util.Optional;

public interface RestaurantDao {

    // CREATE

     Restaurant registerRestaurant(String name, String address, String phoneNumber, List<Tags> tags, User owner, String facebook, String twitter, String instagram);
     boolean setImageByRestaurantId(Image image, long restaurantId);

    // READ
    Optional<Restaurant> findById(long id);
    Optional<Restaurant> findByIdWithMenu(int page, int amountOnPage, long id);
    int findByIdWithMenuCount( long id);
    List<Restaurant> getRestaurantsFilteredBy(int page, int amountOnPage, String name, List<Tags> tags,Long ownedBy, double minAvgPrice, double maxAvgPrice, Sorting sort, boolean desc, int lastDays);
    int getRestaurantsFilteredByCount(String name, List<Tags> tags,Long ownedBy, double minAvgPrice, double maxAvgPrice, int lastDays);
    List<Restaurant> getPopularRestaurants(int limit, int minValue);
    List<Restaurant> getHotRestaurants(int limit, int lastDays);
    List<Restaurant> getRestaurantsFromOwner(int page, int amountOnPage, long userId);
    int getRestaurantsFromOwnerCount(long userId);





    // DESTROY
    boolean deleteRestaurantById(long id);

}
