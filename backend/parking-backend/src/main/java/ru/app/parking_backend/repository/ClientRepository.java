package ru.app.parking_backend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.entity.Client;

import java.util.List;
import java.util.Optional;

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

    public Optional<Client> findById(Integer id) {
        List<Client> clients = jdbc.query("SELECT * FROM clients WHERE id = ?", rowMapper, id);
        return clients.stream().findFirst();
    }

    public List<Client> searchByName(String name) {
        return jdbc.query("SELECT * FROM clients WHERE full_name ILIKE ? ORDER BY id DESC", rowMapper, "%" + name + "%");
    }

    public Client save(Client client) {
        if (client.id() == null) {
            // делаем вставку и сразу возвращаем сгенерированный id через postgresql RETURNING id
            Integer newId = jdbc.queryForObject(
                    "INSERT INTO clients (full_name) VALUES (?) RETURNING id",
                    Integer.class,
                    client.fullName()
            );
            return new Client(newId, client.fullName());
        } else {
            int rowsAffected = jdbc.update("UPDATE clients SET full_name = ? WHERE id = ?", client.fullName(), client.id());
            if (rowsAffected == 0) {
                throw new RuntimeException("Клиент с id " + client.id() + " не найден");
            }
            return client;
        }
    }

    public void delete(Integer id) {
        int rowsAffected = jdbc.update("DELETE FROM clients WHERE id = ?", id);
        if (rowsAffected == 0) {
            throw new RuntimeException("Клиент с id " + id + " не найден");
        }
    }

    public boolean existById(Integer id) {
        if (id == null) {
            return false;
        }
        String sql = "SELECT EXISTS(SELECT 1 FROM clients WHERE id = ?)";
        return Boolean.TRUE.equals(jdbc.queryForObject(sql, Boolean.class, id));
    }
}

