package com.codigo.ms_seguridad.controller.Admin;

import com.codigo.ms_seguridad.aggregates.request.RestauranteRequest;
import com.codigo.ms_seguridad.aggregates.response.RestauranteResponse;
import com.codigo.ms_seguridad.aggregates.response.SedeResponse;
import com.codigo.ms_seguridad.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/admin/")
@RestController
@RequiredArgsConstructor
public class RestauranteController {

        private final RestauranteService restauranteService;

        @GetMapping("all-restaurante")
        private List<RestauranteResponse> allRestaurantes(@RequestParam("nombre") String nombreRestaurante){
            return  restauranteService.listRestaurantes(nombreRestaurante);
        }

        @PostMapping("{codigo}/create-restaurante")
        private RestauranteResponse createRestaurante(@PathVariable("codigo") String codigo,
                                                                                        @ModelAttribute RestauranteRequest restauranteRequest){
            return  restauranteService.createRestaurante(codigo, restauranteRequest);
        }

        @GetMapping("{nombre-restaurante}")
        private RestauranteResponse findRestaurante(@PathVariable("nombreRestaurante") String nombre){
            return restauranteService.findByNombreResturante(nombre);
        }
}
