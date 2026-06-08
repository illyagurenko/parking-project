package ru.app.parking_backend.repository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.entity.ParkingSpace;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

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

    public Optional<ParkingSpace> findById(Integer id) {
        List<ParkingSpace> spaces = jdbc.query("SELECT * FROM parking_spaces WHERE id = ?", rowMapper, id);
        return spaces.stream().findFirst();
    }

    public ParkingSpace save(ParkingSpace parkingSpace) {
        if (parkingSpace.id() == null) {
            // делаем вставку и сразу возвращаем сгенерированный id через postgresql RETURNING id
            Integer newId = jdbc.queryForObject(
                    "INSERT INTO parking_spaces (number_space) VALUES (?) RETURNING id",
                    Integer.class,
                    parkingSpace.numberSpace()
            );
            return new ParkingSpace(newId, parkingSpace.numberSpace());
        } else {
            // обновляет место
            jdbc.update("UPDATE parking_spaces SET number_space = ? WHERE id = ?", parkingSpace.numberSpace(), parkingSpace.id());
            return parkingSpace;
        }
    }

    public void delete(Integer id) {
        jdbc.update("DELETE FROM parking_spaces WHERE id = ?", id);
    }
}
