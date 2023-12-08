package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.ReservationStatus;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.models.ReservationRow;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Transactional
@Sql(scripts = "classpath:reservation-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ReservationJpaDaoTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    private DataSource ds;
    @Autowired
    private ReservationJpaDao reservationJpaDao;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        Object[] reservation = new Object[]{100L, 999L, 999L, "2023-08-08 21:00:00", 1, "PENDING"};

        String sql = "INSERT INTO reservations (reservation_id, user_id, restaurant_id, date, quantity, status) VALUES (?, ?, ?, ?, ? ,?)";

        // Insert test reservation for cancel
        jdbcTemplate.update(sql, reservation);
    }
    @After
    public void tearDown() {
        // Delete test reservation
        Object[] reservationId = new Object[]{100L};

        String sql = "DELETE FROM reservations WHERE reservation_id=?";

        // Delete test reservation for cancel
        jdbcTemplate.update(sql, reservationId);
    }


    @Test
    public void testFindById() {
        final String d = "2023-08-08 19:00:00";
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime date = LocalDateTime.parse(d, formatter);

        final Optional<Reservation> maybeReservation = reservationJpaDao.findById(1);
        assertTrue(maybeReservation.isPresent());
        Reservation reservation = maybeReservation.get();

        assertEquals(ReservationStatus.PENDING, reservation.getStatus());
        assertEquals(date, reservation.getDate());
        assertEquals(999L, reservation.getUser().getId().longValue());
        assertEquals(997L, reservation.getRestaurant().getId().longValue());
    }

    @Test
    public void testFindConfirmedByRestaurant() {
        final String d = "2023-08-07 15:00:00";
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime date = LocalDateTime.parse(d, formatter);
        final long restaurantId = 997L;


        List<Reservation> reservationList = reservationJpaDao.findFilteredReservations(1, 2, null, restaurantId, date, null, ReservationStatus.CONFIRMED, false);
        assertEquals(2, reservationList.size());
        assertEquals(3L, reservationList.get(0).getId().longValue());
        assertEquals(4L, reservationList.get(1).getId().longValue());
    }
    @Test
    public void testFindConfirmedByRestaurantCount() {
        String d = "2023-08-07 15:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(d, formatter);
        final long restaurantId = 997L;

        final int reservationCount = reservationJpaDao.findFilteredReservationsCount(null, restaurantId, date, null, ReservationStatus.CONFIRMED);
        assertEquals(2, reservationCount);
    }

    @Test
    public void testFindPendingByRestaurant() {
        final String d = "2023-08-07 15:00:00";
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime date = LocalDateTime.parse(d, formatter);

        final List<Reservation> reservationList = reservationJpaDao.findFilteredReservations(1, 2, null, 997l, date, null, ReservationStatus.PENDING, false);
        assertEquals(1, reservationList.size());
        assertEquals(1L, reservationList.get(0).getId().longValue());
    }
    @Test
    public void testFindPendingByRestaurantCount() {
        final String d = "2023-08-07 15:00:00";
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime date = LocalDateTime.parse(d, formatter);
        final long restaurantId = 997L;

        final int reservationCount = reservationJpaDao.findFilteredReservationsCount( null, restaurantId, date, null, ReservationStatus.PENDING);
        assertEquals(1,reservationCount);
    }

    @Test
    public void testFindByRestaurant() {
        final List<Reservation> reservationList = reservationJpaDao.findFilteredReservations(1, 4, null, 997l, null, null, null, false);
        assertEquals(3, reservationList.size());
        assertEquals(1L, reservationList.get(0).getId().longValue());
        assertEquals(3L, reservationList.get(1).getId().longValue());
        assertEquals(4L, reservationList.get(2).getId().longValue());
    }
    @Test
    public void testFindByRestaurantCount() {
        final long restaurantId = 997L;
        final int reservationsCount = reservationJpaDao.findFilteredReservationsCount(null, restaurantId, null, null, null);
        assertEquals(3, reservationsCount);
    }

    @Test
    public void testFindByUser() {
        final String d = "2023-08-07 15:00:00";
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime date = LocalDateTime.parse(d, formatter);
        final long userId = 998L;

        final List<Reservation> reservationList = reservationJpaDao.findFilteredReservations(1, 6, userId, null, date, null, null, false);

        assertEquals(2, reservationList.size());
        assertEquals(3L, reservationList.get(0).getId().longValue());
        assertEquals(5L, reservationList.get(1).getId().longValue());
    }

    @Test
    public void testCancelReservation() {
        final long userId = 999L;
        final long restaurantId = 999L;
        final long reservationId = 100L;
        final String date = "2023-08-08 21:00:00";
        final int quantity = 1;
        final String status = "PENDING";

        final String sql = String.format("SELECT * FROM reservations WHERE reservation_id=%d", reservationId);

        // Check reservation Exists
        ReservationRow reservationRow = jdbcTemplate.queryForObject(sql, ReservationRow.rowMapper);
        assertEquals(userId, reservationRow.getUserId().longValue());
        assertEquals(restaurantId, reservationRow.getRestaurantId().longValue());
        // Only care up to seconds in date
        assertEquals(date, reservationRow.getDate().substring(0, 19));
        assertEquals(quantity, reservationRow.getQuantity());
        assertEquals(status, reservationRow.getStatus());

        // Cancel Reservation (DELETE)
        reservationJpaDao.cancelReservation(reservationId);
        em.flush();

        // Check reservation Doesn't exist
        List<ReservationRow> reservationQueryResultList = jdbcTemplate.query(sql, ReservationRow.rowMapper);
        assertTrue(reservationQueryResultList.isEmpty());
    }

}
