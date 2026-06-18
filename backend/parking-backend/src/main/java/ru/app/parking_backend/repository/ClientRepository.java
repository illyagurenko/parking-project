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

    // клиент по id
    public Optional<Client> findById(Integer id) {
        List<Client> clients = jdbc.query("SELECT * FROM clients WHERE id = ?", rowMapper, id);
        return clients.stream().findFirst();
    }

    // сохранение+обновление
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
            jdbc.update("UPDATE clients SET full_name = ? WHERE id = ?", client.fullName(), client.id());
            return client;
        }
    }

    public void delete(Integer id) {
        jdbc.update("DELETE FROM clients WHERE id = ?", id);
    }

    //существует ли сущность+если npe метод не упадет
    public boolean existById(Integer id) {
        if (id == null) {
            return false;
        }
        String sql = "SELECT EXISTS(SELECT 1 FROM clients WHERE id = ?)";
        return Boolean.TRUE.equals(jdbc.queryForObject(sql, Boolean.class, id));
    }

    // постраничный вывод всех клиентов
    public List<Client> findAllPaginated(int limit, int offset) {
        String sql = "SELECT * FROM clients ORDER BY id DESC LIMIT ? OFFSET ?";
        return jdbc.query(sql, rowMapper, limit, offset);
    }

    // постраничный поиск по имени
    public List<Client> searchByNamePaginated(String name, int limit, int offset) {
        String sql = "SELECT * FROM clients WHERE full_name ILIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
        return jdbc.query(sql, rowMapper, "%" + name + "%", limit, offset);
    }

    // Подсчет количества найденных по имени для вычисления страниц поиска
    public long countByName(String name) {
        String sql = "SELECT COUNT(*) FROM clients WHERE full_name ILIKE ?";
        Long count = jdbc.queryForObject(sql, Long.class, "%" + name + "%");
        return count != null ? count : 0L;
    }

    // метод для подсчета всех записей
    public long countAll() {
        String sql = "SELECT COUNT(*) FROM clients";
        Long count = jdbc.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }
}

