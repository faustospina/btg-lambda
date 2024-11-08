package org.btg.service;

import org.btg.exception.NotFoundException;
import org.btg.model.documents.Fondo;
import org.btg.model.dto.FondoDTO;
import org.btg.model.mapper.FondoMapper;
import org.btg.repository.FondoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FondoServiceImplTest {

    @InjectMocks
    private FondoServiceImpl fondoService;

    @Mock
    private FondoRepository repository;

    @Mock
    private FondoMapper mapper;

    private Fondo fondo;
    private FondoDTO fondoDTO;

    @BeforeEach
    void setUp() {
        fondo = new Fondo("1","test-fondo", 500000.0, "FPV");
        fondoDTO = new FondoDTO("1","test-fondo", 500000.0, "FPV");
    }

    @Test
    void testCreate() {
        when(mapper.toDocument(fondoDTO)).thenReturn(fondo);
        when(repository.save(fondo)).thenReturn(Mono.just(fondo));
        when(mapper.toDTO(fondo)).thenReturn(fondoDTO);

        StepVerifier.create(fondoService.create(fondoDTO))
                .expectNext(fondoDTO)
                .verifyComplete();

        verify(repository).save(fondo);
    }

    @Test
    void testFindById_whenFondoExists() {
        when(repository.findById("1")).thenReturn(Mono.just(fondo));
        when(mapper.toDTO(fondo)).thenReturn(fondoDTO);

        StepVerifier.create(fondoService.findById("1"))
                .expectNext(fondoDTO)
                .verifyComplete();

        verify(repository).findById("1");
    }

    @Test
    void testFindById_whenFondoDoesNotExist() {
        String nonExistentId = "999";
        when(repository.findById(nonExistentId)).thenReturn(Mono.empty());

        StepVerifier.create(fondoService.findById(nonExistentId))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof NotFoundException);
                    assertEquals("Client not found: " + nonExistentId, error.getMessage());
                })
                .verify();

        verify(repository).findById(nonExistentId);
    }

    @Test
    void testFindAll_whenFondosExist() {
        Fondo anotherFondo = new Fondo("2", "FondoTest2", 700000.0,"FPV");
        FondoDTO anotherFondoDTO = new FondoDTO("2", "FondoTest2", 700000.0,"FPV");

        when(repository.findAll()).thenReturn(Flux.just(fondo, anotherFondo));
        when(mapper.toDTO(fondo)).thenReturn(fondoDTO);
        when(mapper.toDTO(anotherFondo)).thenReturn(anotherFondoDTO);

        StepVerifier.create(fondoService.findAll())
                .expectNext(fondoDTO, anotherFondoDTO)
                .verifyComplete();

        verify(repository).findAll();
    }

    @Test
    void testFindAll_whenNoFondosExist() {
        when(repository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(fondoService.findAll())
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof NotFoundException);
                    assertEquals("Not found elements", error.getMessage());
                })
                .verify();

        verify(repository).findAll();
    }
}