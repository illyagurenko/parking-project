package ru.app.parking_backend.repository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.entity.Car;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CarRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Car> carRowMapper = (rs, rowNum) -> new Car(
            rs.getInt("id"),
            rs.getString("number_car"),
            rs.getInt("client_id")
    );

    public List<Car> findAllCar(){
        String sql = "select * from cars";
        return jdbcTemplate.query(sql, carRowMapper);
    }

    public List<Car> findCarByNumber(String carNumber){
        String sql = "select * from cars where upper(number_car) = ?";
        return jdbcTemplate.query(sql, carRowMapper, carNumber.toUpperCase());
    }

    public void saveCar(Car car){
        String sql = "insert into cars (number_car, client_id) values (?, ?)";
        jdbcTemplate.update(sql, car.numberCar(), car.clientId());
    }

    public void deleteCar(Integer id){
        String sql = "delete from cars where id = ?";
        jdbcTemplate.update(sql, id);
    }
}



