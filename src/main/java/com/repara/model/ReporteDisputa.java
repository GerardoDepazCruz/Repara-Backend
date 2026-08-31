package com.repara.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reportes_disputas")
public class ReporteDisputa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private SolicitudServicio solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reportante", nullable = false)
    private Usuario reportante;

    @Column(name = "motivo", nullable = false, length = 255)
    private String motivo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoReporte estado;

    @Column(name = "resolucion", length = 255)
    private String resolucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin_resolutor")
    private Usuario adminResolutor;

    @Column(name = "fecha_reporte", nullable = false)
    private LocalDateTime fechaReporte;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @PrePersist
    protected void onCreate() {
        fechaReporte = LocalDateTime.now();
        if (estado == null) estado = EstadoReporte.ABIERTO;
    }

    public enum EstadoReporte {
        ABIERTO, EN_REVISION, CERRADO
    }
}