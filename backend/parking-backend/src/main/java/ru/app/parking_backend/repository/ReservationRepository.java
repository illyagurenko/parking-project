package ru.app.parking_backend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> new Reservation(
            rs.getInt("id"),
            rs.getInt("parking_space_id"),
            rs.getInt("car_id"),
            rs.getBoolean("is_paid"),
            rs.getObject("start_time", LocalDateTime.class),
            rs.getObject("end_time", LocalDateTime.class)
            );

    public List<Reservation> findAllReservations(){
        String sql = "select * from reservations";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public List<Reservation> findCarByNumberCarAndFullName(String carNumber, String fullName){
        String sql = """
                        select * from reservations r 
                        join cars c on r.car_id = c.id
                        join clients cl on c.client.id = cl.id
                        join parkins_spaces p on r.parking_space_id = p.id
                        where upper(c.number_car) = ? and upper(cl.full_name) = ?
                """;
        return jdbcTemplate.query(sql, reservationRowMapper, carNumber.toUpperCase(), fullName.toUpperCase());
    }

    public void saveReservation(Integer carId, Integer parkingSpaceId){
        LocalDateTime startTime = LocalDateTime.now();
        String sql = "insert into reservations (car_id, parking_space_id, start_time) values (?, ?, ?)";
        jdbcTemplate.update(sql, carId, parkingSpaceId, startTime);
    }

    public void releaseReservation(Integer id){
        String sql = "update reservations set end_time = current_timestamp where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void payReservation(Integer id){
        String sql = "update reservations set is_paid = true where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
