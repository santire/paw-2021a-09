package ar.edu.itba.paw.service;

import java.util.List;

import ar.edu.itba.paw.model.MenuItem;

public interface MenuService {
    public List<MenuItem> findMenuByRestaurantId(long id);
}
