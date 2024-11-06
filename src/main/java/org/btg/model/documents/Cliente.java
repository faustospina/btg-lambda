package org.btg.model.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "clientes")
public class Cliente {
    @Id
    private String id;
    private String nombre;
    private String email;
    private String telefono;
    private String preferenciaNotificacion;  // "email" o "sms"
    private double saldo = 500000; // Monto inicial
}
