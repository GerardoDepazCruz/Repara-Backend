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
@Table(name = "insignias_tecnico")
public class InsigniaTecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_insignia")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tecnico", nullable = false)
    private Tecnico tecnico;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    @PrePersist
    protected void onCreate() {
        fechaAsignacion = LocalDateTime.now();
    }
}