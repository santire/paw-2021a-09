package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    MenuItem addItemToRestaurant(long restaurantId, MenuItem item);
    MenuItem updateMenuItem(Long menuItemId, MenuItem newItem);

    void deleteItemById(long menuId);

    Optional<MenuItem> getMenuItem(Long menuItemId);
    List<MenuItem> getRestaurantMenu(int page, int amountOnPage, Long restaurantId);
    int getRestaurantMenuCount(Long restaurantId);
}
