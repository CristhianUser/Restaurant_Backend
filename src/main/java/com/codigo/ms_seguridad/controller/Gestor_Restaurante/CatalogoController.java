package com.codigo.ms_seguridad.controller.Gestor_Restaurante;

import com.codigo.ms_seguridad.aggregates.request.ProductMenuRequest;
import com.codigo.ms_seguridad.aggregates.response.ProductMasterResponse;
import com.codigo.ms_seguridad.aggregates.response.ProductMenuResponse;
import com.codigo.ms_seguridad.service.MenuProductoService;
import com.codigo.ms_seguridad.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestor-rest/")
@RequiredArgsConstructor
public class CatalogoController {

    private final ProductoService productoService;
    private final MenuProductoService menuProductoService;

    @GetMapping("catalogo-productos")
    private List<ProductMasterResponse> ListProducts(@RequestParam("filtro") String filtro){
        return productoService.listProductos(filtro);
    }

    @PostMapping("add-producto")
    private ProductMenuResponse ProductMap(@RequestBody ProductMenuRequest productMenuRequest){
        return menuProductoService.createProductMenu(productMenuRequest);
    }

}
