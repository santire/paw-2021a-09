package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;

import java.util.Optional;

public interface ImageService {
    //public Optional<Image> getImageById(long imageId);
    public Optional<Image> getImageByUserId(long authorId);
    public Optional<Image> getImageByRestaurantId(long imageId);

    public boolean saveUserImage(Image image);
    public boolean saveRestaurantImage(Image image);
}
