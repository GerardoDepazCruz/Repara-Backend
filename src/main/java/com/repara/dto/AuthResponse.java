package com.repara.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tipo = "Bearer";
    private Long id;
    private String correo;
    private String nombres;
    private String apellidos;
    private String rol;
    private Boolean esTecnico;
    private Long tecnicoId;
}