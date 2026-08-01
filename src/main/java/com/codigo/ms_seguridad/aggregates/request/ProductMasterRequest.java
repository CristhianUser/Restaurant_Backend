package com.codigo.ms_seguridad.aggregates.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductMasterRequest {
    private String nombreCategoria;
    private String nombre;
    private MultipartFile foto;
}
