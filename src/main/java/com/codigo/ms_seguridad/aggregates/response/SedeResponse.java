package com.codigo.ms_seguridad.aggregates.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class SedeResponse {
    private String codigo;
    private String departamento;
    private Set<RestauranteResponse> restauranteResponseSet = new HashSet<>();
}
