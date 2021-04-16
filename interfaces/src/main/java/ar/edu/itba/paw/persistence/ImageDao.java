package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;

import java.awt.*;
import java.io.FileInputStream;
import java.util.Optional;

public interface ImageDao {
    public Optional<Image> getImageByRestaurantId(long restaurantId);
    public boolean saveRestaurantImage(Image image);
    public boolean restaurantHasImage(long restaurantId);
    public boolean updateRestaurantImage(Image image);
}
