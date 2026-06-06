package ru.app.parking_backend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.entity.Car;
import ru.app.parking_backend.entity.Reservation;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> carRowMapper = (rs, rowNum) -> new Reservation(
            rs.getInt("id"),
            rs.getInt("parking_space_id"),
            rs.getInt("car_id"),
            rs.getBoolean("is_paid"),
            rs.getObject("start_time", LocalDateTime.class),
            rs.getObject("end_time", LocalDateTime.class)
            );

}
