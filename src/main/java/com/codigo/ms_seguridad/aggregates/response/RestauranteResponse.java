package com.codigo.ms_seguridad.aggregates.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class RestauranteResponse {
    private String codigo;
    private String nombreRestaurante;
    private String ubicacion;
    private String ubicacionUrl;
    private String imagenReferencia;
    private Set<ProductMenuResponse> productMenuResponseSet = new HashSet<>();
}
