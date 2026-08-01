package com.codigo.ms_seguridad.service;

import com.codigo.ms_seguridad.aggregates.request.SedeRequest;
import com.codigo.ms_seguridad.aggregates.response.SedeResponse;

import java.util.List;

public interface SedeService {
    SedeResponse createSede(SedeRequest sedeRequest);
    SedeResponse findByCodigo(String codigo);
    List<SedeResponse> listSedes(String nombreSede);
}
