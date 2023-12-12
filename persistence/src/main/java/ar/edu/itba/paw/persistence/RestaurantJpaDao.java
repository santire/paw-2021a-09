package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.persistence.utils.JpaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RestaurantJpaDao implements RestaurantDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantJpaDao.class);

    @PersistenceContext
    private EntityManager em;

    // CREATE

    @Override
    public Restaurant registerRestaurant(String name, String address, String phoneNumber, List<Tags> tags, User owner, String facebook, String twitter, String instagram) {
        final Restaurant restaurant = new Restaurant(name, address, phoneNumber, tags, owner, facebook, twitter, instagram);
        em.persist(restaurant);
        return restaurant;
    }

    @Override
    public void setImageByRestaurantId(Image image, long restaurantId) {
        final Restaurant restaurant = findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        image.setRestaurant(restaurant);
        if (restaurant.getProfileImage() != null) {
            LOGGER.debug("Merging image");
            Image img = restaurant.getProfileImage();
            img.setData(image.getData());
            img.increaseVersion();
            em.merge(img);
        } else {
            LOGGER.debug("Creating Image of size {}", image.getData().length);
            image.setRestaurant(restaurant);
            restaurant.setProfileImage(image);
            LOGGER.debug("Set profile image: {}", restaurant.getProfileImage() != null);
            em.persist(image);
            em.persist(restaurant);
            LOGGER.debug("Set profile image: {}", restaurant.getProfileImage() != null);
        }

    }

    // READ

    @Override
    public Optional<Restaurant> findById(long id) {
        return Optional.ofNullable(em.find(Restaurant.class, id));
    }

    @Override
    public List<Restaurant> getHotRestaurants(int limit, int lastDays) {
        Query nativeQuery = em.createNativeQuery(
                "SELECT restaurant_id FROM reservations"
                        +
                        " WHERE date > 'now'\\:\\:timestamp - '" + lastDays + " day'\\:\\:interval"
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
        LOGGER.debug("list: {}", filteredIds);

        final TypedQuery<Restaurant> query = em.createQuery("from Restaurant where id IN :filteredIds",
                Restaurant.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList().stream().sorted(Comparator.comparing(v -> filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
//        return query.getResultList();
    }

    public int getRestaurantsFromOwnerCount(long userId) {
        Query nativeQuery = em.createNativeQuery("SELECT restaurant_id FROM restaurants WHERE user_id = :userId");
        nativeQuery.setParameter("userId", userId);

        return nativeQuery.getResultList().size();
    }

    @Override
    public List<Restaurant> getRestaurantsFilteredBy(int page, int amountOnPage, String name, List<Tags> tags, Long userId,
                                                     double minAvgPrice, double maxAvgPrice, Sorting sort, boolean desc, int lastDays) {
       Query nativeQuery = findFilteredRestaurantsQuery(name, tags, userId, minAvgPrice, maxAvgPrice, sort, desc, lastDays);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);
        return collectRestaurants(page, amountOnPage, nativeQuery);
    }

    @Override
    public int getRestaurantsFilteredByCount(String name, List<Tags> tags, Long userId, double minAvgPrice,
                                             double maxAvgPrice, int lastDays) {

        return findFilteredRestaurantsQuery(name, tags, userId, minAvgPrice, maxAvgPrice, Sorting.NAME, true, lastDays).getResultList().size();
    }
    @Override
    public void deleteRestaurantById(long id) {
        Restaurant restaurant = findById(id).orElseThrow(RestaurantNotFoundException::new);
        if (restaurant.getProfileImage() != null) {
            em.remove(restaurant.getProfileImage());
        }
        em.remove(restaurant);
    }

    private Query findFilteredRestaurantsQuery(String name, List<Tags> tags, Long userId,
                                               double minAvgPrice, double maxAvgPrice,
                                               Sorting sort, boolean desc, int lastDays) {
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.now().minusDays(lastDays));
        String userPart = (userId != null) ? " AND user_id = :userId" : "";

        String TAG_CHECK_QUERY = " WHERE true";
        if (!tags.isEmpty()) {
            TAG_CHECK_QUERY += " AND tag_id IN (";
            for (int i = 0; i < tags.size(); i++) {
                TAG_CHECK_QUERY += tags.get(i).getValue();
                if (i != tags.size() - 1)
                    TAG_CHECK_QUERY += ", ";
            }
            TAG_CHECK_QUERY += ")";
        }
        String orderBy = sort.getSortType();
        String order = "DESC";

        if (!desc)
            order = "ASC";

        Query nativeQuery = em.createNativeQuery(
                "SELECT restaurant_id FROM ("
                        +
                        "SELECT r.restaurant_id, r.name, r.address, r.phone_number,"
                        +
                        " r.rating, r.user_id, "
                        +
                        " COALESCE(p, 0) as price, COALESCE(q,0) as hot, COALESCE(l, 0) as likes"
                        +
                        " FROM ("
                        +
                        " SELECT r1.* FROM restaurants r1 LEFT JOIN restaurant_tags rt"
                        +
                        " ON r1.restaurant_id = rt.restaurant_id"
                        +
                        TAG_CHECK_QUERY
                        +
                        userPart
                        +
                        ") AS r"
                        +
                        " LEFT JOIN ("
                        +
                        " SELECT restaurant_id, COUNT(reservation_id)"
                        +
                        " FROM reservations"
                        +
                        " WHERE date > :startDate"
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
                        " LEFT JOIN ("
                        +
                        " SELECT restaurant_id, AVG(price)"
                        +
                        " FROM menu_items"
                        +
                        " GROUP BY restaurant_id"
                        +
                        ") AS pr(rid, p)"
                        +
                        " ON r.restaurant_id=pr.rid"
                        +
                        " WHERE r.name ILIKE :searchTerm"
                        +
                        " AND COALESCE(p, 0) BETWEEN :low AND :high"
                        +
                        " GROUP BY r.restaurant_id, r.name, r.phone_number, r.rating,"
                        + " r.address, r.user_id, hot, likes, price"
                        +
                        " ORDER BY " + orderBy + " " + order
                        +
                        ") AS yay"
        );
        nativeQuery.setParameter("searchTerm", "%" + name.trim().toLowerCase() + "%");
        nativeQuery.setParameter("startDate", startDate);
        nativeQuery.setParameter("low", minAvgPrice);
        nativeQuery.setParameter("high", maxAvgPrice);
        if (userId != null) {
            nativeQuery.setParameter("userId", userId);
        }

        return nativeQuery;
    }

    private List<Restaurant> collectRestaurants(int page, int amountOnPage, Query query) {

        List<Long> filteredIds = JpaUtils.getFilteredIds(page, amountOnPage, query);

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }

        final TypedQuery<Restaurant> typedQuery = em.createQuery("from Restaurant where id IN :filteredIds",
                Restaurant.class);
        typedQuery.setParameter("filteredIds", filteredIds);
        return typedQuery.getResultList().stream().sorted(Comparator.comparing(v -> filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }

}
