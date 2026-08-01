package com.codigo.ms_seguridad.aggregates.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductMenuResponse {
    private String id;
    private String nombre;
    private String categoria;
    private Double precioBase;
    private Double descuento;
    private Double precioFinal;
    private String descripcion;
    private String foto;
}
