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
@Table(name = "tecnicos")
public class Tecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tecnico")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "id_postulacion_origen", nullable = false)
    private Long idPostulacionOrigen;

    @Column(name = "calificacion_promedio", nullable = false)
    private BigDecimal calificacionPromedio;

    @Column(name = "total_servicios", nullable = false)
    private Integer totalServicios;

    @Column(name = "disponible", nullable = false)
    private Boolean disponible;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoTecnico estado;

    @Column(name = "fecha_aprobacion", nullable = false)
    private LocalDateTime fechaAprobacion;

    @ManyToMany
    @JoinTable(
        name = "tecnico_especialidad",
        joinColumns = @JoinColumn(name = "id_tecnico"),
        inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    private List<Categoria> especialidades = new ArrayList<>();

    @OneToMany(mappedBy = "tecnico")
    private List<SolicitudServicio> solicitudes = new ArrayList<>();

    @OneToMany(mappedBy = "tecnico")
    private List<InsigniaTecnico> insignias = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (fechaAprobacion == null) {
            fechaAprobacion = LocalDateTime.now();
        }
        if (estado == null) {
            estado = EstadoTecnico.ACTIVO;
        }
        if (calificacionPromedio == null) {
            calificacionPromedio = BigDecimal.ZERO;
        }
        if (totalServicios == null) {
            totalServicios = 0;
        }
        if (disponible == null) {
            disponible = true;
        }
    }

    public enum EstadoTecnico {
        ACTIVO, SUSPENDIDO, INACTIVO
    }
}