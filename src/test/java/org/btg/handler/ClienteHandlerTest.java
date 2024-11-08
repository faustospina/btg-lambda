package org.btg.handler;

import org.btg.exception.BusinessException;
import org.btg.exception.NotFoundException;
import org.btg.model.dto.ClienteDTO;
import org.btg.model.dto.Fondos;
import org.btg.model.dto.TipoTopic;
import org.btg.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
class ClienteHandlerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteHandler clienteHandler;


    @Test
    void testCreateClienteSuccess() {
        ClienteDTO request = new ClienteDTO("123", "Juan Perez", "juan.perez@example.com", "+573001234567", TipoTopic.email,500000.0,new HashSet<>());
        ClienteDTO savedCliente = new ClienteDTO("123", "Juan Perez", "juan.perez@example.com", "+573001234567", TipoTopic.email,500000.0,new HashSet<>());

        when(clienteService.create(any(ClienteDTO.class))).thenReturn(Mono.just(savedCliente));

        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.bodyToMono(ClienteDTO.class)).thenReturn(Mono.just(request));

        Mono<ServerResponse> response = clienteHandler.createCliente(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().equals(HttpStatus.CREATED))
                .verifyComplete();
    }

    @Test
    void testCreateClienteError() {
        ClienteDTO request = new ClienteDTO("123", "Juan Perez", "juan.perez@example.com", "+573001234567", TipoTopic.email,500000.0,new HashSet<>());

        when(clienteService.create(any(ClienteDTO.class)))
                .thenReturn(Mono.error(new BusinessException("Número de teléfono inválido")));

        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.bodyToMono(ClienteDTO.class)).thenReturn(Mono.just(request));

        Mono<ServerResponse> response = clienteHandler.createCliente(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

    @Test
    void testFindClienteSuccess() {
        String id = "123";
        ClienteDTO cliente = new ClienteDTO("123", "Juan Perez", "juan.perez@example.com", "+573001234567", TipoTopic.email,500000.0,new HashSet<>());

        when(clienteService.findById(id)).thenReturn(Mono.just(cliente));

        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.pathVariable("id")).thenReturn(id);

        Mono<ServerResponse> response = clienteHandler.findCliente(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void testFindClienteNotFound() {
        String id = "nonexistent";

        when(clienteService.findById(id)).thenReturn(Mono.error(new NotFoundException("Client not found: " + id)));

        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.pathVariable("id")).thenReturn(id);

        Mono<ServerResponse> response = clienteHandler.findCliente(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }

    @Test
    void testFindAllClientes() {
        ClienteDTO cliente1 = new ClienteDTO("123", "Juan Perez", "juan.perez@example.com", "+573001234567", TipoTopic.email,500000.0,new HashSet<>());
        ClienteDTO cliente2 = new ClienteDTO("456", "Fernando ossorio", "fernando.osorio@example.com", "+573321234567", TipoTopic.email,900000.0,new HashSet<>());

        when(clienteService.findAll()).thenReturn(Flux.just(cliente1, cliente2));

        ServerRequest serverRequest = mock(ServerRequest.class);

        Mono<ServerResponse> response = clienteHandler.findAllClientes(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void testAddFondosClienteSuccess() {
        String idCliente = "123";
        Set<String> idsFondos = Set.of("fondo1", "fondo2");
        Fondos fondosRequest = new Fondos(idsFondos);
        ClienteDTO clienteUpdated = new ClienteDTO("123", "Juan Perez", "juan.perez@example.com", "+573001234567", TipoTopic.email,500000.0,new HashSet<>());

        when(clienteService.addFondosCliente(idCliente, idsFondos)).thenReturn(Mono.just(clienteUpdated));

        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.pathVariable("id")).thenReturn(idCliente);
        when(serverRequest.bodyToMono(Fondos.class)).thenReturn(Mono.just(fondosRequest));

        Mono<ServerResponse> response = clienteHandler.addFondosCliente(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void testRemoveFondosClienteSuccess() {
        String idCliente = "123";
        Set<String> idsFondos = Set.of("fondo1", "fondo2");
        Fondos fondosRequest = new Fondos(idsFondos);
        ClienteDTO clienteUpdated = new ClienteDTO("123", "Juan Perez", "juan.perez@example.com", "+573001234567", TipoTopic.email,500000.0,new HashSet<>());

        when(clienteService.removeFondosCliente(idCliente, idsFondos)).thenReturn(Mono.just(clienteUpdated));

        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.pathVariable("id")).thenReturn(idCliente);
        when(serverRequest.bodyToMono(Fondos.class)).thenReturn(Mono.just(fondosRequest));

        Mono<ServerResponse> response = clienteHandler.removeFondosCliente(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }
}