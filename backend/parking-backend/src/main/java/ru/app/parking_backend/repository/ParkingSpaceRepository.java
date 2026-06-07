package ru.app.parking_backend.repository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.entity.ParkingSpace;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParkingSpaceRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<ParkingSpace> rowMapper = (rs, rowNum) -> new ParkingSpace(
            rs.getInt("id"),
            rs.getString("number_space")
    );

    public List<ParkingSpace> findAll() {
        return jdbc.query("SELECT * FROM parking_spaces ORDER BY number_space", rowMapper);
    }

    public List<ParkingSpace> findAvailable() {
        String sql = "SELECT * FROM parking_spaces ps WHERE ps.id NOT IN (SELECT parking_id FROM reservations WHERE end_time IS NULL) ORDER BY ps.number_space";
        return jdbc.query(sql, rowMapper);
    }

    public void save(ParkingSpace space) {
        jdbc.update("INSERT INTO parking_spaces (number_space) VALUES (?)", space.numberSpace());
    }

    public void update(ParkingSpace space) {
        jdbc.update("UPDATE parking_spaces SET number_space = ? WHERE id = ?", space.numberSpace(), space.id());
    }

    public void delete(Integer id) {
        jdbc.update("DELETE FROM parking_spaces WHERE id = ?", id);
    }
}
