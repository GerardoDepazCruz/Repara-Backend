package com.repara.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "certificados_garantia")
public class CertificadoGarantia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_certificado")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false, unique = true)
    private SolicitudServicio solicitud;

    @Column(name = "url_pdf", nullable = false, length = 255)
    private String urlPdf;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "fecha_expiracion")
    private LocalDate fechaExpiracion;

    @PrePersist
    protected void onCreate() {
        fechaEmision = LocalDateTime.now();
    }
}