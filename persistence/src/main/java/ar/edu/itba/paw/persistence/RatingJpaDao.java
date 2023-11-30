package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.utils.JpaUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RatingJpaDao implements RatingDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Rating> getRating(long userId, long restaurantId) {
        TypedQuery<Rating> query = em.createQuery("from Rating r WHERE  r.user.id = :userId AND r.restaurant.id = :restaurantId", Rating.class);

        query.setParameter("userId", userId);
        query.setParameter("restaurantId", restaurantId);

        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Rating> getUserRatings(int page, int amountOnPage, long userId) {
        Query nativeQuery = findRatings(userId);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);

        return collectRatings(page, amountOnPage, nativeQuery);
    }

    @Override
    public int getUserRatingsCount(long userId) {
        return findRatings(userId).getResultList().size();
    }

    @Override
    public List<Rating> getUserRatingsByRestaurant(long userId, List<Long> restaurantIds) {
        if (restaurantIds.isEmpty()) {
            return Collections.emptyList();
        }

        TypedQuery<Rating> query = em.createQuery("from Rating where user.id = :userId and restaurant.id in :restaurantIds", Rating.class);
        query.setParameter("userId", userId);
        query.setParameter("restaurantIds", restaurantIds);

        return query.getResultList();
    }

    @Override
    public Rating createRating(User user, Restaurant restaurant, double rating) {
        Rating ratingObj = new Rating(rating);
        ratingObj.setUser(user);
        ratingObj.setRestaurant(restaurant);
        em.persist(ratingObj);
        return ratingObj;
    }

    @Override
    public void deleteRating(long ratingId) {
        Query query = em.createNativeQuery("DELETE FROM ratings WHERE rating_id = ?1");
        query.setParameter(1, ratingId);
        query.executeUpdate();
    }

    private Query findRatings(Long userId) {
        Query nativeQuery = em.createNativeQuery("SELECT rating_id FROM ratings WHERE user_id = :userId ORDER BY rating_id DESC");
        nativeQuery.setParameter("userId", userId);
        return nativeQuery;
    }

    private List<Rating> collectRatings(int page, int amountOnPage, Query query) {
        List<Long> filteredIds = JpaUtils.getFilteredIds(page, amountOnPage, query);
        final TypedQuery<Rating> typedQuery = em.createQuery("from Rating where id IN :filteredIds", Rating.class);
        typedQuery.setParameter("filteredIds", filteredIds);

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }
        return typedQuery.getResultList().stream().sorted(Comparator.comparing(v -> filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }

}
