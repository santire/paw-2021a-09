package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;

import java.awt.*;
import java.io.FileInputStream;
import java.util.Optional;

public interface ImageDao {
    //public Optional<Image> getImageById(long imageId);
    public Optional<Image> getImageByUserId(long userId);
    public Optional<Image> getImageByRestaurantId(long restaurantId);

    public boolean saveUserImage(Image image);
    public boolean saveRestaurantImage(Image image);
    public boolean userHasImage(long userId);
    public boolean restaurantHasImage(long restaurantId);
    public boolean updateUserImage(Image image);
    public boolean updateRestaurantImage(Image image);
}
