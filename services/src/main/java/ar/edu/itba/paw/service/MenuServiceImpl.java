package ar.edu.itba.paw.service;


import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import ar.edu.itba.paw.persistence.MenuDao;
import ar.edu.itba.paw.persistence.RestaurantDao;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private RestaurantDao restaurantDao;

    @Override
    @Transactional
    public MenuItem addItemToRestaurant(long restaurantId, MenuItem item) {
        Restaurant restaurant = restaurantDao.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        LOGGER.debug("restaurant: {}", restaurant.getName());
        return menuDao.addItemToRestaurant(restaurant, item);
    }

    @Override
    @Transactional
    public void deleteItemById(long menuId) {
        menuDao.deleteItemById(menuId);
    }

    @Override
    public boolean menuBelongsToRestaurant(long menuId, long restaurantId) {
        return restaurantDao.menuBelongsToRestaurant(restaurantId, menuId);
    }
}