package ru.app.parking_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.parking_backend.entity.Client;
import ru.app.parking_backend.exception.ResourceNotFoundException;
import ru.app.parking_backend.repository.ClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;

    // эта функция возвращает всех клиентов или ищет по имени если есть параметр
    public List<Client> findAll(String name) {
        if (name != null && !name.trim().isEmpty()) {
            return repository.searchByName(name);
        }
        return repository.findAll();
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
        repository.delete(id);
    }
}
