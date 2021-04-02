package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;
import javax.swing.text.html.Option;

import ar.edu.itba.paw.model.Restaurant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import ar.edu.itba.paw.persistence.config.TestConfig;

@Rollback
@Sql(scripts = "classpath:restaurant-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class RestaurantDaoImplTest {
    // General purpose
    private static final int INSERTED_SIZE = 3;

    // Restaurant to search
    private static final long ID = 1;
    private static final String PREV_INSERTED_NAME = "BurgerKing";
    private static final String PATTERN = "Burg";

    // Restaurant to insert
    private static final String NAME = "McDonalds";
    private static final String ADDRESS = "9 de Julio";
    private static final String PHONE_NUMBER = "46511234";
    private static final float RATING = 0;
    private static final long USER_ID = 1;

    @Autowired
    private DataSource ds;

    @Autowired
    private RestaurantDaoImpl restaurantDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testRestaurantRegister() {
        final Restaurant restaurant = restaurantDao.registerRestaurant(
                NAME, ADDRESS, PHONE_NUMBER, RATING, USER_ID
        );

        assertEquals(NAME, restaurant.getName());
        assertEquals(ADDRESS, restaurant.getAddress());
        assertEquals(PHONE_NUMBER, restaurant.getPhoneNumber());
        assertEquals(USER_ID, restaurant.getUserId());
        assertEquals(INSERTED_SIZE + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "restaurants"));
    }

    @Test
    public void testDeleteRestaurants(){
        boolean success = restaurantDao.deleteRestaurantById(1);
        assertEquals(INSERTED_SIZE - 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "restaurants"));
    }

    @Test
    public void testListAllRestaurants(){
        List<Restaurant> restaurantList = restaurantDao.getAllRestaurants();
        assertEquals(INSERTED_SIZE, restaurantList.size());
    }

    @Test
    public void testRestaurantSearchById(){
        Optional<Restaurant> restaurant = restaurantDao.findById(ID);
        assertTrue(restaurant.isPresent());
        assertEquals(PREV_INSERTED_NAME, restaurant.get().getName());
    }
}
