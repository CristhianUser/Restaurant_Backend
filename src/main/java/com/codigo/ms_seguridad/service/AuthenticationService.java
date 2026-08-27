package com.codigo.ms_seguridad.service;

import com.codigo.ms_seguridad.aggregates.request.RestauranteMasterRequest;
import com.codigo.ms_seguridad.aggregates.request.RestauranteRequest;
import com.codigo.ms_seguridad.aggregates.request.SignInRequest;
import com.codigo.ms_seguridad.aggregates.request.SignUpRequest;
import com.codigo.ms_seguridad.aggregates.response.SignInResponse;
import com.codigo.ms_seguridad.entity.Usuario;

import java.util.List;

public interface AuthenticationService {
    //Registrar Usuario
    Usuario signUpUser(SignUpRequest signUpRequest);
    //Registrar Admin
    Usuario signUpAdmin(SignUpRequest signUpRequest);
    //Registrar GestorRestaurante
    void singUpGestorRestaurante(RestauranteMasterRequest restauranteMasterRequest);
    //Registrar GestorVentas
    Usuario singUpGestorVentas(SignUpRequest signUpRequest);
    List<Usuario> todos();

    //Meotodo para login
    SignInResponse signIn(SignInRequest signInRequest);
    //Metodo para obtener un nuevo accesstoken a partir del refreshtoken
    SignInResponse getTokenByRefreshToken(String refreshToken) throws IllegalAccessException;
}
