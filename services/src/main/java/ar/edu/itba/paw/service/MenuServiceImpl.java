package ar.edu.itba.paw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.persistence.MenuDao;

// TODO: Find out if this should be its own service
// or be returned as a Restaurant property
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

	@Override
	public List<MenuItem> findMenuByRestaurantId(long id) {
		return menuDao.findMenuByRestaurantId(id);
	}
    
}
