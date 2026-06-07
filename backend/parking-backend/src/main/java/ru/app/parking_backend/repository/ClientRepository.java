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
    private final JdbcTemplate jdbc;

    private final RowMapper<Client> rowMapper = (rs, rowNum) -> new Client(
            rs.getInt("id"),
            rs.getString("full_name")
    );

    public List<Client> findAll() {
        return jdbc.query("SELECT * FROM clients ORDER BY id DESC", rowMapper);
    }

    public List<Client> searchByName(String name) {
        return jdbc.query("SELECT * FROM clients WHERE full_name ILIKE ? ORDER BY id DESC", rowMapper, "%" + name + "%");
    }

    public void save(Client client) {
        jdbc.update("INSERT INTO clients (full_name) VALUES (?)", client.fullName());
    }

    public void update(Client client) {
        jdbc.update("UPDATE clients SET full_name = ? WHERE id = ?", client.fullName(), client.id());
    }

    public void delete(Integer id) {
        jdbc.update("DELETE FROM clients WHERE id = ?", id);
    }
}

