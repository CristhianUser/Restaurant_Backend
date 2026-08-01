package com.codigo.ms_seguridad.repository;

import com.codigo.ms_seguridad.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    @Modifying
    @Transactional
    void deleteByCodigo(String codigoRestaurante);
    List<Restaurante> findByNombreUnicoContainingIgnoreCase(String nombreRestaurante);
    Restaurante findByCodigo(String codigoRestaurante);
    Optional<Restaurante> findByNombreUnico(String nombreRestaurante);
}
