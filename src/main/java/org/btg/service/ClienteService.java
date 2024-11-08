package org.btg.service;

import org.btg.model.dto.ClienteDTO;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.Set;

public interface ClienteService {

    Mono<ClienteDTO> create(ClienteDTO request);

    Mono<ClienteDTO> findById(String id);

    Flux<ClienteDTO> findAll();

    Mono<ClienteDTO> addFondosCliente(String idCliente, Set<String> idsFondos);

    Mono<ClienteDTO> removeFondosCliente(String idCliente, Set<String> idsFondos);


}
