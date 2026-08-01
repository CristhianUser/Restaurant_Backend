package com.codigo.ms_seguridad.service.impl;

import com.codigo.ms_seguridad.aggregates.response.CategoryResponse;
import com.codigo.ms_seguridad.aggregates.response.ProductMasterResponse;
import com.codigo.ms_seguridad.entity.Categoria;
import com.codigo.ms_seguridad.entity.ProductoMaster;
import com.codigo.ms_seguridad.repository.CategoriaRespository;
import com.codigo.ms_seguridad.service.CategoriaService;
import com.codigo.ms_seguridad.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRespository categoriaRespository;
    private final ProductoServiceImpl productoService;

    @Override
    public List<CategoryResponse> allCategorias() {
        List<Categoria> categorias = categoriaRespository.findAll();
        List<CategoryResponse> categoryResponses = new ArrayList<>();

        for (Categoria cat:categorias){
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(cat.getId());
            categoryResponse.setNombreCategoria(cat.getNombreCategoria());

            for (ProductoMaster product:cat.getProductoMasters()){
                ProductMasterResponse productMasterResponse = productoService.mapResponseByEntity(product);
                categoryResponse.getProductMasterResponseSet().add(productMasterResponse);
            }

            categoryResponses.add(categoryResponse);
        }

        return categoryResponses;
    }

    @Override
    public Categoria createCategoria(String nuevaCategoria) {
        Categoria nuevaCategoriaEntity = new Categoria();
        nuevaCategoriaEntity.setNombreCategoria(nuevaCategoria.trim().toUpperCase());
        return categoriaRespository.save(nuevaCategoriaEntity);
    }
}
