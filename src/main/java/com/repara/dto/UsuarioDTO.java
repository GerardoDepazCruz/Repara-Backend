package com.repara.dto;

import com.repara.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String direccion;
    private String fotoPerfilUrl;
    private String rol;
    private Boolean esTecnico;
    private String estado;
    private LocalDateTime fechaRegistro;

    public static UsuarioDTO fromEntity(Usuario usuario) {
        return UsuarioDTO.builder()
            .id(usuario.getId())
            .nombres(usuario.getNombres())
            .apellidos(usuario.getApellidos())
            .correo(usuario.getCorreo())
            .telefono(usuario.getTelefono())
            .direccion(usuario.getDireccion())
            .fotoPerfilUrl(usuario.getFotoPerfilUrl())
            .rol(usuario.getRol().name())
            .esTecnico(usuario.getEsTecnico())
            .estado(usuario.getEstado().name())
            .fechaRegistro(usuario.getFechaRegistro())
            .build();
    }
}