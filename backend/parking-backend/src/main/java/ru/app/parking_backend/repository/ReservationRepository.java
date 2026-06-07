package ru.app.parking_backend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.dto.ReservationDto;
import ru.app.parking_backend.entity.Reservation;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {
    private final JdbcTemplate jdbc;


    private final RowMapper<ReservationDto> dtoMapper = (rs, rowNum) -> new ReservationDto(
        rs.getInt("id"),
        rs.getInt("parking_id"),
        rs.getString("number_space"),
        rs.getInt("car_id"),
        rs.getString("number_car"),
        rs.getString("full_name"),
        rs.getBoolean("is_paid"),
        rs.getObject("start_time", OffsetDateTime.class),
        rs.getObject("end_time", OffsetDateTime.class)
);

    // Чтение: Получение активных броней в виде DTO
    public List<ReservationDto> findAllActive() {
        String sql = "SELECT r.*, ps.number_space, c.number_car, cl.full_name " +
                "FROM reservations r " +
                "JOIN parking_spaces ps ON r.parking_id = ps.id " +
                "JOIN cars c ON r.car_id = c.id " +
                "LEFT JOIN clients cl ON c.client_id = cl.id " +
                "WHERE r.end_time IS NULL ORDER BY r.start_time DESC";
        return jdbc.query(sql, dtoMapper);
    }

    // Чтение (Поиск): Получение отфильтрованных броней в виде DTO
    public List<ReservationDto> searchActive(String query) {
        String sql = "SELECT r.*, ps.number_space, c.number_car, cl.full_name " +
                "FROM reservations r " +
                "JOIN parking_spaces ps ON r.parking_id = ps.id " +
                "JOIN cars c ON r.car_id = c.id " +
                "LEFT JOIN clients cl ON c.client_id = cl.id " +
                "WHERE r.end_time IS NULL AND (c.number_car ILIKE ? OR cl.full_name ILIKE ?) " +
                "ORDER BY r.start_time DESC";
        String searchParam = "%" + query + "%";
        return jdbc.query(sql, dtoMapper, searchParam, searchParam);
    }

    // Запись: Принимает чистую сущность Reservation
    public void create(Reservation res) {
        jdbc.update("INSERT INTO reservations (parking_id, car_id, is_paid, start_time) VALUES (?, ?, ?, ?)",
                res.parkingId(), res.carId(), res.isPaid(), OffsetDateTime.now());
    }

    public void updatePayment(Integer id, boolean isPaid) {
        jdbc.update("UPDATE reservations SET is_paid = ? WHERE id = ?", isPaid, id);
    }

    public void releaseSpace(Integer id) {
        jdbc.update("UPDATE reservations SET end_time = ? WHERE id = ?", OffsetDateTime.now(), id);
    }
}
