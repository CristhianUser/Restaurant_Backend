package com.codigo.ms_seguridad.aggregates.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductMenuRequest {
    private Double precioBase;
    private Double descuento;
    private Double precioFinal;
    private String descripcion;
}
