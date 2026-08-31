package com.repara.repository;

import com.repara.model.SolicitudServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudServicioRepository extends JpaRepository<SolicitudServicio, Long> {
    List<SolicitudServicio> findByClienteIdAndEstado(Long clienteId, SolicitudServicio.EstadoSolicitud estado);
    List<SolicitudServicio> findByTecnicoId(Long tecnicoId);
    List<SolicitudServicio> findByEstado(SolicitudServicio.EstadoSolicitud estado);
}