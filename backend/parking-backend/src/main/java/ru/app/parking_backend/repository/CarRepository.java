package ru.app.parking_backend.repository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.entity.Car;

@Repository
@RequiredArgsConstructor
public class CarRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Car> carRowMapper = (rs, rowNum) -> new Car(
            rs.getInt("id"),
            rs.getString("number_car"),
            rs.getInt("clientId")
    );


}



