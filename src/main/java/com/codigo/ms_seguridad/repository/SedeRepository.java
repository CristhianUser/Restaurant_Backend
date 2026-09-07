package com.codigo.ms_seguridad.repository;

import com.codigo.ms_seguridad.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SedeRepository extends JpaRepository<Sede, Long> {
    Optional<Sede> findByCodigoUnico(String codigoUnico);
    boolean existsByDepartamento(String departamento);
    Sede findByDepartamento(String departamento);
    List<Sede> findByDepartamentoContainingIgnoreCaseOrDistritoContainingIgnoreCase(String departamento, String distrito);
}
