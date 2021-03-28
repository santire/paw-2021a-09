package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.persistence.ImageDao;
import ar.edu.itba.paw.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Override
    public Optional<Image> getImageByUserId(long userId){ return imageDao.getImageByUserId(userId); }

    @Override
    public Optional<Image> getImageByRestaurantId(long restaurantId){
        return imageDao.getImageByRestaurantId(restaurantId);
    }


    @Override
    public boolean saveUserImage(Image image){
        if(imageDao.userHasImage(image.getOwnerId())) {
            return imageDao.updateUserImage(image);
        }
        return imageDao.saveUserImage(image);
    }

    @Override
    public boolean saveRestaurantImage(Image image){
        if(imageDao.restaurantHasImage(image.getOwnerId())) {
            return imageDao.updateRestaurantImage(image);
        }
        return imageDao.saveRestaurantImage(image);
    }
}
