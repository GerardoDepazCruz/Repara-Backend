package com.repara.repository;

import com.repara.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    
    @Query("SELECT u FROM Usuario u WHERE u.correo = :correo AND u.estado = 'ACTIVO'")
    Optional<Usuario> findActivoByCorreo(@Param("correo") String correo);
}