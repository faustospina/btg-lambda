package org.btg.repository;

import org.btg.model.documents.Fondo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FondoRepository extends ReactiveMongoRepository<Fondo,String> {
}
