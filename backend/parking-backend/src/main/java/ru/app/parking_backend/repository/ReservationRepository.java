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
                rs.getString("number_space"),
                rs.getInt("car_id"),
                rs.getString("number_car"),
                rs.getInt("client_id"),
                rs.getString("full_name"),
                rs.getBoolean("is_paid"),
                startTs != null ? OffsetDateTime.ofInstant(startTs.toInstant(), ZoneId.systemDefault()) : null,
                endTs != null ? OffsetDateTime.ofInstant(endTs.toInstant(), ZoneId.systemDefault()) : null
        );
    };

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

    // проверка для конкретной машины
    public boolean hasActiveCar(Integer carId) {
        String sql = """
                    SELECT EXISTS(
                        SELECT 1 FROM reservations 
                        WHERE car_id = ? 
                        AND (end_time IS NULL OR end_time > CURRENT_TIMESTAMP)
                    )
                """;
        return Boolean.TRUE.equals(jdbc.queryForObject(sql, Boolean.class, carId));
    }

    // проверка для клиента
    public boolean hasActiveClient(Integer clientId) {
        String sql = """
                    SELECT EXISTS(
                        SELECT 1 FROM reservations r
                        JOIN cars c ON r.car_id = c.id
                        WHERE c.client_id = ? 
                        AND (r.end_time IS NULL OR r.end_time > CURRENT_TIMESTAMP)
                    )
                """;
        return Boolean.TRUE.equals(jdbc.queryForObject(sql, Boolean.class, clientId));
    }

    public boolean hasActiveSpace(Integer parkingId) {
        String sql = """
                    SELECT EXISTS(
                        SELECT 1 FROM reservations 
                        WHERE parking_id = ? 
                        AND (end_time IS NULL OR end_time > CURRENT_TIMESTAMP)
                    )
                """;
        return Boolean.TRUE.equals(jdbc.queryForObject(sql, Boolean.class, parkingId));
    }

    public List<ReservationDto> findAllPaginated(int limit, int offset) {
        String sql = """
                            SELECT r.id, r.car_id, r.parking_id, r.start_time, r.end_time, r.is_paid,\s
                                   c.number_car, c.client_id, cl.full_name, p.number_space
                            FROM reservations r
                            JOIN cars c ON r.car_id = c.id
                            JOIN clients cl ON c.client_id = cl.id
                            JOIN parking_spaces p ON r.parking_id = p.id
                            ORDER BY r.id DESC\s
                            LIMIT ? OFFSET ?
                """;
        return jdbc.query(sql, dtoMapper, limit, offset);
    }

    // общий подсчет бронирований
    public long countAll() {
        String sql = "SELECT COUNT(*) FROM reservations";
        Long count = jdbc.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    // постраничный гибкий поиск по двум фильтрам (можно заполнить только один или оба)
    public List<ReservationDto> searchPaginated(String carNumber, String clientFullName, int limit, int offset) {
        String sql = """
                    SELECT r.id, r.car_id, r.parking_id, r.start_time, r.end_time, r.is_paid,\s 
                           c.number_car, c.client_id, cl.full_name, p.number_space
                    FROM reservations r
                    JOIN cars c ON r.car_id = c.id
                    JOIN clients cl ON c.client_id = cl.id
                    JOIN parking_spaces p ON r.parking_id = p.id
                    WHERE (?::text IS NULL OR c.number_car ILIKE ?)
                      AND (?::text IS NULL OR cl.full_name ILIKE ?)
                    ORDER BY r.id DESC 
                    LIMIT ? OFFSET ?
                """;

        // параметры для LIKE
        String carPattern = carNumber != null ? "%" + carNumber + "%" : null;
        String clientPattern = clientFullName != null ? "%" + clientFullName + "%" : null;

        return jdbc.query(sql, dtoMapper,
                carNumber, carPattern,
                clientFullName, clientPattern,
                limit, offset);
    }

    // подсчет количества записей для гибкого поиска
    public long countSearch(String carNumber, String clientFullName) {
        String sql = """
                    SELECT COUNT(*) 
                    FROM reservations r
                    JOIN cars c ON r.car_id = c.id
                    JOIN clients cl ON c.client_id = cl.id
                    WHERE (?::text IS NULL OR c.number_car ILIKE ?)
                      AND (?::text IS NULL OR cl.full_name ILIKE ?)
                """;

        String carPattern = carNumber != null ? "%" + carNumber + "%" : null;
        String clientPattern = clientFullName != null ? "%" + clientFullName + "%" : null;

        Long count = jdbc.queryForObject(sql, Long.class,
                carNumber, carPattern,
                clientFullName, clientPattern);
        return count != null ? count : 0L;
    }
}
