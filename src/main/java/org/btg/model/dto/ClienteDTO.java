package org.btg.model.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record ClienteDTO(String id, @NotNull String nombre, @NotNull @Email String email, @NotNull String telefono, @NotNull TipoTopic preferenciaNotificacion, @NotNull Double saldo, Set<FondoDTO> fondos) {
}
