package com.codigo.ms_seguridad.service;

import com.codigo.ms_seguridad.aggregates.response.CategoryResponse;
import com.codigo.ms_seguridad.entity.Categoria;

import java.util.List;

public interface CategoriaService {
    List<CategoryResponse> allCategorias();
    Categoria createCategoria(String nuevaCategoria);
}
