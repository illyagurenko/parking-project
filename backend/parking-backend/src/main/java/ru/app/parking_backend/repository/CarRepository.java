package ru.app.parking_backend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.dto.CarDto;
import ru.app.parking_backend.entity.Car;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class CarRepository {
    private final JdbcTemplate jdbc;

    // маппер JOIN в DTO
    private final RowMapper<CarDto> dtoMapper = (rs, rowNum) -> new CarDto(
            rs.getInt("id"),
            rs.getString("number_car"),
            rs.getObject("client_id", Integer.class),
            rs.getString("full_name")
    );

    // владельцы+авто
    public List<CarDto> findAllCarWithClient() {
        String sql = "SELECT c.*, cl.full_name FROM cars c " +
                "LEFT JOIN clients cl ON c.client_id = cl.id ORDER BY c.id DESC";
        return jdbc.query(sql, dtoMapper);
    }

    // поиск машины с владельцем по номеру
    public List<CarDto> searchCarByNumber(String number) {
        String sql = "SELECT c.*, cl.full_name FROM cars c " +
                "LEFT JOIN clients cl ON c.client_id = cl.id " +
                "WHERE c.number_car ILIKE ? ORDER BY c.id DESC";
        return jdbc.query(sql, dtoMapper, "%" + number + "%");
    }

    public void save(Car car) {
        jdbc.update("INSERT INTO cars (number_car, client_id) VALUES (?, ?)",
                car.numberCar(), car.clientId());
    }

    public void update(Car car) {
        jdbc.update("UPDATE cars SET number_car = ?, client_id = ? WHERE id = ?",
                car.numberCar(), car.clientId(), car.id());
    }

    public void delete(Integer id) {
        jdbc.update("DELETE FROM cars WHERE id = ?", id);
    }
}
