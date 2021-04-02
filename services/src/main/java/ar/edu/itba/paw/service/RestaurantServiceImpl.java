package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.persistence.RestaurantDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService{
    @Autowired
    private RestaurantDao restaurantDao;

    @Override
    public Optional<Restaurant> findById(long id){
        return this.restaurantDao.findById(id);
    }

    @Override
    public Optional<Restaurant> findByName(String name){
        return this.restaurantDao.findByName(name);
    }

    @Override
    public Restaurant registerRestaurant(String name, String address, String phoneNumber,
                                         float rating, long userId){
        return restaurantDao.registerRestaurant(name, address, phoneNumber, rating, userId);
    }

    @Override
    public List<Restaurant> getAllRestaurants(){
        return this.restaurantDao.getAllRestaurants();
    }

    @Override
    public boolean deleteRestaurantById(long id){
        return this.restaurantDao.deleteRestaurantById(id);
    }

    @Override
    public boolean deleteRestaurantByName(String name){
        return this.restaurantDao.deleteRestaurantByName(name);
    }

}
