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

    private final ClientRepository clientRepository;

    public List<Client> findAll(String name) {
        if (name != null && !name.trim().isEmpty()) {
            return clientRepository.searchByName(name);
        }
        return clientRepository.findAll();
    }

    public void create(Client client) {
        clientRepository.save(client);
    }

    public void update(Integer id, Client client) {
        Client upClient = new Client(id, client.fullName());
        clientRepository.update(upClient);
    }

    public void delete(Integer id) {
        clientRepository.delete(id);
    }
}
