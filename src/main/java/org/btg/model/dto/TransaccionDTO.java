package org.btg.model.dto;

import java.time.LocalDateTime;

public record TransaccionDTO(String id, String clienteId, String fondoId, String tipo, LocalDateTime fecha, double monto) {
}
