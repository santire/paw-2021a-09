package ar.edu.itba.paw.service;

public interface SocialMediaService {

    public void updateFacebook(String url, long restaurantId);
    public void updateTwitter(String url, long restaurantId);
    public void updateInstagram(String url, long restaurantId);

}
