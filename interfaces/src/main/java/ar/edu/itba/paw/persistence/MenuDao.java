package ar.edu.itba.paw.persistence;

import java.util.List;

import ar.edu.itba.paw.model.MenuItem;

public interface MenuDao {
    public List<MenuItem> findMenuByRestaurantId(long id);
    public void addItemToRestaurant(long restaurantId, MenuItem item);
}
