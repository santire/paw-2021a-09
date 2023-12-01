package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface MenuDao {
    MenuItem addItemToRestaurant(Restaurant restaurant, MenuItem item);

    void deleteItemById(long menuId);
    Optional<MenuItem> getMenuItemById(Long menuItemId);
    List<MenuItem> getMenuByRestaurantId(int page, int amountOnPage, Long restaurantId);
    int getMenuByRestaurantIdCount(Long restaurantId);
}
