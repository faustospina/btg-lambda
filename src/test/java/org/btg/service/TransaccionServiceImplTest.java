package org.btg.service;

import org.btg.exception.NotFoundException;
import org.btg.model.documents.Fondo;
import org.btg.model.documents.Transaccion;
import org.btg.model.dto.FondoDTO;
import org.btg.model.dto.TransaccionDTO;
import org.btg.model.dto.TransaccionesResponse;
import org.btg.model.mapper.FondoMapper;
import org.btg.model.mapper.TransaccionMapper;
import org.btg.repository.TransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceImplTest {

    @InjectMocks
    private TransaccionServiceImpl transaccionService;

    @Mock
    private TransaccionMapper mapper;

    @Mock
    private TransaccionRepository repository;

    @Mock
    private FondoService fondoService;

    @Mock
    private FondoMapper fondoMapper;

    private Transaccion transaccion;
    private TransaccionDTO transaccionDTO;

    private TransaccionesResponse transaccionesResponse;

    @BeforeEach
    void setUp() {
        transaccion = Transaccion.builder()
                .id("1")
                .clienteId("cliente1")
                .fondoId("fondo1")
                .tipo("compra")
                .fecha(LocalDateTime.now())
                .monto(1000.0)
                .build();
        transaccionDTO = TransaccionDTO.builder()
                .id("1")
                .clienteId("cliente1")
                .fondoId("fondo1")
                .tipo("compra")
                .fecha(transaccion.getFecha())
                .monto(1000.0)
                .build();
       FondoDTO fondoDTO = new FondoDTO("1","test-fondo", 500000.0, "FPV");
        transaccionesResponse = TransaccionesResponse.builder().fondo(fondoDTO).fecha(LocalDateTime.now()).monto(500000.0).tipo("Apertura").build();
    }

    @Test
    void testCreate() {
        when(mapper.toDocument(transaccionDTO)).thenReturn(transaccion);
        when(repository.save(transaccion)).thenReturn(Mono.just(transaccion));
        when(mapper.toDTO(transaccion)).thenReturn(transaccionDTO);

        StepVerifier.create(transaccionService.create(transaccionDTO))
                .expectNext(transaccionDTO)
                .verifyComplete();

        verify(repository).save(transaccion);
    }

    @Test
    void testFindById_Found() {
        when(repository.findById("1")).thenReturn(Mono.just(transaccion));
        when(mapper.toDTO(transaccion)).thenReturn(transaccionDTO);

        StepVerifier.create(transaccionService.findById("1"))
                .expectNext(transaccionDTO)
                .verifyComplete();

        verify(repository).findById("1");
    }

    @Test
    void testFindById_NotFound() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(transaccionService.findById("1"))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals("Transaccion not found: 1"))
                .verify();

        verify(repository).findById("1");
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(Flux.just(transaccion));
        when(mapper.toDTO(transaccion)).thenReturn(transaccionDTO);

        StepVerifier.create(transaccionService.findAll())
                .expectNext(transaccionDTO)
                .verifyComplete();

        verify(repository).findAll();
    }

    @Test
    void testFindAll_Empty() {
        when(repository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(transaccionService.findAll())
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals("Not found elements transaccion"))
                .verify();

        verify(repository).findAll();
    }

    @Test
    void testFindByClienteId_NotFound() {
        when(repository.findByClienteId("cliente1")).thenReturn(Flux.empty());

        StepVerifier.create(transaccionService.findByClienteId("cliente1"))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals("Client not found: cliente1"))
                .verify();

        verify(repository).findByClienteId("cliente1");
    }
}