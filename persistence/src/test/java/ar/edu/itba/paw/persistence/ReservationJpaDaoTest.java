package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Reservation;
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

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@Transactional
@Sql(scripts = "classpath:reservation-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ReservationJpaDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private ReservationJpaDao reservationJpaDao;

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
        assertFalse(reservation.getConfirmed());
        assertEquals(date, reservation.getDate());

        assertEquals(999l,  reservation.getUser().getId().longValue());
        assertEquals(997l, reservation.getRestaurant().getId().longValue());
    }

    @Test
    public void testFindConfirmedByRestaurant() {

        String d = "2023-08-07 15:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(d, formatter);

        List<Reservation> reservationList = reservationJpaDao.findConfirmedByRestaurant(1, 2, 997l, date);
        assertEquals(2, reservationList.size());
        assertEquals(3, reservationList.get(0).getId().longValue());
        assertEquals(4, reservationList.get(1).getId().longValue());
    }

    @Test
    public void testFindPendingByRestaurant() {

        String d = "2023-08-07 15:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(d, formatter);

        List<Reservation> reservationList = reservationJpaDao.findPendingByRestaurant(1, 2, 997l, date);
        assertEquals(1, reservationList.size());
        assertEquals(1, reservationList.get(0).getId().longValue());
    }

    @Test
    public void testFindByRestaurant() {

        List<Reservation> reservationList = reservationJpaDao.findByRestaurant(2, 2, 997l);
        assertEquals(1, reservationList.size());
        assertEquals(4, reservationList.get(0).getId().longValue());

        reservationList = reservationJpaDao.findByRestaurant(1, 4, 997l);
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

        List<Reservation> reservationList = reservationJpaDao.findByUser(1, 6, 999l, date);

        assertEquals(3, reservationList.size());
        assertEquals(2, reservationList.get(0).getId().longValue());
        assertEquals(1, reservationList.get(1).getId().longValue());
        assertEquals(4, reservationList.get(2).getId().longValue());
    }

    @Test
    public void testCancelReservation() {
        long ID = 5;

        final Optional<Reservation> maybeReservation = reservationJpaDao.findById(ID);
        assertTrue(maybeReservation.isPresent());

        reservationJpaDao.cancelReservation(ID);

        final Optional<Reservation> notReservation = reservationJpaDao.findById(ID);
        assertFalse(notReservation.isPresent());
    }

    @Test
    public void testReservationNotFound() {
        long ID = 10;

        final Optional<Reservation> maybeReservation = reservationJpaDao.findById(ID);
        assertFalse(maybeReservation.isPresent());
    }

}
