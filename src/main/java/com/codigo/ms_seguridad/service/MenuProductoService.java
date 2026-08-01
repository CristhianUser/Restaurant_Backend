package com.codigo.ms_seguridad.service;

import com.codigo.ms_seguridad.aggregates.request.ProductMenuRequest;
import com.codigo.ms_seguridad.aggregates.response.ProductMenuResponse;

public interface MenuProductoService {
    ProductMenuResponse agregarMenu(String codigoProductoCatalogo, ProductMenuRequest productMenuRequest);
    ProductMenuResponse actualizarMenu(String codigoMenu);
    ProductMenuResponse verProducto(String codigoProductoMenu);
    void eliminarPlatoDelMenu(String codigoProductoMenu);
}
