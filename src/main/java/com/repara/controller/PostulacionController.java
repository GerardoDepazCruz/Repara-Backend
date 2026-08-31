// controller/PostulacionController.java
package com.repara.controller;

import com.repara.dto.PostulacionRequest;
import com.repara.dto.PostulacionResponse;
import com.repara.service.PostulacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postulaciones")
@RequiredArgsConstructor
public class PostulacionController {

    private static final Logger log = LoggerFactory.getLogger(PostulacionController.class);
    private final PostulacionService postulacionService;

    @PostMapping
    public ResponseEntity<PostulacionResponse> crearPostulacion(@Valid @RequestBody PostulacionRequest request) {
        log.info("📝 Creando postulación");
        logAuthInfo();
        return ResponseEntity.ok(postulacionService.crearPostulacion(request));
    }

    @GetMapping("/mis-postulaciones")
    public ResponseEntity<List<PostulacionResponse>> getMisPostulaciones() {
        log.info("📋 Obteniendo postulaciones del usuario");
        logAuthInfo();
        return ResponseEntity.ok(postulacionService.getPostulacionesPorUsuario());
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<PostulacionResponse>> getPostulacionesPendientes() {
        log.info("📋 ADMIN: Obteniendo postulaciones pendientes");
        logAuthInfo();
        return ResponseEntity.ok(postulacionService.getPostulacionesPendientes());
    }

    @GetMapping("/todas")
    public ResponseEntity<List<PostulacionResponse>> getAllPostulaciones() {
        log.info("📋 ADMIN: Obteniendo todas las postulaciones");
        logAuthInfo();
        return ResponseEntity.ok(postulacionService.getAllPostulaciones());
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<PostulacionResponse> aprobarPostulacion(@PathVariable Long id) {
        log.info("✅ ADMIN: Aprobando postulación ID: {}", id);
        logAuthInfo();
        
        // Verificar autenticación
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            log.error("❌ No hay autenticación");
            throw new RuntimeException("No autenticado");
        }
        
        log.info("👤 Nombre: {}", auth.getName());
        log.info("🔑 Credenciales: {}", auth.getCredentials());
        log.info("🔑 Roles: {}", auth.getAuthorities());
        log.info("🔑 Principal: {}", auth.getPrincipal());
        log.info("🔑 Details: {}", auth.getDetails());
        
        return ResponseEntity.ok(postulacionService.aprobarPostulacion(id));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<PostulacionResponse> rechazarPostulacion(
            @PathVariable Long id,
            @RequestParam String motivo) {
        log.info("❌ ADMIN: Rechazando postulación ID: {}, Motivo: {}", id, motivo);
        logAuthInfo();
        return ResponseEntity.ok(postulacionService.rechazarPostulacion(id, motivo));
    }

    private void logAuthInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.info("🔐 Auth: name={}, roles={}, authenticated={}", 
                auth.getName(), 
                auth.getAuthorities(),
                auth.isAuthenticated());
        } else {
            log.warn("⚠️ No hay autenticación en el contexto");
        }
    }
}