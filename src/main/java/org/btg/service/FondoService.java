package org.btg.service;

import org.btg.model.dto.FondoDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FondoService {

    Mono<FondoDTO> create(FondoDTO request);

    Mono<FondoDTO> findById(String id);

    Flux<FondoDTO> findAll();
}
