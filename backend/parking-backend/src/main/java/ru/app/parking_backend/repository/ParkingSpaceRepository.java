package ru.app.parking_backend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.app.parking_backend.entity.ParkingSpace;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ParkingSpaceRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<ParkingSpace> rowMapper = (rs, rowNum) -> new ParkingSpace(
            rs.getInt("id"),
            rs.getString("number_space")
    );

    // место по id
    public Optional<ParkingSpace> findById(Integer id) {
        List<ParkingSpace> spaces = jdbc.query("SELECT * FROM parking_spaces WHERE id = ?", rowMapper, id);
        return spaces.stream().findFirst();
    }

    // сохранение+обновление
    public ParkingSpace save(ParkingSpace parkingSpace) {
        if (parkingSpace.id() == null) {
            // делаем вставку и сразу возвращаем сгенерированный id через postgresql RETURNING id
            Integer newId = jdbc.queryForObject(
                    "INSERT INTO parking_spaces (number_space) VALUES (?) RETURNING id",
                    Integer.class,
                    parkingSpace.numberSpace()
            );
            return new ParkingSpace(newId, parkingSpace.numberSpace());
        } else {
            jdbc.update("UPDATE parking_spaces SET number_space = ? WHERE id = ?", parkingSpace.numberSpace(), parkingSpace.id());
            return parkingSpace;
        }
    }

    // удаление
    public void delete(Integer id) {
        jdbc.update("DELETE FROM parking_spaces WHERE id = ?", id);
    }

    //существует ли сущность+если npe метод не упадет
    public boolean existById(Integer id) {
        if (id == null) {
            return false;
        }
        String sql = "SELECT EXISTS(SELECT 1 FROM parking_spaces WHERE id = ?)";
        return Boolean.TRUE.equals(jdbc.queryForObject(sql, Boolean.class, id));
    }

    // постраничный вывод всех парковочных мест
    public List<ParkingSpace> findAllPaginated(int limit, int offset) {
        String sql = "SELECT * FROM parking_spaces ORDER BY id DESC LIMIT ? OFFSET ?";
        return jdbc.query(sql, rowMapper, limit, offset);
    }

    // подсчет общего количества мест
    public long countAll() {
        String sql = "SELECT COUNT(*) FROM parking_spaces";
        Long count = jdbc.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    // постраничный поиск парковочного места по его номеру
    public List<ParkingSpace> searchByNumberPaginated(String numberSpace, int limit, int offset) {
        String sql = "SELECT * FROM parking_spaces WHERE number_space ILIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
        return jdbc.query(sql, rowMapper, "%" + numberSpace + "%", limit, offset);
    }

    // подсчет количества мест, найденных по номеру
    public long countByNumber(String numberSpace) {
        String sql = "SELECT COUNT(*) FROM parking_spaces WHERE number_space ILIKE ?";
        Long count = jdbc.queryForObject(sql, Long.class, "%" + numberSpace + "%");
        return count != null ? count : 0L;
    }
}
