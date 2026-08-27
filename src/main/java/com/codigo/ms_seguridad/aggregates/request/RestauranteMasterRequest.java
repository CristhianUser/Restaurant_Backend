package com.codigo.ms_seguridad.aggregates.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestauranteMasterRequest {
    private RestauranteRequest restauranteRequest;
    private SignUpRequest signUpRequest;
}
