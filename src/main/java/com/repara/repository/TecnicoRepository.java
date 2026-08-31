package com.repara.repository;

import com.repara.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
    Optional<Tecnico> findByUsuarioId(Long usuarioId);
    List<Tecnico> findByDisponibleTrue();
    List<Tecnico> findByEstado(Tecnico.EstadoTecnico estado);
}