package ru.app.parking_backend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.entity.Reservation;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    private LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts != null ? ts.toLocalDateTime() : null;
    }

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> new Reservation(
            rs.getInt("id"),
            rs.getObject("parking_id", Integer.class),
            rs.getObject("car_id", Integer.class),
            rs.getBoolean("is_paid"),
            toLocalDateTime(rs.getTimestamp("start_time")),
            toLocalDateTime(rs.getTimestamp("end_time"))
    );

    public List<Reservation> findAllReservations() {
        String sql = "select * from reservations";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public List<Reservation> findCarByNumberCarAndFullName(String carNumber, String fullName) {
        String sql = """
                        select
                        r.id,
                        r.parking_id,
                        r.car_id,
                        r.is_paid,
                        r.start_time,
                        r.end_time
                        from reservations r
                        join cars c on r.car_id = c.id
                        join clients cl on c.client_id = cl.id
                        join parking_spaces p on r.parking_id = p.id
                        where upper(c.number_car) = ? and upper(cl.full_name) = ?
                """;
        return jdbcTemplate.query(sql, reservationRowMapper, carNumber.toUpperCase(), fullName.toUpperCase());
    }

    public void saveReservation(Integer carId, Integer parkingSpaceId) {
        LocalDateTime now = LocalDateTime.now();
        String sql = "insert into reservations (car_id, parking_id, start_time, add_time) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, carId, parkingSpaceId, now, now);
    }

    public void releaseReservation(Integer id) {
        String sql = "update reservations set end_time = current_timestamp where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void payReservation(Integer id) {
        String sql = "update reservations set is_paid = true where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
