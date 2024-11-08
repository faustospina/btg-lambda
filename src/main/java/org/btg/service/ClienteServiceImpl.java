package org.btg.service;

import lombok.RequiredArgsConstructor;
import org.btg.exception.BusinessException;
import org.btg.exception.NotFoundException;
import org.btg.model.documents.Cliente;
import org.btg.model.dto.ClienteDTO;
import org.btg.model.dto.TipoTopic;
import org.btg.model.dto.TransaccionDTO;
import org.btg.model.mapper.ClienteMapper;
import org.btg.model.mapper.FondoMapper;
import org.btg.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository repository;

    private final ClienteMapper mapper;

    private final FondoMapper fondoMapper;

    private final FondoService fondoService;

    private final TransaccionService transaccionService;

    private final SnsTopicService snsTopicService;


    @Override
    public Mono<ClienteDTO> create(ClienteDTO request) {
        if (request.preferenciaNotificacion().equals(TipoTopic.email)) {
            Cliente cliente = mapper.toDocument(request);
            snsTopicService.addSubscription(request.email(), TipoTopic.email.name());
            return repository.save(cliente).map(mapper::toDTO);
        } else {
            if (isValidPhoneNumber(request.telefono())) {
                Cliente cliente = mapper.toDocument(request);
                snsTopicService.addSubscription(request.telefono(), TipoTopic.sms.name());
                return repository.save(cliente).map(mapper::toDTO);
            } else {
                return Mono.error(new BusinessException("Número de teléfono inválido. Debe ser un número válido en formato E.164."));
            }
        }
    }


    private  boolean isValidPhoneNumber(String phoneNumber) {
        String PHONE_REGEX = "^\\+[1-9]{1}[0-9]{10,14}$";
        return Pattern.matches(PHONE_REGEX, phoneNumber);
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

    @Transactional
    @Override
    public Mono<ClienteDTO> addFondosCliente(String idCliente, Set<String> idsFondos) {
        return repository.findById(idCliente)
                .flatMap(cliente -> {
                    if (cliente.getFondos() == null) {
                        cliente.setFondos(new HashSet<>());
                    }
                    return Flux.fromIterable(idsFondos)
                            .flatMap(idFondo -> fondoService.findById(idFondo)
                                    .flatMap(fondo -> {
                                        if (cliente.getSaldo() < fondo.montoMinimo()) {
                                            return Mono.error(new BusinessException(
                                                    "No tiene saldo disponible para vincularse al fondo " + fondo.nombre()));
                                        }
                                        cliente.setSaldo(cliente.getSaldo() - fondo.montoMinimo());
                                        cliente.getFondos().add(fondoMapper.toDocument(fondo));

                                        TransaccionDTO transaccion = new TransaccionDTO(
                                                null, idCliente, idFondo, "Apertura",
                                                LocalDateTime.now(), fondo.montoMinimo());
                                        snsTopicService.publishMessageToTopic("La subscripcion al fondo "+fondo.nombre()+" ha sido exitosa!");
                                        return transaccionService.create(transaccion).thenReturn(fondo);
                                    })
                            )
                            .then(Mono.defer(() -> repository.save(cliente)))
                            .map(updatedCliente -> mapper.toDTO(updatedCliente));
                })
                .switchIfEmpty(Mono.error(new NotFoundException("not found cliente")));
    }

    @Transactional
    @Override
    public Mono<ClienteDTO> removeFondosCliente(String idCliente, Set<String> idsFondos) {
        return repository.findById(idCliente)
                .flatMap(cliente -> {
                    if (cliente.getFondos() == null || cliente.getFondos().isEmpty()) {
                        return Mono.error(new BusinessException("El cliente no tiene fondos asociados."));
                    }

                    return Flux.fromIterable(idsFondos)
                            .flatMap(idFondo -> fondoService.findById(idFondo)
                                    .flatMap(fondo -> {
                                        var fondosCliente = cliente.getFondos();

                                        // Mapea el fondo a un documento y verifica si está asociado al cliente
                                        var fondoDocumento = fondoMapper.toDocument(fondo);
                                        if (!fondosCliente.contains(fondoDocumento)) {
                                            return Mono.error(new BusinessException(
                                                    "El fondo " + fondo.nombre() + " no está vinculado al cliente."));
                                        }

                                        // Ajusta el saldo del cliente y elimina el fondo de su lista
                                        cliente.setSaldo(cliente.getSaldo() + fondo.montoMinimo());
                                        fondosCliente.remove(fondoDocumento);

                                        // Crea una transacción de cancelación
                                        TransaccionDTO transaccion = new TransaccionDTO(
                                                null, idCliente, idFondo, "Cancelacion",
                                                LocalDateTime.now(), fondo.montoMinimo());
                                        return transaccionService.create(transaccion).thenReturn(fondo);
                                    })
                            )
                            .then(Mono.defer(() -> repository.save(cliente)))
                            .map(updatedCliente -> mapper.toDTO(updatedCliente));
                })
                .switchIfEmpty(Mono.error(new NotFoundException("Cliente no encontrado")));
    }




}
