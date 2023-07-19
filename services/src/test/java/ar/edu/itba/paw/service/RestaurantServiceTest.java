package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.Tags;
import ar.edu.itba.paw.persistence.RestaurantDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantServiceTest {

    private static final String RESTAURANT_NAME = "myrestaurant";
    private static final String RESTAURANT_ADDRESS = "9 de Julio";
    private static final String RESTAURANT_PHONE = "123456789";
    private static final Long RESTAURANT_ID = 1l;
    private static final String RESTAURANT_FACEBOOK = "facebook.com/rest";
    private static final String RESTAURANT_INSTAGRAM = "instagram.com/rest";
    private static final String RESTAURANT_TWITTER = "twitter.com/rest";

    @InjectMocks
    private RestaurantServiceImpl restaurantService = new RestaurantServiceImpl();

    @Mock
    private RestaurantDao restaurantDao;

    @Test
    public void testUpdateRestaurant(){

        Restaurant r = new Restaurant();
        r.setName(RESTAURANT_NAME);
        r.setAddress(RESTAURANT_ADDRESS);
        r.setPhoneNumber(RESTAURANT_PHONE);
        r.setId(RESTAURANT_ID);
        List<Tags> tags = new ArrayList();
        tags.add(Tags.ARABE);
        r.setTags(tags);
        r.setFacebook(RESTAURANT_FACEBOOK);
        r.setInstagram(RESTAURANT_INSTAGRAM);
        r.setTwitter(RESTAURANT_TWITTER);

        Mockito.when(restaurantDao.findById(RESTAURANT_ID)).thenReturn(Optional.of(r));

        List<Tags> newTags = new ArrayList();
        tags.add(Tags.AMERICANO);
        tags.add(Tags.ARGENTINO);

        restaurantService.updateRestaurant(RESTAURANT_ID,"newrestname", "newrestaddress", "987654321", newTags, "facebook.com/newrest", "twitter.com/newrest", "instagram.com/newrest");

        Assert.assertEquals("newrestname", r.getName());
        Assert.assertEquals("newrestaddress", r.getAddress());
        Assert.assertEquals("987654321", r.getPhoneNumber());
        Assert.assertEquals(newTags, r.getTags());
        Assert.assertEquals("facebook.com/newrest", r.getFacebook());
        Assert.assertEquals("twitter.com/newrest", r.getTwitter());
        Assert.assertEquals("instagram.com/newrest", r.getInstagram());
    }

}
