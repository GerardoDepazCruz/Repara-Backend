package com.repara.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer id;

    @Column(name = "nombre", nullable = false, unique = true, length = 80)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCategoria estado;

    @OneToMany(mappedBy = "categoria")
    private List<SolicitudServicio> solicitudes = new ArrayList<>();

    @ManyToMany(mappedBy = "especialidades")
    private List<Tecnico> tecnicos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (estado == null) estado = EstadoCategoria.ACTIVA;
    }

    public enum EstadoCategoria {
        ACTIVA, INACTIVA
    }
}