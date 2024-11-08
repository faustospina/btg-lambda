package org.btg.service;

import lombok.RequiredArgsConstructor;
import org.btg.exception.NotFoundException;
import org.btg.model.documents.Transaccion;
import org.btg.model.dto.TransaccionDTO;
import org.btg.model.dto.TransaccionesResponse;
import org.btg.model.mapper.FondoMapper;
import org.btg.model.mapper.TransaccionMapper;
import org.btg.repository.TransaccionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransaccionServiceImpl implements TransaccionService{

    private final TransaccionRepository repository;

    private final FondoService fondoService;

    private final FondoMapper fondoMapper;

    private final TransaccionMapper mapper;
    @Override
    public Mono<TransaccionDTO> create(TransaccionDTO request) {
        Transaccion transaccion = mapper.toDocument(request);
        return repository.save(transaccion).map(mapper::toDTO);
    }

    @Override
    public Mono<TransaccionDTO> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("Transaccion not found: " + id)));
    }

    @Override
    public Flux<TransaccionDTO> findAll() {
        return repository.findAll()
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("Not found elements transaccion")));
    }

    @Override
    public Flux<TransaccionesResponse> findByClienteId(String idCliente) {
        return repository.findByClienteId(idCliente)
                .flatMap(transaccion ->  fondoService.findById(transaccion.getFondoId())
                            .map(fondo -> mapper.reporte(transaccion, fondoMapper.toDocument(fondo)))
                            .switchIfEmpty(Mono.error(new NotFoundException("Fondo not found for transaccion: " + transaccion.getId())))
                )
                .switchIfEmpty(Mono.error(new NotFoundException("Client not found: " + idCliente)));
    }

}
