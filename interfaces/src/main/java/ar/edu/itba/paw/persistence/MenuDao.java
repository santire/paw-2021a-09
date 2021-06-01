package ar.edu.itba.paw.persistence;

import java.util.List;

import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.model.Restaurant;

public interface MenuDao {
    public void addItemToRestaurant(Restaurant restaurant, MenuItem item);
    public void deleteItemById(long menuId);
}
