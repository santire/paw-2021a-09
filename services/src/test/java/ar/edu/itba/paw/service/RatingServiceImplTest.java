package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.persistence.RatingDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RatingServiceImplTest {

    private static final long RESTAURANT_ID = 1;
    private static final int RATING = 5;
    private static final long USER_ID = 1;

    @InjectMocks
    private RatingServiceImpl ratingService = new RatingServiceImpl();

    @Mock
    private RatingDao ratingDao;
    @Mock
    private RestaurantService restaurantService;

    @Test
    public void rateRestaurant(){
        Mockito.when(ratingDao.rateRestaurant(Mockito.eq(USER_ID), Mockito.eq(RESTAURANT_ID),
                Mockito.eq(RATING))).thenReturn(new Rating(USER_ID, RESTAURANT_ID, RATING));

        Rating rating = ratingService.rateRestaurant(USER_ID, RESTAURANT_ID, RATING);

        assertEquals(USER_ID, rating.getUserId());
        assertEquals(RESTAURANT_ID, rating.getRestaurantId());
        assertEquals(RATING, rating.getRating());
    }

    @Test
    public void getRating(){
        Mockito.when(ratingDao.getRating(Mockito.eq(USER_ID), Mockito.eq(RESTAURANT_ID)))
                .thenReturn(java.util.Optional.of(new Rating(USER_ID, RESTAURANT_ID, RATING)));

        Optional<Rating> rating = ratingService.getRating(USER_ID, RESTAURANT_ID);

        assertTrue(rating.isPresent());
        assertEquals(RATING, rating.get().getRating());
        assertEquals(USER_ID, rating.get().getUserId());
        assertEquals(RESTAURANT_ID, rating.get().getRestaurantId());
    }

    @Test
    public void updateAvgRating(){
        Mockito.when(ratingDao.getNumberOfRates(Mockito.eq(RESTAURANT_ID)))
                .thenReturn(2);

        int newRating = 4;
        int numberOfRates = ratingDao.getNumberOfRates(RESTAURANT_ID);
        float currentAvgRating = 5;
        float sumOfRatings = numberOfRates * currentAvgRating;
        sumOfRatings += newRating;
        int newAvg = Math.round(sumOfRatings/(numberOfRates+1));

        assertEquals(5, newAvg);
    }
}
