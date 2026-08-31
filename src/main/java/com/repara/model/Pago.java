package com.repara.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private SolicitudServicio solicitud;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false)
    private TipoPago tipoPago;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

// model/Pago.java
@Column(name = "monto", nullable = false, precision = 10, scale = 2)
private BigDecimal monto;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPago estado;

    @Column(name = "referencia_transaccion", length = 150)
    private String referenciaTransaccion;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) estado = EstadoPago.PENDIENTE;
    }

    public enum TipoPago {
        INICIAL, COMPLEMENTARIO
    }

    public enum EstadoPago {
        PENDIENTE, PAGADO, REEMBOLSADO, FALLIDO
    }
}