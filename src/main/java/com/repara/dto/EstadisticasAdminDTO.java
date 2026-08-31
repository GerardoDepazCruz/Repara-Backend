package com.repara.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasAdminDTO {
    private Long totalUsuarios;
    private Long totalTecnicos;
    private Long totalTecnicosDisponibles;
    private Long totalSolicitudes;
    private Long solicitudesPendientes;
    private Long solicitudesCompletadas;
    private Long solicitudesCanceladas;
    private BigDecimal totalGanancias;
    private Long totalPostulacionesPendientes;
    private Map<String, Long> solicitudesPorCategoria;
    private Map<String, BigDecimal> gananciasPorMes;
}