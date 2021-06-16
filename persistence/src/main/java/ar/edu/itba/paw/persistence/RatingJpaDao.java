package ar.edu.itba.paw.persistence;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
		TypedQuery<Rating> query = em.createQuery("from Rating r WHERE  r.user.id = :userId AND r.restaurant.id = :restaurantId", Rating.class);

		query.setParameter("userId", userId);
		query.setParameter("restaurantId", restaurantId);

		return query.getResultList().stream().findFirst();
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
