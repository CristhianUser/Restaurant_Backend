package com.codigo.ms_seguridad.repository;

import com.codigo.ms_seguridad.entity.ProductoMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<ProductoMaster, Long> {
    ProductoMaster findByCodigo(String UUID);
    List<ProductoMaster> findByNombreContainingIgnoreCase(String nombreProducto);
}
