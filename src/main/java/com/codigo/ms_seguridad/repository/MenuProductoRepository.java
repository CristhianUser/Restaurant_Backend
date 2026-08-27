package com.codigo.ms_seguridad.repository;

import com.codigo.ms_seguridad.aggregates.response.ProductMenuResponse;
import com.codigo.ms_seguridad.entity.MenuProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuProductoRepository extends JpaRepository<MenuProducto, String> {
    List<MenuProducto> findByProducto_NombreContainingIgnoreCaseOrCategoriaContainingIgnoreCase(String nombre, String categoria);
}
