package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.Tags;
import ar.edu.itba.paw.model.User;
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
    public List<Restaurant> findByName(String name){
        return this.restaurantDao.findByName(name);
    }

    // CREATE

    @Override
    public Restaurant registerRestaurant(String name, String address, String phoneNumber,
                                         float rating, long userId){

        return restaurantDao.registerRestaurant(name, address, phoneNumber, rating, userId);
    }

    @Override
    public boolean setImageByRestaurantId(Image image, long restaurantId) {
        return restaurantDao.setImageByRestaurantId(image, restaurantId);
    }

    // READ

    @Override
    public List<Restaurant> getAllRestaurants(){
        return this.restaurantDao.getAllRestaurants();
    }

    @Override
    public List<Restaurant> getAllRestaurants(String searchTerm){
        return this.restaurantDao.getAllRestaurants(searchTerm);
    }


    @Override
    public List<Restaurant> getPopularRestaurants(){
        return this.restaurantDao.getPopularRestaurants();
    }

    @Override
    public List<Restaurant> getHotRestaurants(int lastDays) {
        return this.restaurantDao.getHotRestaurants(lastDays);
    }

    // UPDATE
    @Override
    public void updateName(long id, String name) {
        restaurantDao.updateName(id, name);
    }

    @Override
    public void updateAddress(long id, String address) {
        restaurantDao.updateAddress(id, address);
    }

    @Override
    public void updatePhoneNumber(long id, String phoneNumber) {
        restaurantDao.updatePhoneNumber(id, phoneNumber);
    }

    @Override
    public Optional<Restaurant> updateRestaurant(long id, String name, String address, String phoneNumber) {
       return restaurantDao.updateRestaurant(id, name, address ,phoneNumber);
    }


    // DESTROY

    @Override
    public boolean deleteRestaurantById(long id){
        return this.restaurantDao.deleteRestaurantById(id);
    }

    @Override
    public boolean deleteRestaurantByName(String name){
        return this.restaurantDao.deleteRestaurantByName(name);
    }

    // ??

    @Override
    public Optional<User> findRestaurantOwner(long id) {
        return restaurantDao.findRestaurantOwner(id);
    }

    @Override
    public List<Restaurant> getRestaurantsFromOwner(long userId) {
        return restaurantDao.getRestaurantsFromOwner(userId);
    }

    @Override
    public boolean addTag(long restaurantId, Tags tag) {

        List<Tags> tags = restaurantDao.tagsInRestaurant(restaurantId);

        if(tags.size() >= 3 || tags.contains(tag))
            return false;


        restaurantDao.addTag(restaurantId,tag.getValue());

        return true;
    }

    @Override
    public boolean removeTag(long restaurantId, Tags tag) {


        return restaurantDao.removeTag(restaurantId,tag.getValue());
    }

    @Override
    public List<Tags> tagsInRestaurant(long restaurantId) {
        return restaurantDao.tagsInRestaurant(restaurantId);
    }

    @Override
    public List<Restaurant> getRestaurantsWithTags(List<Tags> tags) {
        return restaurantDao.getRestaurantsWithTags(tags);
    }

    @Override
    public List<Restaurant> getRestaurantsFilteredBy(String name, List<Tags> tags, double minAvgPrice, double maxAvgPrice) {
        return restaurantDao.getRestaurantsFilteredBy(name,tags,minAvgPrice,maxAvgPrice);
    }

}
