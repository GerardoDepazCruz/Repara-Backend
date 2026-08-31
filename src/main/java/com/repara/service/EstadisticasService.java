package com.repara.service;

import com.repara.dto.EstadisticasAdminDTO;
import com.repara.model.SolicitudServicio;
import com.repara.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstadisticasService {

    private final UsuarioRepository usuarioRepository;
    private final TecnicoRepository tecnicoRepository;
    private final SolicitudServicioRepository solicitudRepository;
    private final PostulacionTecnicoRepository postulacionRepository;

    public EstadisticasAdminDTO getEstadisticas() {
        Long totalUsuarios = usuarioRepository.count();
        Long totalTecnicos = tecnicoRepository.count();
        Long totalTecnicosDisponibles = tecnicoRepository.findByDisponibleTrue().stream().count();
        Long totalSolicitudes = solicitudRepository.count();

        Long solicitudesPendientes = solicitudRepository.findByEstado(SolicitudServicio.EstadoSolicitud.PENDIENTE_ASIGNACION).stream().count();
        Long solicitudesCompletadas = solicitudRepository.findByEstado(SolicitudServicio.EstadoSolicitud.COMPLETADO).stream().count();
        Long solicitudesCanceladas = solicitudRepository.findByEstado(SolicitudServicio.EstadoSolicitud.CANCELADO).stream().count();

        // Calcular ganancias totales (simulado - de solicitudes completadas con costo final)
        BigDecimal totalGanancias = BigDecimal.ZERO;
        // Aquí deberías sumar los costos finales de las solicitudes completadas

        Long totalPostulacionesPendientes = postulacionRepository.findByEstado(com.repara.model.PostulacionTecnico.EstadoPostulacion.PENDIENTE).stream().count();

        // Solicitudes por categoría (simulado)
        Map<String, Long> solicitudesPorCategoria = new HashMap<>();
        solicitudesPorCategoria.put("Lavadoras", 25L);
        solicitudesPorCategoria.put("Microondas", 15L);
        solicitudesPorCategoria.put("Refrigeradores", 20L);

        // Ganancias por mes (simulado)
        Map<String, BigDecimal> gananciasPorMes = new HashMap<>();
        gananciasPorMes.put("Enero", new BigDecimal("1500.00"));
        gananciasPorMes.put("Febrero", new BigDecimal("2200.00"));
        gananciasPorMes.put("Marzo", new BigDecimal("1800.00"));

        return EstadisticasAdminDTO.builder()
                .totalUsuarios(totalUsuarios)
                .totalTecnicos(totalTecnicos)
                .totalTecnicosDisponibles(totalTecnicosDisponibles)
                .totalSolicitudes(totalSolicitudes)
                .solicitudesPendientes(solicitudesPendientes)
                .solicitudesCompletadas(solicitudesCompletadas)
                .solicitudesCanceladas(solicitudesCanceladas)
                .totalGanancias(totalGanancias)
                .totalPostulacionesPendientes(totalPostulacionesPendientes)
                .solicitudesPorCategoria(solicitudesPorCategoria)
                .gananciasPorMes(gananciasPorMes)
                .build();
    }
}