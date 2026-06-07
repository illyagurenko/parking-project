package ru.app.parking_backend.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.parking_backend.dto.UpdateClientRequest;
import ru.app.parking_backend.entity.Client;
import ru.app.parking_backend.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> findAllClient(){
        return clientRepository.findAllClient();
    }

    public Optional<Client> findClientByFullName(String fullName){
        return clientRepository.findClientByFullName(fullName).stream()
                .findFirst();
    }

    public Client saveClient(Client client){
        return clientRepository.saveClientAndReturn(client);
    }

    public void updateClientName(Integer id, UpdateClientRequest request) {
        clientRepository.updateClientName(id, request.fullName());
    }

    public void deleteClient(Integer id){
        clientRepository.deleteClient(id);
    }
}
