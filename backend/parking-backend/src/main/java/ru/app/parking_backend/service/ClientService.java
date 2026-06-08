package ru.app.parking_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.parking_backend.entity.Client;
import ru.app.parking_backend.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;

    // эта функция возвращает всех клиентов или ищет по имени если есть параметр
    public List<Client> getAllClients(String name) {
        if (name != null && !name.trim().isEmpty()) {
            return repository.searchByName(name);
        }
        return repository.findAll();
    }

    public Optional<Client> getClientById(Integer id) {
        return repository.findById(id);
    }
    public Client saveClient(Client client) {
        return repository.save(client);
    }


    public void delete(Integer id) {
        repository.delete(id);
    }
}
