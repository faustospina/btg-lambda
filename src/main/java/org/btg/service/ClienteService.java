package org.btg.service;

import org.btg.model.dto.ClienteDTO;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface ClienteService {

    Mono<ClienteDTO> create(ClienteDTO request);

    Mono<ClienteDTO> findById(String id);

    Flux<ClienteDTO> findAll();


}
