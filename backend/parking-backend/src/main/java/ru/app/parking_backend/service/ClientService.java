package ru.app.parking_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.parking_backend.dto.PageResponse;
import ru.app.parking_backend.entity.Client;
import ru.app.parking_backend.exception.ResourceNotFoundException;
import ru.app.parking_backend.repository.ClientRepository;
import ru.app.parking_backend.repository.ReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final ReservationRepository reservationRepository;

    public PageResponse<Client> findAll(String name, int page, int size) {
        // параметры пагинации
        if (page < 0) page = 0;
        if (size < 10) size = 10;
        if (size > 100) size = 100;

        int offset = page * size;

        List<Client> content;
        long totalElements;

        if (name != null && !name.trim().isEmpty()) {
            String searchPattern = name.trim();
            content = repository.searchByNamePaginated(searchPattern, size, offset);
            totalElements = repository.countByName(searchPattern);
        } else {
            content = repository.findAllPaginated(size, offset);
            totalElements = repository.countAll();
        }

        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new PageResponse<>(
                content,
                totalElements,
                totalPages,
                page,
                size
        );
    }

    public Client findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Клиент с id " + id + " не найден"));
    }

    public Client save(Client client) {
        if (client.id() != null && !repository.existById(client.id())) {
            throw new ResourceNotFoundException("Клиент с id " + client.id() + " не найден");
        }
        return repository.save(client);
    }

    public void delete(Integer id) {
        if (!repository.existById(id)) {
            throw new ResourceNotFoundException("Клиент с id " + id + " не найден");
        }
        if (reservationRepository.hasActiveClient(id)) {
            throw new IllegalStateException("Невозможно удалить клиента: одна или несколько его машин находятся на парковке");
        }
        repository.delete(id);
    }
}
