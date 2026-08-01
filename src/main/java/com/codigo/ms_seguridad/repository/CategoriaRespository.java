package com.codigo.ms_seguridad.repository;

import com.codigo.ms_seguridad.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRespository extends JpaRepository<Categoria, Long> {
    Categoria findByNombreCategoriaContainingIgnoreCase(String nombreCategoria);
}
