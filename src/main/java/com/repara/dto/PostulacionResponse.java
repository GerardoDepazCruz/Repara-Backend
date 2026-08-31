package com.repara.dto;

import com.repara.model.PostulacionTecnico;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostulacionResponse {
    private Long id;
    private Long usuarioId;
    private String nombres;
    private String apellidos;
    private String correo;
    private String especialidadDeclarada;
    private Integer aniosExperiencia;
    private String cvUrl;
    private String certificadosUrl;
    private String dniDocumentoUrl;
    private String estado;
    private String motivoRechazo;
    private LocalDateTime fechaPostulacion;
    private LocalDateTime fechaRevision;

    public static PostulacionResponse fromEntity(PostulacionTecnico postulacion) {
        if (postulacion == null) {
            return null;
        }
        
        PostulacionResponse response = new PostulacionResponse();
        response.setId(postulacion.getId());
        response.setEspecialidadDeclarada(postulacion.getEspecialidadDeclarada());
        response.setAniosExperiencia(postulacion.getAniosExperiencia());
        response.setCvUrl(postulacion.getCvUrl());
        response.setCertificadosUrl(postulacion.getCertificadosUrl());
        response.setDniDocumentoUrl(postulacion.getDniDocumentoUrl());
        response.setEstado(postulacion.getEstado() != null ? postulacion.getEstado().name() : null);
        response.setMotivoRechazo(postulacion.getMotivoRechazo());
        response.setFechaPostulacion(postulacion.getFechaPostulacion());
        response.setFechaRevision(postulacion.getFechaRevision());
        
        if (postulacion.getUsuario() != null) {
            response.setUsuarioId(postulacion.getUsuario().getId());
            response.setNombres(postulacion.getUsuario().getNombres());
            response.setApellidos(postulacion.getUsuario().getApellidos());
            response.setCorreo(postulacion.getUsuario().getCorreo());
        }
        
        return response;
    }
}