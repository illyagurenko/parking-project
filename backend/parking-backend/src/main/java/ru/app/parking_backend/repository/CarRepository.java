package ru.app.parking_backend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.dto.CarWithClientDTO;
import ru.app.parking_backend.entity.Car;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CarRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Car> carRowMapper = (rs, rowNum) -> new Car(
            rs.getInt("id"),
            rs.getString("number_car"),
            rs.getObject("client_id", Integer.class)
    );

    private final RowMapper<CarWithClientDTO> carWithClientDTO = (rs, rowNum) -> new CarWithClientDTO(
            rs.getInt("id"),
            rs.getString("number_car"),
            rs.getObject("client_id", Integer.class),
            rs.getString("client_full_name")
    );

    public List<Car> findCarByNumber(String carNumber){
        String sql = "select * from cars where upper(number_car) = ?";
        return jdbcTemplate.query(sql, carRowMapper, carNumber.toUpperCase());
    }

    public List<Car> findCarById(Integer id) {
        String sql = "select * from cars where id = ?";
        return jdbcTemplate.query(sql, carRowMapper, id);
    }

    public Car saveCar(Car car) {
        String sql = "insert into cars (number_car, client_id) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, car.numberCar());
            ps.setObject(2, car.clientId());
            return ps;
        }, keyHolder);
        Integer generatedId = keyHolder.getKey().intValue();
        return new Car(generatedId, car.numberCar(), car.clientId());
    }

    public void updateCarNumber(Integer id, String newCarNumber) {
        String sql = "update cars set number_car = ? where id = ?";
        jdbcTemplate.update(sql, newCarNumber, id);
    }

    public void deleteCar(Integer id) {
        String sql = "delete from cars where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<CarWithClientDTO> findAllWithClient() {
        String sql = """
                SELECT c.id, c.number_car, c.client_id,
                       cl.full_name AS client_full_name
                FROM cars c
                LEFT JOIN clients cl ON c.client_id = cl.id
                ORDER BY c.id
                """;
        return jdbcTemplate.query(sql, carWithClientDTO);
    }

    public List<CarWithClientDTO> findByNumberCarWithClient(String numberCar) {
        String sql = """
                SELECT c.id, c.number_car, c.client_id, cl.full_name AS client_full_name
                FROM cars c
                LEFT JOIN clients cl ON c.client_id = cl.id
                WHERE upper(c.number_car) = ?
                """;
        return jdbcTemplate.query(sql, carWithClientDTO, numberCar.toUpperCase());
    }

    public List<CarWithClientDTO> findByClientId(Integer clientId) {
        String sql = """
                SELECT c.id, c.number_car, c.client_id, cl.full_name AS client_full_name
                FROM cars c
                LEFT JOIN clients cl ON c.client_id = cl.id
                WHERE c.client_id = ?
                ORDER BY c.id
                """;
        return jdbcTemplate.query(sql, carWithClientDTO, clientId);
    }

    public List<CarWithClientDTO> findByClientName(String clientName) {
        String sql = """
                SELECT c.id, c.number_car, c.client_id, cl.full_name AS client_full_name
                FROM cars c
                LEFT JOIN clients cl ON c.client_id = cl.id
                WHERE upper(cl.full_name) like ?
                ORDER BY c.id
                """;
        return jdbcTemplate.query(sql, carWithClientDTO, "%" + clientName.toUpperCase() + "%");
    }
}
