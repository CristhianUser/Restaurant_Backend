package com.codigo.ms_seguridad.repository;

import com.codigo.ms_seguridad.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    Restaurante findByCodigo(String codigo);
    boolean existsByRucRestaurante(String ruc);
    void deleteByCodigo(String codigo);
    List<Restaurante> findByNombreRestauranteContainingIgnoreCaseOrSedeDepartamentoContainingIgnoreCase(String nombreRestaurante, String departamento);
}
