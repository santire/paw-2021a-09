package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.model.Restaurant;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class MenuJpaDao implements MenuDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public MenuItem addItemToRestaurant(Restaurant restaurant, MenuItem item) {
        MenuItem newItem = new MenuItem(item.getName(), item.getDescription(), item.getPrice());
        newItem.setRestaurant(restaurant);
        restaurant.addMenuItem(newItem);

        em.persist(newItem);
        return newItem;
    }

    @Override
    public void deleteItemById(long menuId) {
        Query query = em.createQuery("delete MenuItem where id = :id");
        query.setParameter("id", menuId);
        query.executeUpdate();
    }

}
