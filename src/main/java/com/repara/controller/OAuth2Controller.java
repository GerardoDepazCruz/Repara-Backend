// controller/OAuth2Controller.java
package com.repara.controller;

import com.repara.config.JwtService;
import com.repara.model.Usuario;
import com.repara.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private static final Logger log = LoggerFactory.getLogger(OAuth2Controller.class);
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @GetMapping("/oauth2/success")
    public RedirectView oauth2Success() {
        log.info("🔐 ===== OAUTH2 SUCCESS CALLBACK RECIBIDO =====");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("🔐 Authentication: {}", authentication);
        log.info("🔐 Authentication class: {}", authentication != null ? authentication.getClass().getName() : "null");
        
        String token = null;
        
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("✅ Autenticado: {}", authentication.isAuthenticated());
            log.info("✅ Name: {}", authentication.getName());
            log.info("✅ Principal: {}", authentication.getPrincipal());
            log.info("✅ Principal class: {}", authentication.getPrincipal().getClass().getName());
            
            // ✅ Verificar si es OAuth2User
            if (authentication.getPrincipal() instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                
                String email = oauth2User.getAttribute("email");
                String name = oauth2User.getAttribute("name");
                
                log.info("📧 Email: {}", email);
                log.info("👤 Name: {}", name);
                
                if (email == null) {
                    email = "usuario@google.com";
                }
                
                if (name == null) {
                    name = "Usuario Google";
                }
                
                // Buscar o crear usuario
                Optional<Usuario> usuarioExistente = usuarioRepository.findByCorreo(email);
                
                Usuario usuario;
                if (usuarioExistente.isPresent()) {
                    usuario = usuarioExistente.get();
                    log.info("✅ Usuario existente: {}", usuario.getCorreo());
                } else {
                    usuario = new Usuario();
                    usuario.setCorreo(email);
                    usuario.setNombres(name);
                    usuario.setApellidos("");
                    usuario.setRol(Usuario.RolUsuario.CLIENTE);
                    usuario.setEsTecnico(false);
                    usuario.setEstado(Usuario.EstadoUsuario.ACTIVO);
                    usuario.setContrasenaHash(passwordEncoder.encode(UUID.randomUUID().toString()));
                    usuario = usuarioRepository.save(usuario);
                    log.info("🆕 Nuevo usuario creado: {}", usuario.getCorreo());
                }
                
                // Generar JWT
                token = jwtService.generateToken(usuario);
                log.info("🔑 Token generado");
            } else {
                log.warn("⚠️ Principal NO es OAuth2User. Es: {}", authentication.getPrincipal().getClass().getName());
                
                // ✅ INTENTAR OBTENER EL USUARIO DE OTRA FORMA
                Object principal = authentication.getPrincipal();
                if (principal instanceof String) {
                    log.info("Principal es String: {}", principal);
                }
            }
        } else {
            log.warn("⚠️ No hay autenticación o no está autenticado");
        }
        
        if (token != null) {
            String redirectUrl = "http://localhost:3000/login/success?token=" + token;
            log.info("➡️ Redirigiendo a: {}", redirectUrl);
            return new RedirectView(redirectUrl);
        } else {
            log.error("❌ No se pudo generar token, redirigiendo a error");
            return new RedirectView("http://localhost:3000/login?error=true");
        }
    }
}