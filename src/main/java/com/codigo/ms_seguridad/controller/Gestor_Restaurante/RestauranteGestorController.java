package com.codigo.ms_seguridad.controller.Gestor_Restaurante;

import com.codigo.ms_seguridad.aggregates.response.RestauranteResponse;
import com.codigo.ms_seguridad.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/gestor-rest/")
@RestController
@RequiredArgsConstructor
public class RestauranteGestorController {
    private final RestauranteService restauranteService;

    @GetMapping("restaurante")
    private RestauranteResponse getRestauranteUser(){
        return restauranteService.getRestaurante();
    }

}
