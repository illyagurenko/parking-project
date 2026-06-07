package ru.app.parking_backend.repository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.entity.Client;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClientRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Client> clientRowMapper = (rs, rowNum) -> new Client(
            rs.getInt("id"),
            rs.getString("full_name")
    );

    public List<Client> findAllClient(){
        String sql = "select * from clients";
        return jdbcTemplate.query(sql, clientRowMapper);
    }

    public List<Client> findClientByFullName(String fullName){
        String sql = "select * from clients where upper(full_name) = ?";
        return jdbcTemplate.query(sql, clientRowMapper, fullName.toUpperCase());
    }

    public void saveClient(Client client){
        String sql = "insert into clients (full_name) values (?)";
        jdbcTemplate.update(sql, client.fullName());
    }

    public void updateClientName(Integer id, String newFullName) {
        String sql = "update clients set full_name = ? where id = ?";
        jdbcTemplate.update(sql, newFullName, id);
    }

    public void deleteClient(Integer id){
        String sql = "delete from clients where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
