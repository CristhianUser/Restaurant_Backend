package com.codigo.ms_seguridad.aggregates.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class RestauranteResponse {
    private String codigo;
    private String sedeRestaurante;
    private String rucRestaurante;
    private String nombreRestaurante;
    private String fotoRestaurante;
    private String ubicacionRestaurante;
    private Set<DataResponse> usuariosReponsResponses = new HashSet<>();
    private Set<ProductMenuResponse> productMenuResponses= new HashSet<>();
}
