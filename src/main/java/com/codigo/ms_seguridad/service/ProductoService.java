package com.codigo.ms_seguridad.service;

import com.codigo.ms_seguridad.aggregates.request.ProductMasterRequest;
import com.codigo.ms_seguridad.aggregates.response.ProductMasterResponse;

import java.util.List;

public interface ProductoService {
    ProductMasterResponse createProducto(ProductMasterRequest productMasterRequest);
    ProductMasterResponse findByIdProducto(Long id);
    ProductMasterResponse findByCodigoProducto(String codigo);
    ProductMasterResponse updateByIdProducto(String codigo, ProductMasterRequest productMasterRequest);
    List<ProductMasterResponse> listProductos(String nombreProducto);
    void deleteById(Long id);
    void deletteByCodigoProducto(String codigo);
}
