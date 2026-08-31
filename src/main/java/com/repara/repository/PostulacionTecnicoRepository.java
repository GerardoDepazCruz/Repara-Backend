package com.repara.repository;

import com.repara.model.PostulacionTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostulacionTecnicoRepository extends JpaRepository<PostulacionTecnico, Long> {
    List<PostulacionTecnico> findByUsuarioId(Long usuarioId);
    List<PostulacionTecnico> findByEstado(PostulacionTecnico.EstadoPostulacion estado);
}