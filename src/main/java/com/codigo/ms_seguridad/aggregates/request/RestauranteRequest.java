package com.codigo.ms_seguridad.aggregates.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class RestauranteRequest {
    private String ubicacion;
    private String ubicacionUrl;
    private MultipartFile fotoRestaurante;
}
