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
@Table(name = "notificacion_destinatario")
public class NotificacionDestinatario {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_notificacion", nullable = false)
    private Notificacion notificacion;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_lectura")
    private LocalDateTime fechaLectura;
}