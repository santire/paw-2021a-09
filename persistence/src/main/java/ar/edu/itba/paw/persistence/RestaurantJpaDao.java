package ar.edu.itba.paw.persistence;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.Sorting;
import ar.edu.itba.paw.model.Tags;
import ar.edu.itba.paw.model.User;

@Repository
public class RestaurantJpaDao implements RestaurantDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantJpaDao.class);

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

    // READ

    @Override
    public Optional<Restaurant> findById(long id) {
        return Optional.ofNullable(em.find(Restaurant.class, id));
    }

    @Override
    public Optional<Restaurant> findByIdWithMenu(int page, int amountOnPage, long id){
        Optional<Restaurant> maybeRestaurant = findById(id);
        if (!maybeRestaurant.isPresent()) {
            return maybeRestaurant;
        }
        Restaurant restaurant = maybeRestaurant.get();

        List<MenuItem> menuItems;
        Query nativeQuery = em.createNativeQuery(
                "SELECT menu_item_id"
                +
                " FROM restaurants r LEFT JOIN menu_items m"
                +
                " ON r.restaurant_id = m.restaurant_id"
                +
                " WHERE r.restaurant_id = :rid"
                +
                " ORDER BY menu_item_id ASC"
                );

        nativeQuery.setParameter("rid", id);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);

        @SuppressWarnings("unchecked")
        // List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString())).collect(Collectors.toList());
        List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().filter(e -> e!=null).map(e -> Long.valueOf(e.toString())).collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            menuItems = Collections.emptyList();
        } else {
            final TypedQuery<MenuItem> query = em.createQuery("from MenuItem where id IN :filteredIds",
                    MenuItem.class);
            query.setParameter("filteredIds", filteredIds);
            menuItems =  query.getResultList().stream().sorted(Comparator.comparing(v->filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
        }

        restaurant.setMenuPage(menuItems);
        return Optional.of(restaurant);
    }

    @Override
    public int findByIdWithMenuPageCount(int amountOnPage, long id){
        Query nativeQuery = em.createNativeQuery(
                "SELECT menu_item_id"
                +
                " FROM restaurants r LEFT JOIN menu_items m"
                +
                " ON r.restaurant_id = m.restaurant_id"
                +
                " WHERE r.restaurant_id = :rid"
                +
                " ORDER BY menu_item_id ASC"
                );

        nativeQuery.setParameter("rid", id);
        int amountOfRestaurants = nativeQuery.getResultList().size();
        int pageAmount = (int) Math.ceil((double) amountOfRestaurants / amountOnPage);

        return pageAmount <= 0 ? 1 : pageAmount;
    }

    @Override
    public List<Restaurant> getAllRestaurants(int page, int amountOnPage, String searchTerm) {
        Query nativeQuery = em.createNativeQuery("SELECT restaurant_id FROM restaurants WHERE lower(name) LIKE ?1");
        nativeQuery.setParameter(1, "%" + searchTerm.trim().toLowerCase() + "%");
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
        nativeQuery.setParameter(1, "%" + searchTerm.trim().toLowerCase() + "%");

        int amountOfRestaurants = nativeQuery.getResultList().size();
        int pageAmount = (int) Math.ceil((double) amountOfRestaurants / amountOnPage);

        return pageAmount <= 0 ? 1 : pageAmount;
    }

    @Override
    public List<Restaurant> getHotRestaurants(int limit, int lastDays) {
        Query nativeQuery = em.createNativeQuery(
                "SELECT restaurant_id FROM reservations"
                +
                " WHERE date > 'now'\\:\\:timestamp - '"+lastDays+" day'\\:\\:interval"
                +
                " GROUP BY restaurant_id"
                +
                " ORDER BY COUNT(reservation_id) DESC"
                );
        nativeQuery.setMaxResults(limit);

        @SuppressWarnings("unchecked")
        List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString()))
                .collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }
        LOGGER.debug("list: {}", filteredIds.toString());

        final TypedQuery<Restaurant> query = em.createQuery("from Restaurant where id IN :filteredIds",
                Restaurant.class);
        query.setParameter("filteredIds", filteredIds);

        return query.getResultList().stream().sorted((r1, r2) -> r2.getLikes() - r1.getLikes()).collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> getPopularRestaurants(int limit, int minValue) {
        Query nativeQuery = em.createNativeQuery(
                "SELECT rest.restaurant_id FROM ("
                    +
                    " SELECT r.restaurant_id, COUNT(like_id) as likes FROM restaurants r"
                    +
                    " RIGHT JOIN likes l ON r.restaurant_id = l.restaurant_id"
                    +
                    " GROUP BY r.restaurant_id"
                    +
                    ") AS rest"
                +
                " WHERE likes >= ?1"
                );
        nativeQuery.setMaxResults(limit);
        nativeQuery.setParameter(1, minValue);

        @SuppressWarnings("unchecked")
        List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString()))
                .collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }
        LOGGER.debug("list: {}", filteredIds.toString());

        final TypedQuery<Restaurant> query = em.createQuery("from Restaurant where id IN :filteredIds",
                Restaurant.class);
        query.setParameter("filteredIds", filteredIds);

        return query.getResultList().stream().sorted((r1, r2) -> r2.getLikes() - r1.getLikes()).collect(Collectors.toList());
    }

    public List<Restaurant> getRestaurantsFromOwner(int page, int amountOnPage, long userId) {
        Query nativeQuery = em.createNativeQuery("SELECT restaurant_id FROM restaurants WHERE user_id = :userId");
        nativeQuery.setParameter("userId", userId);
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

    public int getRestaurantsFromOwnerPagesCount(int amountOnPage, long userId) {
        Query nativeQuery = em.createNativeQuery("SELECT restaurant_id FROM restaurants WHERE user_id = :userId");
        nativeQuery.setParameter("userId", userId);

        int amountOfRestaurants = nativeQuery.getResultList().size();
        int pageAmount = (int) Math.ceil((double) amountOfRestaurants / amountOnPage);

        return pageAmount <= 0 ? 1 : pageAmount;
    }

    @Override
    public List<Restaurant> getRestaurantsFilteredBy(int page, int amountOnPage, String name, List<Tags> tags,
            double minAvgPrice, double maxAvgPrice, Sorting sort, boolean desc, int lastDays) {
        String TAG_CHECK_QUERY = " ";
        if(!tags.isEmpty()){
            TAG_CHECK_QUERY += " WHERE tag_id IN (";
            for(int i=0; i<tags.size(); i++){
                TAG_CHECK_QUERY += tags.get(i).getValue();
                if(i!=tags.size()-1)
                    TAG_CHECK_QUERY += ", ";
            }
            TAG_CHECK_QUERY+=")";
        }
        String orderBy=sort.getSortType();
        String order="DESC";

        if(!desc)
            order="ASC";

        Query nativeQuery = em.createNativeQuery(
                "SELECT restaurant_id FROM ("
                +
                    "SELECT r.restaurant_id, r.name, r.address, r.phone_number,"
                    +
                            " r.rating, r.user_id, "
                            +
                            " AVG(price) as price, COALESCE(q,0) as hot, COALESCE(l, 0) as likes"
                    +
                    " FROM ("
                        +
                        " SELECT r1.* FROM restaurants r1 LEFT JOIN restaurant_tags rt"
                        +
                        " ON r1.restaurant_id = rt.restaurant_id"
                        +
                        TAG_CHECK_QUERY
                        +
                        ") AS r"
                    +
                    " LEFT JOIN ("
                        +
                        " SELECT restaurant_id, COUNT(reservation_id)"
                        +
                        " FROM reservations"
                        +
                        " WHERE date > 'now'\\:\\:timestamp - '"+lastDays+" day'\\:\\:interval"
                        +
                        " GROUP BY restaurant_id"
                        +
                        ") AS hot(rid, q)"
                        +
                        " ON r.restaurant_id=hot.rid"
                    +
                    " LEFT JOIN ("
                        +
                        " SELECT restaurant_id, COUNT(like_id)"
                        +
                        " FROM likes"
                        +
                        " GROUP BY restaurant_id"
                        +
                        ") AS lik(rid, l)"
                        +
                        " ON r.restaurant_id=lik.rid"
                    +
                    " LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id"
                    +
                    " WHERE r.name ILIKE :searchTerm"
                    +
                    " AND price BETWEEN :low AND :high"
                    +
                    " GROUP BY r.restaurant_id, r.name, r.phone_number, r.rating,"
                            + " r.address, r.user_id,  hot, likes"
                    +
                    " ORDER BY " + orderBy+ " " + order
                +
                ") AS yay"
                );
        nativeQuery.setParameter("searchTerm", "%" + name.trim().toLowerCase() + "%");
        nativeQuery.setParameter("low", minAvgPrice);
        nativeQuery.setParameter("high", maxAvgPrice);
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
        return query.getResultList().stream().sorted(Comparator.comparing(v->filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }

    @Override
    public int getRestaurantsFilteredByPageCount(int amountOnPage, String name, List<Tags> tags, double minAvgPrice,
            double maxAvgPrice) {
        String TAG_CHECK_QUERY = " ";
        if(!tags.isEmpty()){
            TAG_CHECK_QUERY += " WHERE tag_id IN (";
            for(int i=0; i<tags.size(); i++){
                TAG_CHECK_QUERY += tags.get(i).getValue();
                if(i!=tags.size()-1)
                    TAG_CHECK_QUERY += ", ";
            }
            TAG_CHECK_QUERY+=")";
        }
        Query nativeQuery = em.createNativeQuery(
                "SELECT restaurant_id FROM ("
                +
                    "SELECT r.restaurant_id, AVG(price) as price"
                    +
                    " FROM ("
                        +
                        " SELECT r1.* FROM restaurants r1 LEFT JOIN restaurant_tags rt"
                        +
                        " ON r1.restaurant_id = rt.restaurant_id"
                        +
                        TAG_CHECK_QUERY
                        +
                        ") AS r"
                    +
                    " LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id"
                    +
                    " WHERE r.name ILIKE :searchTerm"
                    +
                    " AND price BETWEEN :low AND :high"
                    +
                    " GROUP BY r.restaurant_id"
                    +
                ") AS yay"
                );
        nativeQuery.setParameter("searchTerm", "%" + name.trim().toLowerCase() + "%");
        nativeQuery.setParameter("low", minAvgPrice);
        nativeQuery.setParameter("high", maxAvgPrice);
        int amountOfRestaurants = nativeQuery.getResultList().size();
        int pageAmount = (int) Math.ceil((double) amountOfRestaurants / amountOnPage);

        return pageAmount <= 0 ? 1 : pageAmount;
    }



    @Override
    public List<Restaurant> getLikedRestaurantsPreview(int limit, long userId) {
        Query nativeQuery = em.createNativeQuery(
                " SELECT r.restaurant_id FROM restaurants r"
                +
                " RIGHT JOIN likes l ON r.restaurant_id = l.restaurant_id"
                +
                " WHERE l.user_id = ?1"
                +
                " ORDER BY l.like_id DESC"
                );
        nativeQuery.setParameter(1, userId);
        nativeQuery.setMaxResults(limit);

        @SuppressWarnings("unchecked")
        List<Long> filteredIds = (List<Long>) nativeQuery.getResultList().stream().map(e -> Long.valueOf(e.toString()))
                .collect(Collectors.toList());

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }
        LOGGER.debug("list: {}", filteredIds.toString());

        final TypedQuery<Restaurant> query = em.createQuery("from Restaurant where id IN :filteredIds",
                Restaurant.class);
        query.setParameter("filteredIds", filteredIds);

        return query.getResultList().stream().sorted(Comparator.comparing(v->filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }

    // UPDATE


    @Override
    public boolean menuBelongsToRestaurant(long restaurantId, long menuId) {
        Query nativeQuery = em.createNativeQuery("SELECT menu_item_id FROM restaurants r LEFT JOIN menu_items m ON r.restaurant_id = m.restaurant_id WHERE r.restaurant_id = :rid AND m.menu_item_id = :mid");
            nativeQuery.setParameter("rid", restaurantId);
            nativeQuery.setParameter("mid", menuId);

            return !nativeQuery.getResultList().isEmpty();
    }

    // DELETE
    @Override
    public boolean deleteRestaurantById(long id) {
        Optional<Restaurant> maybeRestaurant = findById(id);
        if (maybeRestaurant.isPresent()) {
            em.remove(maybeRestaurant.get());
            return true;
        }
        return false;
    }

}
