package org.btg.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionesResponse {
    private FondoDTO fondo;
    private String tipo;
    private LocalDateTime fecha;
    private double monto;
}
