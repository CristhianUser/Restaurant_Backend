package com.codigo.ms_seguridad.service;

import com.codigo.ms_seguridad.aggregates.request.RestauranteRequest;
import com.codigo.ms_seguridad.aggregates.response.RestauranteResponse;

import java.util.List;

public interface RestauranteService {
    RestauranteResponse createRestaurante(String codigoSede,RestauranteRequest restauranteRequest);
    List<RestauranteResponse> listRestaurantes(String nombreRestaurante);
    RestauranteResponse findByNombreResturante(String nombreRestaurante);
    RestauranteResponse updateRestaurante(String nombreRestaurante,RestauranteRequest restauranteRequest);
    void DeleteByCodigoUnicoDelRestaurante(String codigoUnicoRestaurante);
}
