package ru.app.parking_backend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.dto.ReservationDto;
import ru.app.parking_backend.entity.Reservation;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
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

    // делает запрос для получения всех броней со связанными данными
    public List<ReservationDto> findAll() {
        String sql = "SELECT r.*, p.number_space as parking_number, c.number_car as car_number, c.client_id as client_id, cl.full_name as client_full_name " +
                "FROM reservations r " +
                "JOIN parking_spaces p ON r.parking_id = p.id " +
                "JOIN cars c ON r.car_id = c.id " +
                "LEFT JOIN clients cl ON c.client_id = cl.id " +
                "ORDER BY r.id";
        return jdbc.query(sql, dtoMapper);
    }

    // выполняет динамический запрос поиска по номеру и владельцу
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

    // запрашивает бронь по id
    public Optional<Reservation> findById(Integer id) {
        List<Reservation> list = jdbc.query("SELECT * FROM reservations WHERE id = ?", rowMapper, id);
        return list.stream().findFirst();
    }

    // сохраняет бронь
    public Reservation save(Reservation res) {
        if (res.id() == null) {
            // keyholder это контейнер куда драйвер базы данных сложит сгенерированный id
            // он нужен чтобы после создания записи сразу вернуть клиенту объект с уже заполненным id
            // без него мы бы не знали под каким id сохранилась запись
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO reservations (parking_id, car_id, is_paid, start_time, end_time) VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, res.parkingId());
                ps.setLong(2, res.carId());
                ps.setBoolean(3, res.isPaid() != null ? res.isPaid() : false);
                ps.setTimestamp(4, res.startTime() != null ? Timestamp.from(res.startTime().toInstant()) : new Timestamp(System.currentTimeMillis()));
                if (res.endTime() != null) {
                    ps.setTimestamp(5, Timestamp.from(res.endTime().toInstant()));
                } else {
                    ps.setNull(5, Types.TIMESTAMP);
                }
                return ps;
            }, keyHolder);
            int newId = ((Number) keyHolder.getKeys().get("id")).intValue();
            return new Reservation(newId, res.parkingId(), res.carId(), res.isPaid(), res.startTime(), res.endTime());
        } else {
            // выполняет запрос на обновление
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

    // удаляет бронь
    public void delete(Integer id) {
        jdbc.update("DELETE FROM reservations WHERE id = ?", id);
    }
}
