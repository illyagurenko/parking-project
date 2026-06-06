package ru.app.parking_backend.repository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.entity.Car;
import ru.app.parking_backend.entity.Client;
import ru.app.parking_backend.entity.ParkingSpace;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParkingSpaceRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ParkingSpace> parkingSpaceRowMapper = (rs, rowNum) -> new ParkingSpace(
            rs.getInt("id"),
            rs.getString("number_space")
    );

    public List<ParkingSpace> findAllParkingSpace(){
        String sql = "select * from parking_spaces";
        return jdbcTemplate.query(sql, parkingSpaceRowMapper);
    }

    public List<ParkingSpace> findParkingSpaceByNumber(String numberSpace){
        String sql = "select * from parking_spaces where upper(full_name) = ?";
        return jdbcTemplate.query(sql, parkingSpaceRowMapper, numberSpace.toUpperCase());
    }

    public void saveParkingSpace(ParkingSpace parkingSpace){
        String sql = "insert into parking_spaces (number_space) values (?)";
        jdbcTemplate.update(sql, parkingSpace.numberSpace());
    }

    public void deleteParkingSpace(Integer id){
        String sql = "delete from parking_spaces where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
