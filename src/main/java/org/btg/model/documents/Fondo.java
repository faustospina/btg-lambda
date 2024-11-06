package org.btg.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "fondos")
public class Fondo {
    @Id
    private String id;
    private String nombre;
    private double montoMinimo;
    private String categoria;
}
