package org.btg.service;

import org.btg.model.dto.TransaccionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransaccionService {
    Mono<TransaccionDTO> create(TransaccionDTO request);

    Mono<TransaccionDTO> findById(String id);

    Flux<TransaccionDTO> findAll();

    Flux<TransaccionDTO> findByClienteId(String idCliente);
}
