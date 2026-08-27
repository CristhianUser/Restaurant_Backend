package com.codigo.ms_seguridad.service;

import com.codigo.ms_seguridad.aggregates.request.RestauranteRequest;
import com.codigo.ms_seguridad.aggregates.response.RestauranteResponse;

import java.util.List;

public interface RestauranteService {
    List<RestauranteResponse> listRestaurantes(String filtro);
    RestauranteResponse getRestaurante();
//  RestauranteResponse getResponse(String codigoRestaurante);
    void eliminarRestaurante(String codigoRestaurante);
}
