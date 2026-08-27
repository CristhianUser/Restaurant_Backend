package com.codigo.ms_seguridad.service.impl;
import com.codigo.ms_seguridad.aggregates.request.SedeRequest;
import com.codigo.ms_seguridad.aggregates.response.ProductMasterResponse;
import com.codigo.ms_seguridad.aggregates.response.RestauranteResponse;
import com.codigo.ms_seguridad.aggregates.response.SedeResponse;
import com.codigo.ms_seguridad.entity.Restaurante;
import com.codigo.ms_seguridad.entity.Sede;
import com.codigo.ms_seguridad.repository.SedeRepository;
import com.codigo.ms_seguridad.service.SedeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SedeServiceImpl implements SedeService {

    private final SedeRepository sedeRepository;
    private final RestauranteServiceImpl restauranteService;

    @Override
    public List<SedeResponse> listSedeResponses(String filtro) {
        boolean isSearch = filtro.trim().isEmpty();
        List<SedeResponse> responses = isSearch ?
                sedeRepository.findAll().stream().map(this::getSedeResponse).toList() :
                sedeRepository.findByDepartamentoContainingIgnoreCaseOrDistritoContainingIgnoreCase(filtro,filtro).stream().map(this::getSedeResponse).toList();
        return responses;
    }

    public SedeResponse getSedeResponse(Sede sede){
        SedeResponse sedeResponse = new SedeResponse();
        sedeResponse.setCodigo(sede.getCodigoUnico());
        sedeResponse.setDepartamento(sede.getDepartamento());
        sedeResponse.setRestauranteResponseSet(restauranteResponses(sede.getRestaurantes()));
        return sedeResponse;
    }

    public Sede getSedeEntity(SedeRequest sedeRequest){
        Sede sedeEntity = new Sede();
        sedeEntity.setDepartamento(sedeEntity.getDepartamento());
        return sedeEntity;
    }

    public Set<RestauranteResponse> restauranteResponses(Set<Restaurante> restaurantes){
        Set<RestauranteResponse> responses = new HashSet<>();
        for(Restaurante res:restaurantes){
            RestauranteResponse response = restauranteService.getResponseRestaurante(res);
            responses.add(response);
        }
        return responses;
    }

}
