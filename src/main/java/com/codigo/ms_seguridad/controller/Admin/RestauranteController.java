package com.codigo.ms_seguridad.controller.Admin;

import com.codigo.ms_seguridad.aggregates.response.RestauranteResponse;
import com.codigo.ms_seguridad.service.RestauranteService;
import com.codigo.ms_seguridad.service.impl.SedeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/admin/")
@RestController
@RequiredArgsConstructor
public class RestauranteController {

        private final RestauranteService restauranteService;
        private final SedeServiceImpl sedeService;

        @GetMapping("restaurantes")
        public List<RestauranteResponse> restaurantes(@RequestParam("filtro") String filtro){
                return  restauranteService.listRestaurantes(filtro);
        }

}
