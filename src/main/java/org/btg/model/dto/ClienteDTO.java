package org.btg.model.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public record ClienteDTO(String id, String nombre, String email, String telefono, String preferenciaNotificacion, double saldo) {
}
