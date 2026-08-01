package com.codigo.ms_seguridad.controller.Cliente;

import com.codigo.ms_seguridad.aggregates.response.RestauranteResponse;
import com.codigo.ms_seguridad.aggregates.response.SedeResponse;
import com.codigo.ms_seguridad.service.RestauranteService;
import com.codigo.ms_seguridad.service.SedeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/user/")
public class MenuController {

    private final RestauranteService restauranteService;
    private final SedeService sedeService;

    @GetMapping("sedes")
    private ResponseEntity<List<SedeResponse>> responseList(@RequestParam String nombreSede){
        List<SedeResponse> sedeResponses = sedeService.listSedes(nombreSede);
        return ResponseEntity.ok(sedeResponses);
    }

    @GetMapping("sede/{codigoSede}/restaurantes")
    private ResponseEntity<SedeResponse> SedeRestaurantes(@PathVariable String codigoSede){
        SedeResponse sedeResponse = sedeService.findByCodigo(codigoSede);
        return ResponseEntity.ok(sedeResponse);
    }

    @GetMapping("restaurante/{nomnbreRestaurante}/menu")
    private ResponseEntity<RestauranteResponse> MenuRestaurante(@PathVariable String nombreRestaurante){
        RestauranteResponse restauranteResponse = restauranteService.findByNombreResturante(nombreRestaurante);
        return ResponseEntity.ok(restauranteResponse);
    }


}
