package ru.app.parking_backend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.dto.CarWithClientDTO;
import ru.app.parking_backend.entity.Car;

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
            rs.getInt("client_id"),
            rs.getString("client_full_name")
    );

//    public List<Car> findAllCar(){
//        String sql = "select * from cars";
//        return jdbcTemplate.query(sql, carRowMapper);
//    }

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
        jdbcTemplate.update(sql, car.numberCar(), car.clientId());
        return car;
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
                WHERE c.number_car = ?
                """;
        return jdbcTemplate.query(sql, carWithClientDTO, numberCar);
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
}



