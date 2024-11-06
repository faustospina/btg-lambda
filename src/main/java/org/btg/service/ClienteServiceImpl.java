package org.btg.service;

import lombok.RequiredArgsConstructor;
import org.btg.exception.NotFoundException;
import org.btg.model.documents.Cliente;
import org.btg.model.dto.ClienteDTO;
import org.btg.model.mapper.ClienteMapper;
import org.btg.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository repository;

    private final ClienteMapper mapper;


    @Override
    public Mono<ClienteDTO> create(ClienteDTO request) {
        Cliente cliente = mapper.toDocument(request);
        return repository.save(cliente).map(mapper::toDTO);
    }

    @Override
    public Mono<ClienteDTO> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("Client not found: " + id)));
    }


    @Override
    public Flux<ClienteDTO> findAll() {
        return repository.findAll()
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("Not found elements")));
    }
}
