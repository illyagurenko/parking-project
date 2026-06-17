package ru.app.parking_backend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.dto.ReservationDto;
import ru.app.parking_backend.entity.Reservation;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<Reservation> rowMapper = (rs, rowNum) -> {
        Timestamp startTs = rs.getTimestamp("start_time");
        Timestamp endTs = rs.getTimestamp("end_time");
        return new Reservation(
                rs.getInt("id"),
                rs.getInt("parking_id"),
                rs.getInt("car_id"),
                rs.getBoolean("is_paid"),
                startTs != null ? OffsetDateTime.ofInstant(startTs.toInstant(), ZoneId.systemDefault()) : null,
                endTs != null ? OffsetDateTime.ofInstant(endTs.toInstant(), ZoneId.systemDefault()) : null
        );
    };

    private final RowMapper<ReservationDto> dtoMapper = (rs, rowNum) -> {
        Timestamp startTs = rs.getTimestamp("start_time");
        Timestamp endTs = rs.getTimestamp("end_time");
        return new ReservationDto(
                rs.getInt("id"),
                rs.getInt("parking_id"),
                rs.getString("parking_number"),
                rs.getInt("car_id"),
                rs.getString("car_number"),
                rs.getInt("client_id"),
                rs.getString("client_full_name"),
                rs.getBoolean("is_paid"),
                startTs != null ? OffsetDateTime.ofInstant(startTs.toInstant(), ZoneId.systemDefault()) : null,
                endTs != null ? OffsetDateTime.ofInstant(endTs.toInstant(), ZoneId.systemDefault()) : null
        );
    };

    // запрос для получения всех броней со связанными данными
    public List<ReservationDto> findAll() {
        String sql = "SELECT r.*, p.number_space as parking_number, c.number_car as car_number, c.client_id as client_id, cl.full_name as client_full_name " +
                "FROM reservations r " +
                "JOIN parking_spaces p ON r.parking_id = p.id " +
                "JOIN cars c ON r.car_id = c.id " +
                "LEFT JOIN clients cl ON c.client_id = cl.id " +
                "ORDER BY r.id DESC";
        return jdbc.query(sql, dtoMapper);
    }

    // запрос поиска по номеру и владельцу
    public List<ReservationDto> search(String carNumber, String clientFullName) {
        String sql = "SELECT r.*, p.number_space as parking_number, c.number_car as car_number, c.client_id as client_id, cl.full_name as client_full_name " +
                "FROM reservations r " +
                "JOIN parking_spaces p ON r.parking_id = p.id " +
                "JOIN cars c ON r.car_id = c.id " +
                "LEFT JOIN clients cl ON c.client_id = cl.id " +
                "WHERE 1=1 ";

        if (carNumber != null && !carNumber.isEmpty()) {
            sql += " AND c.number_car ILIKE '%" + carNumber + "%' ";
        }
        if (clientFullName != null && !clientFullName.isEmpty()) {
            sql += " AND cl.full_name ILIKE '%" + clientFullName + "%' ";
        }
        sql += " ORDER BY r.id";
        return jdbc.query(sql, dtoMapper);
    }

    public Optional<Reservation> findById(Integer id) {
        List<Reservation> list = jdbc.query("SELECT * FROM reservations WHERE id = ?", rowMapper, id);
        return list.stream().findFirst();
    }

    // сохраняет бронь
    public Reservation save(Reservation res) {
        if (res.id() == null) {
            OffsetDateTime startTime = res.startTime() != null ? res.startTime() : OffsetDateTime.now(ZoneId.systemDefault());
            Boolean isPaid = res.isPaid() != null ? res.isPaid() : false;

            Integer newId = jdbc.queryForObject(
                    "INSERT INTO reservations (parking_id, car_id, is_paid, start_time, end_time) VALUES (?, ?, ?, ?, ?) RETURNING id",
                    Integer.class,
                    res.parkingId(),
                    res.carId(),
                    isPaid,
                    Timestamp.from(startTime.toInstant()),
                    res.endTime() != null ? Timestamp.from(res.endTime().toInstant()) : null
            );
            return new Reservation(newId, res.parkingId(), res.carId(), isPaid, startTime, res.endTime());
        } else {
            jdbc.update(
                    "UPDATE reservations SET parking_id = ?, car_id = ?, is_paid = ?, start_time = ?, end_time = ? WHERE id = ?",
                    res.parkingId(),
                    res.carId(),
                    res.isPaid(),
                    res.startTime() != null ? Timestamp.from(res.startTime().toInstant()) : null,
                    res.endTime() != null ? Timestamp.from(res.endTime().toInstant()) : null,
                    res.id()
            );
            return res;
        }
    }

    public void delete(Integer id) {
        jdbc.update("DELETE FROM reservations WHERE id = ?", id);
    }

    public boolean existById(Integer id) {
        if (id == null) {
            return false;
        }
        String sql = "SELECT EXISTS(SELECT 1 FROM reservations WHERE id = ?)";
        return Boolean.TRUE.equals(jdbc.queryForObject(sql, Boolean.class, id));
    }
}
