package com.codigo.ms_seguridad.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "sede")
@Getter
@Setter
public class Sede {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cod_sede",unique = true)
    private String codigoUnico = UUID.randomUUID().toString();
    private String nombre;
    private String distrito;
    private String ubicacion;
    private String fotoReferencia;
    @OneToMany(mappedBy = "sede")
    private Set<Restaurante> restaurantes = new HashSet<>();

    public void AsociarRestaurante(Restaurante restaurante){
        this.restaurantes.add(restaurante);
        restaurante.setSede(this);
    }
}
