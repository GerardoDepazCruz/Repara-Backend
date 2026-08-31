// service/AuthService.java
package com.repara.service;

import com.repara.config.JwtService;
import com.repara.dto.AuthRequest;
import com.repara.dto.AuthResponse;
import com.repara.dto.RegisterRequest;
import com.repara.model.Usuario;
import com.repara.repository.TecnicoRepository;
import com.repara.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UsuarioRepository usuarioRepository;
    private final TecnicoRepository tecnicoRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        try {
            log.info("Intentando registrar usuario: {}", request.getCorreo());
            
            // Validar que las contraseñas coincidan
            if (!request.getContrasena().equals(request.getConfirmarContrasena())) {
                throw new RuntimeException("Las contraseñas no coinciden");
            }

            // Validar que el correo no esté registrado
            if (usuarioRepository.existsByCorreo(request.getCorreo())) {
                throw new RuntimeException("El correo ya está registrado");
            }

            // Crear usuario con todos los campos necesarios
            Usuario usuario = new Usuario();
            usuario.setNombres(request.getNombres());
            usuario.setApellidos(request.getApellidos());
            usuario.setCorreo(request.getCorreo());
            usuario.setContrasenaHash(passwordEncoder.encode(request.getContrasena()));
            usuario.setTelefono(request.getTelefono());
            usuario.setRol(Usuario.RolUsuario.CLIENTE);
            usuario.setEsTecnico(false);
            usuario.setEstado(Usuario.EstadoUsuario.ACTIVO);
            
            // Los campos opcionales los dejamos como null
            usuario.setDni(null);
            usuario.setDireccion(null);
            usuario.setLatitud(null);
            usuario.setLongitud(null);
            usuario.setFotoPerfilUrl(null);
            usuario.setMotivoSuspension(null);

            log.info("Guardando usuario en la base de datos...");
            usuario = usuarioRepository.save(usuario);
            log.info("Usuario guardado con ID: {}", usuario.getId());

            // Generar token
            String token = jwtService.generateToken(usuario);

            return AuthResponse.builder()
                    .token(token)
                    .id(usuario.getId())
                    .correo(usuario.getCorreo())
                    .nombres(usuario.getNombres())
                    .apellidos(usuario.getApellidos())
                    .rol(usuario.getRol().name())
                    .esTecnico(usuario.getEsTecnico())
                    .build();
        } catch (Exception e) {
            log.error("Error al registrar usuario: {}", e.getMessage(), e);
            throw new RuntimeException("Error al registrar usuario: " + e.getMessage(), e);
        }
    }

    public AuthResponse login(AuthRequest request) {
        try {
            log.info("Intentando login: {}", request.getCorreo());
            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContrasena())
            );

            Usuario usuario = (Usuario) authentication.getPrincipal();
            
            // Verificar si el usuario está activo
            if (usuario.getEstado() != Usuario.EstadoUsuario.ACTIVO) {
                throw new RuntimeException("Usuario inactivo o suspendido");
            }

            // Verificar si es técnico y obtener su ID
            Long tecnicoId = null;
            if (usuario.getEsTecnico()) {
                var tecnico = tecnicoRepository.findByUsuarioId(usuario.getId());
                if (tecnico.isPresent()) {
                    tecnicoId = tecnico.get().getId();
                }
            }

            String token = jwtService.generateToken(usuario);

            return AuthResponse.builder()
                    .token(token)
                    .id(usuario.getId())
                    .correo(usuario.getCorreo())
                    .nombres(usuario.getNombres())
                    .apellidos(usuario.getApellidos())
                    .rol(usuario.getRol().name())
                    .esTecnico(usuario.getEsTecnico())
                    .tecnicoId(tecnicoId)
                    .build();
        } catch (Exception e) {
            log.error("Error al hacer login: {}", e.getMessage(), e);
            throw new RuntimeException("Error al iniciar sesión: " + e.getMessage(), e);
        }
    }
}