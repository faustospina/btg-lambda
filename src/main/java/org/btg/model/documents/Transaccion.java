package org.btg.model.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transacciones")
public class Transaccion {
    @Id
    private String id;
    private String clienteId;
    private String fondoId;
    private String tipo;
    private LocalDateTime fecha;
    private double monto;
}
