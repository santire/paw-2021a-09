package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.model.Restaurant;

public interface MenuDao {
    MenuItem addItemToRestaurant(Restaurant restaurant, MenuItem item);

    void deleteItemById(long menuId);
}
