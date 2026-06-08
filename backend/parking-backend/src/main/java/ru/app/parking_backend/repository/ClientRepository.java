package ru.app.parking_backend.repository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.entity.Client;


import java.sql.PreparedStatement;
import java.sql.Statement;
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
            // здесь мы используем keyholder для получения сгенерированного базой данных id при вставке новой записи
            // мы передаем preparedstatement с флагом который просит вернуть сгенерированные ключи
            // после выполнения запроса мы достаем сгенерированный id из keyholder и создаем новый record
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(connection -> {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO clients (full_name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, client.fullName());
                return ps;
            }, keyHolder);
            int newId = ((Number) keyHolder.getKeys().get("id")).intValue();
            return new Client(newId, client.fullName());
        } else {
            // обновляет запись если она уже существует
            jdbc.update("UPDATE clients SET full_name = ? WHERE id = ?", client.fullName(), client.id());
            return client;
        }
    }
    public void update(Client client) {
        jdbc.update("UPDATE clients SET full_name = ? WHERE id = ?", client.fullName(), client.id());
    }

    public void delete(Integer id) {
        jdbc.update("DELETE FROM clients WHERE id = ?", id);
    }
}

