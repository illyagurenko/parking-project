package ru.app.parking_backend.repository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.entity.Car;
import ru.app.parking_backend.entity.Client;

@Repository
@RequiredArgsConstructor
public class ClientRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Client> carRowMapper = (rs, rowNum) -> new Client(
            rs.getInt("id"),
            rs.getString("full_name")
    );
}
