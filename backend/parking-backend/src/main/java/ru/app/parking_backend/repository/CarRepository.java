package ru.app.parking_backend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.dto.CarDto;
import ru.app.parking_backend.entity.Car;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CarRepository {
    private final JdbcTemplate jdbc;

    // маппер для обычной модели машины
    private final RowMapper<Car> rowMapper = (rs, rowNum) -> new Car(
            rs.getInt("id"),
            rs.getString("number_car"),
            rs.getObject("client_id", Integer.class)
    );

    // маппер DTO
    private final RowMapper<CarDto> dtoMapper = (rs, rowNum) -> new CarDto(
            rs.getInt("id"),
            rs.getString("number_car"),
            rs.getObject("client_id", Integer.class),
            rs.getString("full_name")
    );

    // машина по id
    public Optional<Car> findById(Integer id) {
        List<Car> cars = jdbc.query("SELECT * FROM cars WHERE id = ?", rowMapper, id);
        return cars.stream().findFirst();
    }

    // сохранение/обновление машины
    public Car save(Car car) {
        if (car.id() == null) {
            // делаем вставку и сразу возвращаем сгенерированный id через postgresql RETURNING id
            Integer newId = jdbc.queryForObject(
                    "INSERT INTO cars (number_car, client_id) VALUES (?, ?) RETURNING id",
                    Integer.class,
                    car.numberCar(), car.clientId()
            );
            return new Car(newId, car.numberCar(), car.clientId());
        } else {
            jdbc.update("UPDATE cars SET number_car = ?, client_id = ? WHERE id = ?", car.numberCar(), car.clientId(), car.id());
            return car;
        }
    }

    public void delete(Integer id) {
        jdbc.update("DELETE FROM cars WHERE id = ?", id);
    }

    public boolean existById(Integer id) {
        if (id == null) {
            return false;
        }
        String sql = "SELECT EXISTS(SELECT 1 FROM cars WHERE id = ?)";
        return Boolean.TRUE.equals(jdbc.queryForObject(sql, Boolean.class, id));
    }

    // постраничный вывод всех машин
    public List<CarDto> findAllPaginated(int limit, int offset) {
        String sql = """
                    SELECT c.id, c.number_car, c.client_id, cl.full_name
                    FROM cars c 
                    LEFT JOIN clients cl ON c.client_id = cl.id 
                    ORDER BY c.id DESC 
                    LIMIT ? OFFSET ?
                """;
        return jdbc.query(sql, dtoMapper, limit, offset);
    }

    // подсчет общего количества машин
    public long countAll() {
        String sql = "SELECT COUNT(*) FROM cars";
        Long count = jdbc.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    // постраничный поиск по номеру машины
    public List<CarDto> searchByNumberPaginated(String number, int limit, int offset) {
        String sql = """
                    SELECT c.id, c.number_car, c.client_id, cl.full_name
                    FROM cars c 
                    LEFT JOIN clients cl ON c.client_id = cl.id 
                    WHERE c.number_car ILIKE ? 
                    ORDER BY c.id DESC 
                    LIMIT ? OFFSET ?
                """;
        return jdbc.query(sql, dtoMapper, "%" + number + "%", limit, offset);
    }

    // подсчет количества машин, найденных по номеру
    public long countByNumber(String number) {
        String sql = "SELECT COUNT(*) FROM cars WHERE number_car ILIKE ?";
        Long count = jdbc.queryForObject(sql, Long.class, "%" + number + "%");
        return count != null ? count : 0L;
    }
}
