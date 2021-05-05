package ar.edu.itba.paw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.persistence.MenuDao;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

	@Override
	public List<MenuItem> findMenuByRestaurantId(long id) {
		return menuDao.findMenuByRestaurantId(id);
	}

	@Override
	public void addItemToRestaurant(long restaurantId, MenuItem item) {
		menuDao.addItemToRestaurant(restaurantId, item);
	}

	@Override
	public void deleteItemById(long menuId) {
		menuDao.deleteItemById(menuId);
	}

	@Override
	public boolean menuBelongsToRestaurant(long menuId, long restaurantId) {
		return findMenuByRestaurantId(restaurantId)
					.stream()
					.anyMatch(m -> menuId == m.getId());
	}
    
}
