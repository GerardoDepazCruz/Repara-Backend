package com.repara.controller;

import com.repara.dto.EstadisticasAdminDTO;
import com.repara.dto.UsuarioDTO;
import com.repara.model.Usuario;
import com.repara.repository.UsuarioRepository;
import com.repara.service.EstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final EstadisticasService estadisticasService;

    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasAdminDTO> getEstadisticas() {
        return ResponseEntity.ok(estadisticasService.getEstadisticas());
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDTO>> getUsuarios() {
        return ResponseEntity.ok(
            usuarioRepository.findAll().stream()
                .filter(u -> u.getRol() != Usuario.RolUsuario.ADMIN)
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/tecnicos")
    public ResponseEntity<List<UsuarioDTO>> getTecnicos() {
        return ResponseEntity.ok(
            usuarioRepository.findAll().stream()
                .filter(Usuario::getEsTecnico)
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList())
        );
    }

    @PutMapping("/usuarios/{id}/suspender")
    public ResponseEntity<UsuarioDTO> suspenderUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstado(Usuario.EstadoUsuario.SUSPENDIDO);
        usuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario));
    }

    @PutMapping("/usuarios/{id}/activar")
    public ResponseEntity<UsuarioDTO> activarUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstado(Usuario.EstadoUsuario.ACTIVO);
        usuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario));
    }
}