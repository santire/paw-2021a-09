package ar.edu.itba.paw.persistence;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.Sorting;
import ar.edu.itba.paw.model.Tags;
import ar.edu.itba.paw.model.User;

@Repository
public class RestaurantJpaDao implements RestaurantDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Deprecated
    public Restaurant registerRestaurant(String name, String address, String phoneNumber, float rating, long userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Restaurant registerRestaurant(String name, String address, String phoneNumber, List<Tags> tags, User owner) {
        final Restaurant restaurant = new Restaurant(name, address, phoneNumber, tags, owner);
        em.persist(restaurant);
        return restaurant;
    }

    @Override
    public boolean setImageByRestaurantId(Image image, long restaurantId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Optional<Restaurant> findById(long id) {
        // TODO Auto-generated method stub
        return Optional.ofNullable(em.find(Restaurant.class, id));
    }

    @Override
    public Optional<Restaurant> findByIdWithMenu(long id, int menuPage, int amountOnMenuPage) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int findByIdWithMenuPagesCount(int amountOnMenuPage, long id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Restaurant> getAllRestaurants(int page, int amountOnPage, String searchTerm) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getAllRestaurantPagesCount(int amountOnPage, String searchTerm) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Restaurant> getRestaurantsFromOwner(int page, int amountOnPage, long userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getRestaurantsFromOwnerPagesCount(int amountOnPage, long userId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Restaurant> getPopularRestaurants(int limit, int minValue) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Restaurant> getAllRestaurants(int page, int amountOnPage) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Restaurant> findByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateName(long id, String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateAddress(long id, String address) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updatePhoneNumber(long id, String phoneNumber) {
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateRating(long id, int rating) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean deleteRestaurantById(long id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deleteRestaurantByName(String name) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Optional<User> findRestaurantOwner(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Restaurant> getHotRestaurants(int lastDays) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addTag(long restaurantId, int tagId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeTag(long restaurantId, int tagId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Tags> tagsInRestaurant(long restaurantId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Restaurant> getRestaurantsWithTags(List<Tags> tags) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Restaurant> getRestaurantsFilteredBy(int page, int amountOnPage, String name, List<Tags> tags,
            double minAvgPrice, double maxAvgPrice, Sorting sort, boolean desc, int lastDays) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getRestaurantsFilteredByPageCount(int amountOnPage, String name, List<Tags> tags, double minAvgPrice,
            double maxAvgPrice) {
        // TODO Auto-generated method stub
        return 0;
    }
}
