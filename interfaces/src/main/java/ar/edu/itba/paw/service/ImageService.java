package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;

import java.util.Optional;

public interface ImageService {
    public Optional<Image> getImageByRestaurantId(long restaurantId);
    public boolean saveRestaurantImage(Image image);
    public boolean setImageByRestaurantId(Image image, long restaurantId);
}
