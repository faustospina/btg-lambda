package org.btg.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "transacciones")
public class Transaccion {
    @Id
    private String id;
    private String clienteId;
    private String fondoId;
    private String tipo; // "apertura" o "cancelacion"
    private LocalDateTime fecha;
    private double monto;
}
