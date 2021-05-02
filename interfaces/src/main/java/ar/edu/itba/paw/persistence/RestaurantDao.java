package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.Tags;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface RestaurantDao {
    
    // CREATE
    public Restaurant registerRestaurant(String name, String address, String phoneNumber, float rating, long userId);
    public boolean setImageByRestaurantId(Image image, long restaurantId);

    // READ
    public Optional<Restaurant> findById(long id);
    public List<Restaurant> findByName(String name);
    public List<Restaurant> getAllRestaurants();
    public List<Restaurant> getAllRestaurants(String searchTerm);
    public List<Restaurant> getPopularRestaurants();
    public List<Restaurant> getHotRestaurants(int lastDays);

    // UPDATE
    public void updateName(long id, String name);
    public void updateAddress(long id, String address);
    public void updatePhoneNumber(long id, String phoneNumber);
    public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber);
    public void updateRating(long id, int rating);


    // DESTROY
    public boolean deleteRestaurantById(long id);
    public boolean deleteRestaurantByName(String name);

    public Optional<User> findRestaurantOwner(long id);
    public List<Restaurant> getRestaurantsFromOwner(long userId);

    public boolean addTag(long restaurantId, int tagId);
    public boolean removeTag(long restaurantId, int tagId);
    public List<Tags> tagsInRestaurant(long restaurantId);

    public List<Restaurant> getRestaurantsWithTags(List<Tags> tags);

    public List<Restaurant> getRestaurantsFilteredBy(String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice);
    
}
