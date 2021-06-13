package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.exceptions.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SocialMediaServiceImpl implements SocialMediaService {

    @Autowired
    private RestaurantService restaurantService;


    @Override
    @Transactional
    public void updateFacebook(String url, long restaurantId) {
        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        if(url.isEmpty())
            restaurant.setFacebook(null);
        else
            restaurant.setFacebook(url);
    }

    @Override
    @Transactional
    public void updateTwitter(String url, long restaurantId) {
        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);

        if(url.isEmpty())
            restaurant.setTwitter(null);
        else
            restaurant.setTwitter(url);
    }

    @Override
    @Transactional
    public void updateInstagram(String url, long restaurantId) {
        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        if(url.isEmpty())
            restaurant.setInstagram(null);
        else
         restaurant.setInstagram(url);
    }



}
