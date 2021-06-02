package ar.edu.itba.paw.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

@Repository
public class LikeJpaDao implements LikesDao {

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

}
