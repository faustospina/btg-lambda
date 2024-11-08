package org.btg.service;

import org.btg.model.documents.Cliente;
import org.btg.model.documents.Fondo;
import org.btg.model.dto.ClienteDTO;
import org.btg.model.dto.FondoDTO;
import org.btg.model.dto.TipoTopic;
import org.btg.model.dto.TransaccionDTO;
import org.btg.model.mapper.ClienteMapper;
import org.btg.model.mapper.FondoMapper;
import org.btg.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository repository;

    @Mock
    private ClienteMapper mapper;

    @Mock
    private FondoMapper fondoMapper;

    @Mock
    private FondoService fondoService;

    @Mock
    private TransaccionService transaccionService;

    @Mock
    private SnsTopicService snsTopicService;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId("cliente1");
        cliente.setSaldo(1000000.0);
        cliente.setFondos(new HashSet<>());

        clienteDTO = new ClienteDTO("cliente1", "test@example.com", "+1234567890", "1000000", TipoTopic.email, 1000000.0,new HashSet<>());
    }

    @Test
    void testAddFondosCliente_withSufficientBalance() {
        String idFondo = "fondo1";
        FondoDTO fondoDTO = new FondoDTO(idFondo, "Fondo Test", 500000.0,"FPV");
        TransaccionDTO transaccionDTO = new TransaccionDTO(null, "cliente1", idFondo, "Apertura", LocalDateTime.now(), 500000);

        when(repository.findById("cliente1")).thenReturn(Mono.just(cliente));
        when(fondoService.findById(idFondo)).thenReturn(Mono.just(fondoDTO));
        when(fondoMapper.toDocument(fondoDTO)).thenReturn(new Fondo());
        when(transaccionService.create(any(TransaccionDTO.class))).thenReturn(Mono.just(transaccionDTO));
        when(repository.save(cliente)).thenReturn(Mono.just(cliente));
        when(mapper.toDTO(cliente)).thenReturn(clienteDTO);

        Set<String> idsFondos = Set.of(idFondo);

        Mono<ClienteDTO> result = clienteService.addFondosCliente("cliente1", idsFondos);

        ClienteDTO updatedCliente = result.block();
        assertNotNull(updatedCliente);
        assertEquals(1000000, updatedCliente.saldo());
        verify(snsTopicService).publishMessageToTopic("La subscripcion al fondo " + fondoDTO.nombre() + " ha sido exitosa!");
    }

    @Test
    void testRemoveFondosCliente_withFondoLinked() {
        String idFondo = "fondo1";
        FondoDTO fondoDTO = new FondoDTO(idFondo, "Fondo Test", 500000,"FVP");
        Fondo fondo = new Fondo();
        fondo.setId(idFondo);

        TransaccionDTO transaccionDTO = TransaccionDTO.builder()
                .id("1")
                .clienteId("cliente1")
                .fondoId("fondo1")
                .tipo("compra")
                .fecha(LocalDateTime.now())
                .monto(1000.0)
                .build();

        cliente.getFondos().add(fondo);

        when(repository.findById("cliente1")).thenReturn(Mono.just(cliente));
        when(fondoService.findById(idFondo)).thenReturn(Mono.just(fondoDTO));
        when(fondoMapper.toDocument(fondoDTO)).thenReturn(fondo);
        when(transaccionService.create(any(TransaccionDTO.class))).thenReturn(Mono.just(transaccionDTO));
        when(repository.save(cliente)).thenReturn(Mono.just(cliente));
        when(mapper.toDTO(cliente)).thenReturn(clienteDTO);

        Set<String> idsFondos = Set.of(idFondo);

        Mono<ClienteDTO> result = clienteService.removeFondosCliente("cliente1", idsFondos);

        ClienteDTO updatedCliente = result.block();
        assertNotNull(updatedCliente);
        assertEquals(1000000, updatedCliente.saldo());
        verify(transaccionService).create(any(TransaccionDTO.class));
    }
}