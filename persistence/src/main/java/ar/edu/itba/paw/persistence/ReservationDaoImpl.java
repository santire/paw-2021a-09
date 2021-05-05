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
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
            rs.getTimestamp("date"),
            rs.getLong("quantity"),
            rs.getBoolean("confirmed")
    );

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;


    @Autowired
    public ReservationDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservations")
                .usingGeneratedKeyColumns("reservation_id")
                .usingColumns("user_id", "restaurant_id", "date", "quantity", "confirmed");
    }


    @Override
    public List<Reservation> findByUser(long userId) {
        return jdbcTemplate.query("SELECT * FROM reservations WHERE user_id = ? ORDER BY date ASC", RESERVATION_ROW_MAPPER, userId).stream().collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByUser(int page, int amountOnPage, long userId, Timestamp currentTime) {
        return jdbcTemplate.query(
                "SELECT * FROM reservations"
                +
                " WHERE user_id = ? and date >= ? ORDER BY date ASC"
                +
                " OFFSET ? FETCH NEXT ? ROWS ONLY"
                , RESERVATION_ROW_MAPPER, userId, currentTime,  (page-1)*amountOnPage, amountOnPage)
                .stream().collect(Collectors.toList());
    }

    @Override
    public int findByUserPageCount(int amountOnPage, long userId, Timestamp currentTime) {
        int amount = jdbcTemplate.query(
                "SELECT CEILING(COUNT(*)::numeric/?) as c"
                +
                " FROM reservations"
                +
                " WHERE user_id = ? and date >= ?"
                ,(r,n) -> r.getInt("c"), amountOnPage, userId, currentTime)
                .stream().findFirst().orElse(0);
        return amount <=0 ? 1 : amount;

    }

    @Override
    public List<Reservation> findByRestaurant(long restaurantId) {
        return jdbcTemplate.query("SELECT * FROM reservations WHERE restaurant_id = ? ORDER BY date ASC", RESERVATION_ROW_MAPPER, restaurantId).stream().collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByRestaurant(int page, int amountOnPage, long restaurantId) {
        return jdbcTemplate.query(
                "SELECT * FROM reservations"
                +
                " WHERE restaurant_id = ? ORDER BY date ASC"
                +
                " OFFSET ? FETCH NEXT ? ROWS ONLY"
                , RESERVATION_ROW_MAPPER, restaurantId, (page-1)*amountOnPage, amountOnPage)
                .stream().collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findConfirmedByRestaurant(int page, int amountOnPage, long restaurantId, Timestamp currentTime) {
        return jdbcTemplate.query(
                "SELECT * FROM reservations"
                        +
                        " WHERE restaurant_id = ? and confirmed = true and date >= ? ORDER BY date ASC"
                        +
                        " OFFSET ? FETCH NEXT ? ROWS ONLY"
                , RESERVATION_ROW_MAPPER, restaurantId, currentTime,  (page-1)*amountOnPage, amountOnPage)
                .stream().collect(Collectors.toList());
    }

    @Override
    public int findConfirmedByRestaurantPageCount(int amountOnPage, long restaurantId, Timestamp currentTime) {
        int amount =  jdbcTemplate.query(
                "SELECT CEILING(COUNT(*)::numeric/?) as c"
                        +
                        " FROM reservations"
                        +
                        " WHERE restaurant_id = ? and confirmed = true and date >= ?"
                ,(r,n) -> r.getInt("c"), amountOnPage, restaurantId, currentTime)
                .stream().findFirst().orElse(0);
        return amount <= 0 ? 1 : amount;
    }

    @Override
    public List<Reservation> findPendingByRestaurant(int page, int amountOnPage, long restaurantId, Timestamp currentTime) {
        return jdbcTemplate.query(
                "SELECT * FROM reservations"
                        +
                        " WHERE restaurant_id = ? AND confirmed = false and date >= ? ORDER BY date ASC"
                        +
                        " OFFSET ? FETCH NEXT ? ROWS ONLY"
                , RESERVATION_ROW_MAPPER, restaurantId, currentTime, (page-1)*amountOnPage, amountOnPage)
                .stream().collect(Collectors.toList());
    }

    @Override
    public int findPendingByRestaurantPageCount(int amountOnPage, long restaurantId, Timestamp currentTime) {
        int amount =  jdbcTemplate.query(
                "SELECT CEILING(COUNT(*)::numeric/?) as c"
                        +
                        " FROM reservations"
                        +
                        " WHERE restaurant_id = ? and confirmed = false and date >= ?"
                ,(r,n) -> r.getInt("c"), amountOnPage, restaurantId, currentTime)
                .stream().findFirst().orElse(0);
        return amount <= 0 ? 1 : amount;
    }

    @Override
    public int findByRestaurantPageCount(int amountOnPage, long restaurantId) {
        int amount =  jdbcTemplate.query(
                "SELECT CEILING(COUNT(*)::numeric/?) as c"
                +
                " FROM reservations"
                +
                " WHERE restaurant_id = ?"
                ,(r,n) -> r.getInt("c"), amountOnPage, restaurantId)
                .stream().findFirst().orElse(0);
        return amount <= 0 ? 1 : amount;
    }



    @Override
    public Optional<Reservation> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM reservations WHERE reservation_id = ?", RESERVATION_ROW_MAPPER, id)
                .stream().findFirst();
    }

    @Override
    public Reservation addReservation(long userId, long restaurantId, LocalDateTime date, long quantity) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("restaurant_id", restaurantId);
        params.addValue("date", Timestamp.valueOf(date));
        params.addValue("quantity", quantity);
        params.addValue("confirmed", false);

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
    public Optional<Reservation> modifyReservation(int reservationId, LocalDateTime date, long quantity){
        jdbcTemplate.update("UPDATE reservations SET date = ?, quantity = ? WHERE reservation_id = ?", Timestamp.valueOf(date), quantity, reservationId);
        return findById(reservationId);
    }

    @Override
    public boolean confirmReservation(int reservationId){
        jdbcTemplate.update("UPDATE reservations SET confirmed = true WHERE reservation_id = ?", reservationId);
        return true;
    }
}
