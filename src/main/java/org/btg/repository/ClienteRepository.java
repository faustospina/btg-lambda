package org.btg.repository;

import org.btg.model.documents.Cliente;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ClienteRepository extends ReactiveMongoRepository <Cliente,String> {
}
