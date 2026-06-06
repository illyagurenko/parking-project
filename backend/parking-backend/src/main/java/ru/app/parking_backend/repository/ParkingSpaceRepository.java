package ru.app.parking_backend.repository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.entity.Car;
import ru.app.parking_backend.entity.ParkingSpace;

@Repository
@RequiredArgsConstructor
public class ParkingSpaceRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ParkingSpace> carRowMapper = (rs, rowNum) -> new ParkingSpace(
            rs.getInt("id"),
            rs.getString("number_space")
    );
}
