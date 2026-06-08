package ru.app.parking_backend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.dto.CarDto;
import ru.app.parking_backend.entity.Car;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
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
            rs.getObject("client_id") != null ? rs.getInt("client_id") : null
    );

    // маппер DTO
    private final RowMapper<CarDto> dtoMapper = (rs, rowNum) -> new CarDto(
            rs.getInt("id"),
            rs.getString("number_car"),
            rs.getObject("client_id", Integer.class),
            rs.getString("full_name")
    );

    // владельцы+авто
    public List<CarDto> findAll() {
        String sql = "SELECT c.*, cl.full_name FROM cars c " +
                "LEFT JOIN clients cl ON c.client_id = cl.id ORDER BY c.id DESC";
        return jdbc.query(sql, dtoMapper);
    }

    // ищет машину по id
    public Optional<Car> findById(Integer id) {
        List<Car> cars = jdbc.query("SELECT * FROM cars WHERE id = ?", rowMapper, id);
        return cars.stream().findFirst();
    }

    // ищет машину по номеру используя частичное совпадение
    public List<CarDto> searchByNumber(String numberCar) {
        String sql = "SELECT c.*, cl.full_name as client_full_name FROM cars c LEFT JOIN clients cl ON c.client_id = cl.id WHERE c.number_car ILIKE ?";
        return jdbc.query(sql, dtoMapper, "%" + numberCar + "%");
    }

    // поиск машины с владельцем по номеру
// сохраняет новую или обновляет существующую машину
    public Car save(Car car) {
        if (car.id() == null) {
            // keyholder нужен чтобы после вставки получить id который база сама сгенерировала
            // мы используем preparedstatement с указанием что нам нужны ключи
            // база возвращает ключи и мы сохраняем их в keyholder
            // потом достаем id и ставим его объекту машины
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(connection -> {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO cars (number_car, client_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, car.numberCar());
                if (car.clientId() != null) {
                    ps.setLong(2, car.clientId());
                } else {
                    ps.setNull(2, Types.INTEGER);
                }
                return ps;
            }, keyHolder);
            int newId = ((Number) keyHolder.getKeys().get("id")).intValue();
            return new Car(newId, car.numberCar(), car.clientId());
        } else {
            // делает запрос на обновление записи если id уже есть
            jdbc.update("UPDATE cars SET number_car = ?, client_id = ? WHERE id = ?",
                    car.numberCar(), car.clientId(), car.id());
            return car;
        }
    }


    public void delete(Integer id) {
        jdbc.update("DELETE FROM cars WHERE id = ?", id);
    }
}
