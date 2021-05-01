package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Reservation;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ReservationDaoImpl implements ReservationDao{


    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (rs, rowNum) -> new Reservation(
            rs.getLong("reservation_id"),
            rs.getLong("user_id"),
            rs.getLong("restaurant_id"),
            rs.getDate("date"),
            rs.getLong("quantity")
    );

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;


    @Autowired
    public ReservationDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservations")
                .usingGeneratedKeyColumns("reservation_id")
                .usingColumns("user_id", "restaurant_id", "date", "quantity");
    }


    @Override
    public List<Reservation> findByUser(long userId) {
        return jdbcTemplate.query("SELECT * FROM reservations WHERE user_id = ?", RESERVATION_ROW_MAPPER, userId).stream().collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByUser(int page, int amountOnPage, long userId) {
        return jdbcTemplate.query(
                "SELECT * FROM reservations"
                +
                " WHERE user_id = ?"
                +
                " OFFSET ? FETCH NEXT ? ROWS ONLY"
                , RESERVATION_ROW_MAPPER, userId, (page-1)*amountOnPage, amountOnPage)
                .stream().collect(Collectors.toList());
    }

    @Override
    public int findByUserPageCount(int amountOnPage, long userId) {
        return jdbcTemplate.query(
                "SELECT CEILING(COUNT(*)::numeric/?) as c"
                +
                " FROM reservations"
                +
                " WHERE user_id = ?"
                ,(r,n) -> r.getInt("c"), amountOnPage, userId)
                .stream().findFirst().orElse(0);

    }

    @Override
    public List<Reservation> findByRestaurant(long restaurantId) {
        return jdbcTemplate.query("SELECT * FROM reservations WHERE restaurant_id = ?", RESERVATION_ROW_MAPPER, restaurantId).stream().collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId) {
        return jdbcTemplate.query(
                "SELECT * FROM reservations"
                +
                " WHERE restaurant_id = ?"
                +
                " OFFSET ? FETCH NEXT ? ROWS ONLY"
                , RESERVATION_ROW_MAPPER, restaurantId, (page-1)*amountOnPage, amountOnPage)
                .stream().collect(Collectors.toList());
    }

    @Override
    public int findByRestaurantPageCount(int amountOnPage, long restaurantId) {
        return jdbcTemplate.query(
                "SELECT CEILING(COUNT(*)::numeric/?) as c"
                +
                " FROM reservations"
                +
                " WHERE restaurant_id = ?"
                ,(r,n) -> r.getInt("c"), amountOnPage, restaurantId)
                .stream().findFirst().orElse(0);
    }

    @Override
    public Optional<Reservation> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM reservations WHERE reservation_id = ?", RESERVATION_ROW_MAPPER, id)
                .stream().findFirst();
    }

    @Override
    public Reservation addReservation(long userId, long restaurantId, Date date, long quantity) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("restaurant_id", restaurantId);
        params.addValue("date", date);
        params.addValue("quantity", quantity);

        final Number reservationId = jdbcInsert.executeAndReturnKey(params);

    return new Reservation(reservationId.longValue(),userId,restaurantId,date,quantity);
    }

    @Override
    public boolean cancelReservation(int id) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        Object[] args = new Object[] {id};

        return jdbcTemplate.update(sql, args) == 1;
    }

    @Override
    public Optional<Reservation> modifyReservation(int reservationId, Date date, long quantity){
        jdbcTemplate.update("UPDATE reservations SET date = ?, quantity = ? WHERE reservation_id = ?", date, quantity, reservationId);
        return findById(reservationId);
    }
}
