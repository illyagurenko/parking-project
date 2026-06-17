package ru.app.parking_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.app.parking_backend.dto.PageResponse;
import ru.app.parking_backend.entity.Client;
import ru.app.parking_backend.service.ClientService;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClientController {
    private final ClientService service;

    @GetMapping
    public PageResponse<Client> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.findAll(name, page, size);
    }

    @GetMapping("/{id}")
    public Client findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public Client save(@Valid @RequestBody Client client) {
        return service.save(client);
    }

    @PutMapping("/{id}")
    public Client update(@PathVariable Integer id, @Valid @RequestBody Client client) {
        Client updatedClient = new Client(id, client.fullName());
        return service.save(updatedClient);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
