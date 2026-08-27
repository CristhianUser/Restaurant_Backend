package com.codigo.ms_seguridad.controller.Admin;

import com.codigo.ms_seguridad.aggregates.request.ProductMasterRequest;
import com.codigo.ms_seguridad.aggregates.response.ProductMasterResponse;
import com.codigo.ms_seguridad.entity.Categoria;
import com.codigo.ms_seguridad.service.CategoriaService;
import com.codigo.ms_seguridad.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/admin")
@RequiredArgsConstructor
@RestController
public class ProductMasterController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    @GetMapping("/catalogo-productos")
    public ResponseEntity<List<ProductMasterResponse>> listProductos(@RequestParam("nombreProducto") String nombreProducto){
        List<ProductMasterResponse> listaProductMasterResponses = productoService.listProductos(nombreProducto);
        return ResponseEntity.ok(listaProductMasterResponses);
    }

    @GetMapping("/pruebadmin")
    public String saludoAdmin(){
        return "Hola Administrador";
    }

    @GetMapping("/producto/{codigoProducto}")
    public ResponseEntity<ProductMasterResponse> findProducto(@PathVariable String codigoProducto){
        ProductMasterResponse productMasterResponse = productoService.findByCodigoProducto(codigoProducto);
        return ResponseEntity.ok(productMasterResponse);
    }

    @PostMapping("/catalogo-productos/crear-producto")
    public ResponseEntity<ProductMasterResponse> createProducto(@ModelAttribute ProductMasterRequest productMasterRequest){
        ProductMasterResponse productMasterResponse = productoService.createProducto(productMasterRequest);
        System.out.println("Categoría recibida: " + productMasterRequest.getNombreCategoria());
        System.out.println("Nombre recibido: " + productMasterRequest.getNombre());
        System.out.println("Foto recibida: " + (productMasterRequest.getFoto() != null ? productMasterRequest.getFoto().getOriginalFilename() : "NULA"));
        return ResponseEntity.ok(productMasterResponse);
    }

    @PostMapping("/{codigoProducto}/actualizar")
    public ResponseEntity<ProductMasterResponse> updateProducto(@PathVariable String codigoProducto, @RequestBody ProductMasterRequest productMasterRequest){
        ProductMasterResponse productMasterResponse = productoService.updateByIdProducto(codigoProducto, productMasterRequest);
        return ResponseEntity.ok(productMasterResponse);
    }

    @DeleteMapping("/eliminar/{codigo}")
    public void deleteProductoByCodigo(@PathVariable String codigo){
        productoService.deletteByCodigoProducto(codigo);
    }
}
