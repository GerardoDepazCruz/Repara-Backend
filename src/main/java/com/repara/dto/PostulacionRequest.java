package com.repara.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostulacionRequest {
    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidadDeclarada;

    @NotNull(message = "Los años de experiencia son obligatorios")
    private Integer aniosExperiencia;

    private String cvUrl;
    private String certificadosUrl;
    private String dniDocumentoUrl;
}