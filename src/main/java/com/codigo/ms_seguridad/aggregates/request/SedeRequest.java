package com.codigo.ms_seguridad.aggregates.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class SedeRequest {
    private String departamento;
}
