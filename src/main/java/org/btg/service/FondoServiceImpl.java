package org.btg.service;

import lombok.RequiredArgsConstructor;
import org.btg.exception.NotFoundException;
import org.btg.model.documents.Fondo;
import org.btg.model.dto.FondoDTO;
import org.btg.model.mapper.FondoMapper;
import org.btg.repository.FondoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
@Service
public class FondoServiceImpl implements FondoService{

    private final FondoRepository repository;

    private final FondoMapper mapper;


    @Override
    public Mono<FondoDTO> create(FondoDTO request) {
        Fondo fondo = mapper.toDocument(request);
        return repository.save(fondo).map(mapper::toDTO);
    }

    @Override
    public Mono<FondoDTO> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("Client not found: " + id)));
    }

    @Override
    public Flux<FondoDTO> findAll() {
        return repository.findAll()
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("Not found elements")));
    }
}
