package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.MenuItem;

public interface MenuService {
    MenuItem addItemToRestaurant(long restaurantId, MenuItem item);

    void deleteItemById(long menuId);

    boolean menuBelongsToRestaurant(long menuId, long restaurantId);
}
