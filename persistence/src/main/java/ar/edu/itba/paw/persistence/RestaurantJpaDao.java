package ar.edu.itba.paw.persistence;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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

    // CREATE

    @Override
    public Restaurant registerRestaurant(String name, String address, String phoneNumber, List<Tags> tags, User owner) {
        final Restaurant restaurant = new Restaurant(name, address, phoneNumber, tags, owner);
        em.persist(restaurant);
        return restaurant;
    }

    @Override
    public boolean setImageByRestaurantId(Image image, long restaurantId) {
        final Optional<Restaurant> maybeRestaurant = findById(restaurantId);
        if (!maybeRestaurant.isPresent()) {
            return false;
        }
        Restaurant restaurant = maybeRestaurant.get();
        restaurant.setProfileImage(image);
        em.persist(restaurant);

        return true;
    }

    @Deprecated
    @Override
    public Restaurant registerRestaurant(String name, String address, String phoneNumber, float rating, long userId) {
        return null;
    }

    // READ

    @Override
    public Optional<Restaurant> findById(long id) {
        return Optional.ofNullable(em.find(Restaurant.class, id));
    }

    @Override
    @Deprecated
    public Optional<Restaurant> findByIdWithMenu(long id, int menuPage, int amountOnMenuPage) {
        return Optional.ofNullable(em.find(Restaurant.class, id));
    }

    @Override
    public int findByIdWithMenuPagesCount(int amountOnMenuPage, long id) {
        return 0;
    }

    @Override
    public List<Restaurant> getAllRestaurants(int page, int amountOnPage, String searchTerm) {
        Query nativeQuery = em.createNativeQuery("SELECT restaurant_id FROM restaurants WHERE lower(name) LIKE ?1");
                nativeQuery.setParameter(1, "%"+searchTerm.trim().toLowerCase()+"%");
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);
        @SuppressWarnings("unchecked")
        List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString()))
                .collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Restaurant> query = em.createQuery("from Restaurant where id IN :filteredIds",
                Restaurant.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList();
    }

    @Override
    public int getAllRestaurantPagesCount(int amountOnPage, String searchTerm) {
        Query nativeQuery = em.createNativeQuery("SELECT restaurant_id FROM restaurants WHERE lower(name) LIKE ?1");
                nativeQuery.setParameter(1, "%"+searchTerm.trim().toLowerCase()+"%");

        int amountOfRestaurants =  nativeQuery.getResultList().size();
        int pageAmount = (int) Math.ceil((double)amountOfRestaurants / amountOnPage);

        return pageAmount <= 0 ? 1 : pageAmount;
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
    @Deprecated
    public List<Restaurant> findByName(String name) {
        return null;
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

    @Override
    public List<Restaurant> getRestaurantsWithTags(List<Tags> tags) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Deprecated
    // Comes from the model
    public List<Tags> tagsInRestaurant(long restaurantId) {
        // TODO Auto-generated method stub
        return null;
    }

    // UPDATE
    // These should be done from the service by modifying the instance (i think)

    @Override
    @Deprecated
    public void updateName(long id, String name) {
        // TODO Auto-generated method stub

    }

    @Override
    @Deprecated
    public void updateAddress(long id, String address) {
        // TODO Auto-generated method stub

    }

    @Override
    @Deprecated
    public void updatePhoneNumber(long id, String phoneNumber) {
        // TODO Auto-generated method stub

    }

    @Override
    @Deprecated
    public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber) {

        return null;
    }

    @Override
    @Deprecated
    public void updateRating(long id, int rating) {
        // TODO Auto-generated method stub

    }

    @Override
    @Deprecated
    public boolean addTag(long restaurantId, int tagId) {
        return false;
    }

    // DELETE

    @Override
    public boolean removeTag(long restaurantId, int tagId) {
        // TODO Auto-generated method stub
        return false;
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
}
