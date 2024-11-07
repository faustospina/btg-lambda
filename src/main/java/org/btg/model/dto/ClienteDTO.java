package org.btg.model.dto;

import java.util.List;

public record ClienteDTO(String id, String nombre, String email, String telefono, String preferenciaNotificacion, Double saldo, List<FondoDTO> fondos) {
}
