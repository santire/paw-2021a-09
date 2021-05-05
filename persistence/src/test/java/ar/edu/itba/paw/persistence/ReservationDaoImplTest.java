package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Rollback
@Sql(scripts = "classpath:reservation-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ReservationDaoImplTest {
    @Autowired
    private DataSource ds;

    @Autowired
    private ReservationDaoImpl reservationDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void modifyReservation(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String date = "2021-05-20 11:35";
        int newQuantity = 5;

        LocalDateTime newDate = LocalDateTime.parse(date, formatter);
        Optional<Reservation> newReservation = reservationDao.modifyReservation(1, newDate, newQuantity);
        assertTrue(newReservation.isPresent());
        assertEquals("2021-05-20", String.valueOf(newReservation.get().getDate().toLocalDate()));
        assertEquals(newQuantity, newReservation.get().getQuantity());
    }
}
