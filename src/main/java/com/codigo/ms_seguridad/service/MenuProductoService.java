package com.codigo.ms_seguridad.service;

import com.codigo.ms_seguridad.aggregates.request.ProductMenuRequest;
import com.codigo.ms_seguridad.aggregates.response.ProductMenuResponse;
import com.codigo.ms_seguridad.entity.MenuProducto;

import java.util.List;

public interface MenuProductoService {
    List<ProductMenuResponse> listProductMenuResponses(String producto, String categoria);
    ProductMenuResponse createProductMenu(ProductMenuRequest productMenuRequest);
}
