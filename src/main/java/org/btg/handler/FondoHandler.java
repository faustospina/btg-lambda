package org.btg.handler;

import lombok.RequiredArgsConstructor;
import org.btg.exception.NotFoundException;
import org.btg.model.dto.FondoDTO;
import org.btg.service.FondoService;
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
public class FondoHandler {

    private final FondoService fondoService;

    public Mono<ServerResponse> createFondo(ServerRequest request) {
        return request.bodyToMono(FondoDTO.class)
                .flatMap(fondo -> fondoService.create(fondo).flatMap(fondoOut ->
                        ServerResponse.created(URI.create("/fondo/".concat(fondoOut.id())))
                                .contentType(APPLICATION_JSON)
                                .bodyValue(fondoOut)
                ));
    }


    public Mono<ServerResponse> findFondo(ServerRequest request) {
        String id = request.pathVariable("id");
        return fondoService.findById(id)
                .flatMap(fondoOut -> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(fondoOut)
                )
                .onErrorResume(NotFoundException.class, ex -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue(Collections.singletonMap("message", ex.getMessage())));
    }


    public Mono<ServerResponse> findAllFondos(ServerRequest request) {
        return fondoService.findAll()
                .collectList()
                .flatMap(fondos ->  ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .bodyValue(fondos)
                )
                .onErrorResume(NotFoundException.class, ex -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue(Collections.singletonMap("message", ex.getMessage())));
    }
}
