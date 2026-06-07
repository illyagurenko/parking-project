package ru.app.parking_backend.repository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.entity.ParkingSpace;

import java.sql.PreparedStatement;
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
        String sql = "select * from parking_spaces where upper(number_space) = ?";
        return jdbcTemplate.query(sql, parkingSpaceRowMapper, numberSpace.toUpperCase());
    }

    public ParkingSpace saveParkingSpace(ParkingSpace parkingSpace){
        String sql = "insert into parking_spaces (number_space) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, parkingSpace.numberSpace());
            return ps;
        }, keyHolder);
        Integer generatedId = keyHolder.getKey().intValue();
        return new ParkingSpace(generatedId, parkingSpace.numberSpace());
    }

    public void updateParkingSpaceNumber(Integer id, String newParkingSpaceNumber) {
        String sql = "update parking_spaces set number_space = ? where id = ?";
        jdbcTemplate.update(sql, newParkingSpaceNumber, id);
    }

    public void deleteParkingSpace(Integer id){
        String sql = "delete from parking_spaces where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
