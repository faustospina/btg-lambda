package org.btg.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record TransaccionDTO(String id, String clienteId, String fondoId, String tipo, LocalDateTime fecha, double monto) {
}
