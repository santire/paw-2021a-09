package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.utils.JpaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LikeJpaDao implements LikesDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(LikeJpaDao.class);
    @PersistenceContext
    private EntityManager em;

    @Override
    public Like like(User user, Restaurant restaurant) {
        Like like = new Like(user, restaurant);
        em.persist(like);
        return like;
    }

    @Override
    public boolean dislike(long userId, long restaurantId) {
        Query query = em.createNativeQuery("DELETE FROM likes WHERE user_id = ?1 AND restaurant_id= ?2");
        query.setParameter(1, userId);
        query.setParameter(2, restaurantId);
        query.executeUpdate();
        return true;
    }

    @Override
    public boolean userLikesRestaurant(long userId, long restaurantId) {
        Query query = em.createNativeQuery("SELECT like_id FROM likes WHERE user_id = ?1 AND restaurant_id= ?2");
        query.setParameter(1, userId);
        query.setParameter(2, restaurantId);

        return query.getResultList().stream().findFirst().isPresent();
    }

    @Override
    public List<Like> userLikesRestaurants(long userId, List<Long> restaurantIds) {
        if (restaurantIds.isEmpty()) {
            return Collections.emptyList();
        }

        TypedQuery<Like> query = em.createQuery("from Like where user.id = :userId and restaurant.id in :restaurantIds", Like.class);
        query.setParameter("userId", userId);
        query.setParameter("restaurantIds", restaurantIds);

        return query.getResultList();
    }

    @Override
    public List<Like> getLikesByUserId(int page, int amountOnPage, Long userId) {
        Query nativeQuery = findLikesQuery(userId);
        nativeQuery.setFirstResult((page - 1) * amountOnPage);
        nativeQuery.setMaxResults(amountOnPage);

        return collectLikes(page, amountOnPage, nativeQuery);
    }

    @Override
    public int getLikesByUserIdCount(Long userId) {
        return findLikesQuery(userId).getResultList().size();
    }

    private Query findLikesQuery(Long userId) {
        Query nativeQuery = em.createNativeQuery("SELECT like_id FROM likes WHERE user_id = :userId ORDER BY like_id DESC");
        nativeQuery.setParameter("userId", userId);
        return nativeQuery;
    }

    private List<Like> collectLikes(int page, int amountOnPage, Query query) {
        List<Long> filteredIds = JpaUtils.getFilteredIds(page, amountOnPage, query);
        final TypedQuery<Like> typedQuery = em.createQuery("from Like where id IN :filteredIds", Like.class);
        typedQuery.setParameter("filteredIds", filteredIds);

        if (filteredIds.isEmpty()) {
            return Collections.emptyList();
        }
        return typedQuery.getResultList().stream().sorted(Comparator.comparing(v -> filteredIds.indexOf(v.getId()))).collect(Collectors.toList());
    }
}
