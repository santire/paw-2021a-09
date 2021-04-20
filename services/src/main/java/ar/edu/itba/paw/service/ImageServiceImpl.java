package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.persistence.ImageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Override
    public Optional<Image> getImageByRestaurantId(long restaurantId){
        return imageDao.getImageByRestaurantId(restaurantId);
    }

    // For now, only 1 profile picture per restaurant
    @Override
    public boolean saveRestaurantImage(Image image){
        // if(imageDao.restaurantHasImage(image.getOwnerId())) {
            // return imageDao.updateRestaurantImage(image);
        // }
        return imageDao.saveRestaurantImage(image);
    }
}
