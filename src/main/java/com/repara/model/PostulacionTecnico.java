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
@Table(name = "postulaciones_tecnico")
public class PostulacionTecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_postulacion")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "especialidad_declarada", nullable = false, length = 255)
    private String especialidadDeclarada;

    @Column(name = "anios_experiencia")
    private Integer aniosExperiencia;

    @Column(name = "cv_url", length = 255)
    private String cvUrl;

    @Column(name = "certificados_url", length = 255)
    private String certificadosUrl;

    @Column(name = "dni_documento_url", length = 255)
    private String dniDocumentoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPostulacion estado;

    @Column(name = "motivo_rechazo", length = 255)
    private String motivoRechazo;

    @Column(name = "fecha_postulacion", nullable = false)
    private LocalDateTime fechaPostulacion;

    @Column(name = "fecha_revision")
    private LocalDateTime fechaRevision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin_revisor")
    private Usuario adminRevisor;

    @PrePersist
    protected void onCreate() {
        fechaPostulacion = LocalDateTime.now();
        if (estado == null) estado = EstadoPostulacion.PENDIENTE;
    }

    public enum EstadoPostulacion {
        PENDIENTE, ACEPTADO, RECHAZADO
    }
}