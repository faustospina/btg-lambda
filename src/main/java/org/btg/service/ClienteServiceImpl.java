package org.btg.service;

import lombok.RequiredArgsConstructor;
import org.btg.exception.BusinessException;
import org.btg.exception.NotFoundException;
import org.btg.model.documents.Cliente;
import org.btg.model.documents.Transaccion;
import org.btg.model.dto.ClienteDTO;
import org.btg.model.dto.TransaccionDTO;
import org.btg.model.mapper.ClienteMapper;
import org.btg.model.mapper.FondoMapper;
import org.btg.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository repository;

    private final ClienteMapper mapper;

    private final FondoMapper fondoMapper;

    private final FondoService fondoService;

    private final TransaccionService transaccionService;


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

    @Override
    public Mono<ClienteDTO> addFondoCliente(String idCliente, String idFondo) {
        return repository.findById(idCliente)
                .flatMap(cliente ->
                        fondoService.findById(idFondo)
                                .flatMap(fondo -> {
                                    if (cliente.getSaldo() < fondo.montoMinimo()) {
                                        return Mono.error(new BusinessException("No tiene saldo disponible para vincularse al fondo " + fondo.nombre()));
                                    }
                                    cliente.setSaldo(cliente.getSaldo() - fondo.montoMinimo());

                                    if (cliente.getFondos() == null) {
                                        cliente.setFondos(new ArrayList<>());
                                    }
                                    cliente.getFondos().add(fondoMapper.toDocument(fondo));
                                    TransaccionDTO transaccion = new TransaccionDTO(null,idCliente,idFondo,"apertura",LocalDateTime.now(),fondo.montoMinimo());
                                    transaccionService.create(transaccion);
                                    return repository.save(cliente);
                                })
                )
                .map(clienteActualizado -> mapper.toDTO(clienteActualizado))
                .switchIfEmpty(Mono.error(new NotFoundException("not found cliente")));
    }

}
