package com.codigo.ms_seguridad.controller.Gestor_Restaurante;

import com.codigo.ms_seguridad.aggregates.response.ProductMenuResponse;
import com.codigo.ms_seguridad.service.MenuProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/gestor-rest/")
@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuProductoService menuProductoService;

    @GetMapping("menu-productos")
    private List<ProductMenuResponse> menu(@RequestParam("producto") String producto,
                                           @RequestParam("categoria") String categoria){
        return menuProductoService.listProductMenuResponses(producto,categoria);
    }

}
