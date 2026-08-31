// service/PostulacionService.java - VERSIÓN CORREGIDA
package com.repara.service;

import com.repara.dto.PostulacionRequest;
import com.repara.dto.PostulacionResponse;
import com.repara.model.PostulacionTecnico;
import com.repara.model.Tecnico;
import com.repara.model.Usuario;
import com.repara.repository.PostulacionTecnicoRepository;
import com.repara.repository.TecnicoRepository;
import com.repara.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostulacionService {

    private static final Logger log = LoggerFactory.getLogger(PostulacionService.class);
    private final PostulacionTecnicoRepository postulacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final TecnicoRepository tecnicoRepository;

    @Transactional
    public PostulacionResponse crearPostulacion(PostulacionRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuario = (Usuario) authentication.getPrincipal();
            log.info("Creando postulacion para usuario: {}", usuario.getCorreo());

            List<PostulacionTecnico> postulacionesExistentes = postulacionRepository.findByUsuarioId(usuario.getId());
            boolean tienePendiente = postulacionesExistentes.stream()
                    .anyMatch(p -> p.getEstado() == PostulacionTecnico.EstadoPostulacion.PENDIENTE);

            if (tienePendiente) {
                throw new RuntimeException("Ya tienes una postulacion pendiente de revision");
            }

            if (usuario.getEsTecnico()) {
                throw new RuntimeException("Ya eres un tecnico registrado");
            }

            PostulacionTecnico postulacion = PostulacionTecnico.builder()
                    .usuario(usuario)
                    .especialidadDeclarada(request.getEspecialidadDeclarada())
                    .aniosExperiencia(request.getAniosExperiencia())
                    .cvUrl(request.getCvUrl() != null ? request.getCvUrl() : "https://ejemplo.com/cv.pdf")
                    .certificadosUrl(request.getCertificadosUrl() != null ? request.getCertificadosUrl() : "https://ejemplo.com/certificados.pdf")
                    .dniDocumentoUrl(request.getDniDocumentoUrl() != null ? request.getDniDocumentoUrl() : "https://ejemplo.com/dni.pdf")
                    .estado(PostulacionTecnico.EstadoPostulacion.PENDIENTE)
                    .build();

            postulacion = postulacionRepository.save(postulacion);
            log.info("Postulacion creada con ID: {}", postulacion.getId());

            return PostulacionResponse.fromEntity(postulacion);
        } catch (Exception e) {
            log.error("Error al crear postulacion: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<PostulacionResponse> getPostulacionesPorUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        log.info("Obteniendo postulaciones para usuario: {}", usuario.getCorreo());

        return postulacionRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(PostulacionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PostulacionResponse> getPostulacionesPendientes() {
        log.info("Obteniendo todas las postulaciones pendientes");
        return postulacionRepository.findByEstado(PostulacionTecnico.EstadoPostulacion.PENDIENTE)
                .stream()
                .map(PostulacionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PostulacionResponse> getAllPostulaciones() {
        log.info("Obteniendo todas las postulaciones");
        return postulacionRepository.findAll()
                .stream()
                .map(PostulacionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
public PostulacionResponse aprobarPostulacion(Long postulacionId) {
    log.info("=== INICIANDO APROBACION DE POSTULACION ID: {} ===", postulacionId);
    
    try {
        // 1. Buscar la postulación
        PostulacionTecnico postulacion = postulacionRepository.findById(postulacionId)
                .orElseThrow(() -> new RuntimeException("Postulacion no encontrada con ID: " + postulacionId));

        log.info("Postulacion encontrada: estado={}", postulacion.getEstado());

        // 2. Verificar estado
        if (postulacion.getEstado() != PostulacionTecnico.EstadoPostulacion.PENDIENTE) {
            throw new RuntimeException("Esta postulacion ya fue revisada. Estado actual: " + postulacion.getEstado());
        }

        // 3. Obtener admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario admin = (Usuario) authentication.getPrincipal();
        log.info("Admin que aprueba: {}", admin.getCorreo());

        // 4. Actualizar postulación
        postulacion.setEstado(PostulacionTecnico.EstadoPostulacion.ACEPTADO);
        postulacion.setFechaRevision(LocalDateTime.now());
        postulacion.setAdminRevisor(admin);
        postulacion = postulacionRepository.save(postulacion);
        log.info("Postulacion actualizada a ACEPTADO");

        // 5. Actualizar usuario - CAMBIAR ROL Y esTecnico
        Usuario usuario = postulacion.getUsuario();
log.info("Usuario ID: {}, Correo: {}, Rol actual: {}", 
    usuario.getId(), 
    usuario.getCorreo(),
    usuario.getRol());

// ✅ CAMBIAR EL ROL A TECNICO
usuario.setRol(Usuario.RolUsuario.TECNICO);
usuario.setEsTecnico(true);
usuario = usuarioRepository.save(usuario);
log.info("Usuario actualizado: Rol={}, esTecnico={}", usuario.getRol(), usuario.getEsTecnico());

        // 6. Crear técnico
        log.info("Creando tecnico para usuario ID: {}", usuario.getId());
        
        Tecnico tecnico = new Tecnico();
        tecnico.setUsuario(usuario);
        tecnico.setIdPostulacionOrigen(postulacion.getId());
        tecnico.setCalificacionPromedio(BigDecimal.ZERO);
        tecnico.setTotalServicios(0);
        tecnico.setDisponible(true);
        tecnico.setEstado(Tecnico.EstadoTecnico.ACTIVO);
        tecnico.setFechaAprobacion(LocalDateTime.now());
        
        tecnico = tecnicoRepository.save(tecnico);
        log.info("Tecnico creado con ID: {}", tecnico.getId());

        log.info("=== APROBACION COMPLETADA EXITOSAMENTE ===");
        return PostulacionResponse.fromEntity(postulacion);
        
    } catch (Exception e) {
        log.error("ERROR al aprobar postulacion: {}", e.getMessage(), e);
        throw new RuntimeException("Error al aprobar postulacion: " + e.getMessage(), e);
    }
}

    @Transactional
    public PostulacionResponse rechazarPostulacion(Long postulacionId, String motivo) {
        try {
            log.info("Rechazando postulacion ID: {}, Motivo: {}", postulacionId, motivo);
            
            PostulacionTecnico postulacion = postulacionRepository.findById(postulacionId)
                    .orElseThrow(() -> new RuntimeException("Postulacion no encontrada"));

            if (postulacion.getEstado() != PostulacionTecnico.EstadoPostulacion.PENDIENTE) {
                throw new RuntimeException("Esta postulacion ya fue revisada");
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Usuario admin = (Usuario) authentication.getPrincipal();

            postulacion.setEstado(PostulacionTecnico.EstadoPostulacion.RECHAZADO);
            postulacion.setMotivoRechazo(motivo);
            postulacion.setFechaRevision(LocalDateTime.now());
            postulacion.setAdminRevisor(admin);
            postulacion = postulacionRepository.save(postulacion);

            return PostulacionResponse.fromEntity(postulacion);
        } catch (Exception e) {
            log.error("Error al rechazar postulacion: {}", e.getMessage(), e);
            throw new RuntimeException("Error al rechazar postulacion: " + e.getMessage(), e);
        }
    }
}