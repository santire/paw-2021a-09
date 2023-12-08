package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.ReservationStatus;
import ar.edu.itba.paw.persistence.config.TestConfig;
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
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@Sql(scripts = "classpath:reservation-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ReservationJpaDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private ReservationJpaDao reservationJpaDao;

    @PersistenceContext
    EntityManager em;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }


    @Test
    public void testFindById() {
        String d = "2023-08-08 19:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(d, formatter);

        final Optional<Reservation> maybeReservation = reservationJpaDao.findById(1);
        assertTrue(maybeReservation.isPresent());
        Reservation reservation = maybeReservation.get();

        assertEquals(ReservationStatus.PENDING, reservation.getStatus());
        assertEquals(date, reservation.getDate());
        assertEquals(999L,  reservation.getUser().getId().longValue());
        assertEquals(997L, reservation.getRestaurant().getId().longValue());
    }

    @Test
    public void testFindConfirmedByRestaurant() {
        String d = "2023-08-07 15:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(d, formatter);


        List<Reservation> reservationList = reservationJpaDao.findFilteredReservations(1, 2, null,997l, date, null, ReservationStatus.CONFIRMED, false );
        assertEquals(2, reservationList.size());
        assertEquals(3, reservationList.get(0).getId().longValue());
        assertEquals(4, reservationList.get(1).getId().longValue());
    }

    @Test
    public void testFindPendingByRestaurant() {
        String d = "2023-08-07 15:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(d, formatter);

        List<Reservation> reservationList = reservationJpaDao.findFilteredReservations(1, 2, null,997l, date, null, ReservationStatus.PENDING, false );
        assertEquals(1, reservationList.size());
        assertEquals(1, reservationList.get(0).getId().longValue());
    }

    @Test
    public void testFindByRestaurant() {
        List<Reservation> reservationList = reservationJpaDao.findFilteredReservations(1, 4, null,997l, null, null, null, false );
        assertEquals(3, reservationList.size());
        assertEquals(1, reservationList.get(0).getId().longValue());
        assertEquals(3, reservationList.get(1).getId().longValue());
        assertEquals(4, reservationList.get(2).getId().longValue());
    }

    @Test
    public void testFindByUser() {
        String d = "2023-08-07 15:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(d, formatter);

        List<Reservation> reservationList = reservationJpaDao.findFilteredReservations(1, 6, 999l,null, date, null, null, false );

        assertEquals(3, reservationList.size());
        assertEquals(2, reservationList.get(0).getId().longValue());
        assertEquals(1, reservationList.get(1).getId().longValue());
        assertEquals(4, reservationList.get(2).getId().longValue());
    }

    @Test
    public void testCancelReservation() {
        long reservationId = 5;

        SimpleReservation reservation = jdbcTemplate.queryForObject("SELECT * FROM reservations WHERE reservation_id = " + reservationId , (rs, rowNum) ->
                new SimpleReservation(
                        rs.getLong("reservation_id"),
                        rs.getLong("user_id"),
                        rs.getLong("restaurant_id"),
                        rs.getString("date"),
                        rs.getInt("quantity"),
                        rs.getString("status")
                ));

        reservationJpaDao.cancelReservation(reservation.reservation_id);
        em.flush();

        List <SimpleReservation> reservations = jdbcTemplate.query("SELECT * FROM reservations WHERE reservation_id = " + reservationId , (rs, rowNum) ->
                new SimpleReservation(
                        rs.getLong("reservation_id"),
                        rs.getLong("user_id"),
                        rs.getLong("restaurant_id"),
                        rs.getString("date"),
                        rs.getInt("quantity"),
                        rs.getString("status")
                ));

        assertTrue(reservations.isEmpty());
    }

    private static class SimpleReservation {
        Long reservation_id;
        Long user_id;
        Long restaurant_id;
        String date;
        int quantity;
        String status;

        public SimpleReservation(Long reservation_id,  Long user_id, Long restaurant_id, String date, int quantity, String status) {
            this.reservation_id = reservation_id;
            this.user_id = user_id;
            this.restaurant_id = restaurant_id;
            this.date = date;
            this.quantity = quantity;
            this.quantity = quantity;
            this.status = status;
        }
    }
}
