package ar.edu.itba.paw.service;

import java.util.List;

import ar.edu.itba.paw.model.MenuItem;

public interface MenuService {
    public List<MenuItem> findMenuByRestaurantId(long id);
    public void addItemToRestaurant(long restaurantId, MenuItem item);
    public void deleteItemById(long menuId);
	  public boolean menuBelongsToRestaurant(long menuId, long restaurantId);
}
