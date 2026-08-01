package com.codigo.ms_seguridad.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categoria")
@Setter
@Getter
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreCategoria;
    @OneToMany(mappedBy = "categoria")
    @JsonManagedReference
    private Set<ProductoMaster> productoMasters = new HashSet<>();

    public void añadirProductoListaCategorias(ProductoMaster productoMaster){
        this.getProductoMasters().add(productoMaster);
        productoMaster.setCategoria(this);
    }
}
