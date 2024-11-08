package org.btg.handler;

import lombok.RequiredArgsConstructor;
import org.btg.exception.NotFoundException;
import org.btg.model.dto.TransaccionDTO;
import org.btg.service.TransaccionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class TransaccionHandler {

    private final TransaccionService transaccionService;

    public Mono<ServerResponse> createTransaccion(ServerRequest request) {
        return request.bodyToMono(TransaccionDTO.class)
                .flatMap(transaccion -> transaccionService.create(transaccion)
                        .flatMap(transaccionOut -> ServerResponse
                                .created(URI.create("/transaccion/".concat(transaccionOut.id())))
                                .contentType(APPLICATION_JSON)
                                .bodyValue(transaccionOut)
                        )
                );
    }

    public Mono<ServerResponse> findTransaccion(ServerRequest request) {
        String id = request.pathVariable("id");
        return transaccionService.findById(id)
                .flatMap(transaccionOut -> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(transaccionOut)
                )
                .onErrorResume(NotFoundException.class, ex -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue(Collections.singletonMap("message", ex.getMessage())));
    }

    public Mono<ServerResponse> findAllTransacciones(ServerRequest request) {
        return transaccionService.findAll()
                .collectList()
                .flatMap(transacciones ->
                       ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .bodyValue(transacciones)
                )
                .onErrorResume(NotFoundException.class, ex -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue(Collections.singletonMap("message", ex.getMessage())));
    }

    public Mono<ServerResponse> findTransaccionByCliente(ServerRequest request) {
        String idCliente = request.pathVariable("idCliente");
        return transaccionService.findByClienteId(idCliente)
                .collectList()
                .flatMap(transacciones ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .bodyValue(transacciones)
                )
                .onErrorResume(NotFoundException.class, ex -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue(Collections.singletonMap("message", ex.getMessage())));
    }
}
