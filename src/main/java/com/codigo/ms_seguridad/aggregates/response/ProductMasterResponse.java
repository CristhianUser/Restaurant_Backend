package com.codigo.ms_seguridad.aggregates.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductMasterResponse {
    private String codigo;
    private String nombre;
    private String categoria;
    private String foto;
}
