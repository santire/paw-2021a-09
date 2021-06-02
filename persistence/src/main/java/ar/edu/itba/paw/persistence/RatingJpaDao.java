package ar.edu.itba.paw.persistence;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;

@Repository
public class RatingJpaDao implements RatingDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<Rating> getRating(long userId, long restaurantId) {
		// Query query = em.createNativeQuery("SELECT rating_id FROM ratings WHERE user_id = ?1 AND restaurant_id= ?2");
		TypedQuery<Rating> query = em.createQuery("from Rating r WHERE  r.user.id = :userId AND r.restaurant.id = :restaurantId", Rating.class);

		query.setParameter("userId", userId);
		query.setParameter("restaurantId", restaurantId);

		return query.getResultList().stream().findFirst();
	}

	@Override
	@Deprecated
	public List<Rating> getRatedRestaurantsByUserId(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	// SErvice
	public Rating rateRestaurant(long userId, long restaurantId, int rating) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	// SErvice
	public boolean modifyRestaurantRating(long userId, long restaurantId, int rating) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Deprecated
	public int getNumberOfRates(long restaurantId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Rating createRating(User user, Restaurant restaurant, double rating) {
		Rating ratingObj = new Rating(rating);
		ratingObj.setUser(user);
		ratingObj.setRestaurant(restaurant);
		em.persist(ratingObj);
		return ratingObj;
	}
}
