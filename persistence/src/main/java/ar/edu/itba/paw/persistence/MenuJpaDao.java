package ar.edu.itba.paw.persistence;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.model.Restaurant;

@Repository
public class MenuJpaDao implements MenuDao {

    @PersistenceContext
    private EntityManager em;

	@Override
	public void addItemToRestaurant(Restaurant restaurant, MenuItem item) {
		restaurant.addMenuItem(item);
		item.setRestaurant(restaurant);
		em.persist(item);
	}

	@Override
	public void deleteItemById(long menuId) {
		Query query = em.createQuery("delete MenuItem where id = :id");
		query.setParameter("id", menuId);
		query.executeUpdate();
	}

}
