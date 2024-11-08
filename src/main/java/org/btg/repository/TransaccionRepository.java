package org.btg.repository;

import org.btg.model.documents.Transaccion;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransaccionRepository extends ReactiveMongoRepository<Transaccion,String> {
    Flux<Transaccion> findByClienteId(String clienteId);
}
