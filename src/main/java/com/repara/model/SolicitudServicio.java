package com.repara.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "solicitudes_servicio")
public class SolicitudServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tecnico")
    private Tecnico tecnico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "descripcion_problema", nullable = false, columnDefinition = "TEXT")
    private String descripcionProblema;

    @Column(name = "direccion_servicio", nullable = false, length = 255)
    private String direccionServicio;

@Column(name = "latitud", precision = 10)
private Double latitud;

@Column(name = "longitud", precision = 10)
private Double longitud;

    @Column(name = "resumen_ia", columnDefinition = "TEXT")
    private String resumenIa;

@Column(name = "costo_estimado", precision = 10, scale = 2)
private BigDecimal costoEstimado;

  @Column(name = "costo_final", precision = 10, scale = 2)
private BigDecimal costoFinal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoSolicitud estado;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_aceptacion")
    private LocalDateTime fechaAceptacion;

    @Column(name = "fecha_finalizacion")
    private LocalDateTime fechaFinalizacion;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
    private List<MensajeIa> mensajesIa = new ArrayList<>();

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
    private List<Evidencia> evidencias = new ArrayList<>();

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
    private List<Pago> pagos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaSolicitud = LocalDateTime.now();
        if (estado == null) estado = EstadoSolicitud.EN_CREACION;
    }

    public enum EstadoSolicitud {
        EN_CREACION,
        PENDIENTE_ASIGNACION,
        ACEPTADA,
        PAGO_INICIAL_PENDIENTE,
        EN_CAMINO,
        EN_DIAGNOSTICO,
        DIAGNOSTICO_NO_REPARABLE,
        REPARACION_EN_PROCESO,
        PAGO_FINAL_PENDIENTE,
        COMPLETADO,
        CANCELADO
    }
}