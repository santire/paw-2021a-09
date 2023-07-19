package ar.edu.itba.paw.persistence;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

@Repository
public class LikeJpaDao implements LikesDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(LikeJpaDao.class);
	@PersistenceContext
	private EntityManager em;

	@Override
	public boolean like(User user, Restaurant restaurant) {
		Like like = new Like(user, restaurant);
		em.persist(like);
		return true;
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
		if(restaurantIds.isEmpty()){
			return Collections.emptyList();
		}

		TypedQuery<Like> query = em.createQuery("from Like where user.id = :userId and restaurant.id in :restaurantIds", Like.class);
		query.setParameter("userId", userId);
		query.setParameter("restaurantIds", restaurantIds);

		return query.getResultList().stream().collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getLikesByUserId(long userId){
		Query query = em.createNativeQuery("SELECT restaurant_id FROM likes WHERE user_id = ?1");
		query.setParameter(1, userId);

		List<BigInteger> resultList = query.getResultList();
		List<Long> likes = new ArrayList<>();

		for (BigInteger result : resultList) {
			likes.add(result.longValue());
		}

		return likes;
	}

}
