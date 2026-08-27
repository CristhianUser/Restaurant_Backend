package com.codigo.ms_seguridad.service;

import com.codigo.ms_seguridad.aggregates.request.SedeRequest;
import com.codigo.ms_seguridad.aggregates.response.RestauranteResponse;
import com.codigo.ms_seguridad.aggregates.response.SedeResponse;
import com.codigo.ms_seguridad.entity.Restaurante;

import java.util.List;

public interface SedeService {
    List<SedeResponse> listSedeResponses(String filtro);
}
