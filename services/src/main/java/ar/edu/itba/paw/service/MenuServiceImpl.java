package ar.edu.itba.paw.service;


import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.exceptions.MenuItemNotFoundException;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.persistence.MenuDao;
import ar.edu.itba.paw.persistence.RestaurantDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private RestaurantDao restaurantDao;

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuItem> getMenuItem(Long menuItemId) {
        return menuDao.getMenuItemById(menuItemId);
    }

    @Override
    @Transactional
    public MenuItem addItemToRestaurant(long restaurantId, MenuItem item) {
        Restaurant restaurant = restaurantDao.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        LOGGER.debug("restaurant: {}", restaurant.getName());
        return menuDao.addItemToRestaurant(restaurant, item);
    }

    @Override
    @Transactional
    public MenuItem updateMenuItem(Long menuItemId, MenuItem newItem) {
        MenuItem menuItem = menuDao.getMenuItemById(menuItemId).orElseThrow(MenuItemNotFoundException::new);
        menuItem.setDescription(newItem.getDescription());
        menuItem.setName(menuItem.getName());
        menuItem.setPrice(menuItem.getPrice());

        return menuItem;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItem> getRestaurantMenu(int page, int amountOnPage, Long restaurantId) {
        return menuDao.getMenuByRestaurantId(page, amountOnPage, restaurantId);
    }

    @Override
    @Transactional(readOnly = true)
    public int getRestaurantMenuCount(Long restaurantId) {
        return menuDao.getMenuByRestaurantIdCount(restaurantId);
    }

    @Override
    @Transactional
    public void deleteItemById(long menuId) {
        MenuItem menuItem = menuDao.getMenuItemById(menuId).orElseThrow(MenuItemNotFoundException::new);
        menuDao.deleteItemById(menuItem.getId());
    }

}