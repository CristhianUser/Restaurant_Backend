package com.codigo.ms_seguridad.controller.Gestor_Restaurante;

import com.codigo.ms_seguridad.aggregates.request.ProductMasterRequest;
import com.codigo.ms_seguridad.aggregates.request.ProductMenuRequest;
import com.codigo.ms_seguridad.aggregates.response.ProductMasterResponse;
import com.codigo.ms_seguridad.aggregates.response.ProductMenuResponse;
import com.codigo.ms_seguridad.service.MenuProductoService;
import com.codigo.ms_seguridad.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/gestor-rest/")
@RequiredArgsConstructor
public class CatalogoController {

    private final ProductoService productoService;
    private final MenuProductoService menuProductoService;

    @GetMapping("catalogo-productos")
    private ResponseEntity<List<ProductMasterResponse>> CatalogoProductosMaster(@RequestParam String nombreProducto){
        List<ProductMasterResponse> productMasterResponseList = productoService.listProductos(nombreProducto);
        return ResponseEntity.ok(productMasterResponseList);
    }

    @PostMapping("añadir-producto/{codigoProducto}")
    private ResponseEntity<ProductMenuResponse> añadirProductoAlMenu(@PathVariable String codigoProducto, @RequestBody ProductMenuRequest productMenuRequest){
        ProductMenuResponse productMenuResponse = menuProductoService.agregarMenu(codigoProducto,productMenuRequest);
        return ResponseEntity.ok(productMenuResponse);
    }

    @DeleteMapping("eliminar/{idProductoMenu}")
    private void eliminarProductoMenuRestaurante(@PathVariable String idProductoMenu){
        menuProductoService.eliminarPlatoDelMenu(idProductoMenu);
    }

}
