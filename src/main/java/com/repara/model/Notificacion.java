package com.repara.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin_emisor", nullable = false)
    private Usuario adminEmisor;

    @Column(name = "asunto", nullable = false, length = 150)
    private String asunto;

    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_destinatario", nullable = false)
    private TipoDestinatario tipoDestinatario;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @OneToMany(mappedBy = "notificacion", cascade = CascadeType.ALL)
    private List<NotificacionDestinatario> destinatarios = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaEnvio = LocalDateTime.now();
    }

    public enum TipoDestinatario {
        TODOS_CLIENTES, TODOS_TECNICOS, ESPECIFICO
    }
}