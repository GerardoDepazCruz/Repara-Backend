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
@Table(name = "evidencias")
public class Evidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evidencia")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private SolicitudServicio solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_archivo", nullable = false)
    private TipoArchivo tipoArchivo;

    @Column(name = "url_archivo", nullable = false, length = 255)
    private String urlArchivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "etapa", nullable = false)
    private Etapa etapa;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida;

    @PrePersist
    protected void onCreate() {
        fechaSubida = LocalDateTime.now();
    }

    public enum TipoArchivo {
        FOTO, VIDEO
    }

    public enum Etapa {
        DESCRIPCION_PROBLEMA, DIAGNOSTICO, REPARACION_TERMINADA, FALLA_NO_REPARABLE
    }
}