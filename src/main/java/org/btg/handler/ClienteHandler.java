package org.btg.handler;

import lombok.RequiredArgsConstructor;
import org.btg.exception.NotFoundException;
import org.btg.model.dto.ClienteDTO;
import org.btg.service.ClienteService;
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
public class ClienteHandler {
    private final ClienteService service;

    public Mono<ServerResponse> createCliente(ServerRequest request) {
        return request.bodyToMono(ClienteDTO.class)
                .flatMap(cliente->service.create(cliente).flatMap(clienteOut->ServerResponse
                        .created(URI.create("/cliente".concat(clienteOut.id())))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(clienteOut)
                ));
    }

    public Mono<ServerResponse> findCliente(ServerRequest request) {
        String id = request.pathVariable("id");
        return service.findById(id)
                .flatMap(clienteOut -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(clienteOut))
                .onErrorResume(NotFoundException.class, ex -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue(Collections.singletonMap("message", ex.getMessage())));
    }


    public Mono<ServerResponse> findAllClientes(ServerRequest request) {
        return service.findAll()
                .collectList()
                .flatMap(clientes ->
                         ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .bodyValue(clientes)

                )
                .onErrorResume(NotFoundException.class, ex -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue(Collections.singletonMap("message", ex.getMessage())));
    }


}
