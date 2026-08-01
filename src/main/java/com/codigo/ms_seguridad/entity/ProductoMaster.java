package com.codigo.ms_seguridad.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "catalogo_productos")
@Getter
@Setter
public class ProductoMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codigo_prod", unique = true, length = 10)
    private String codigo = UUID.randomUUID().toString().replace("-","").substring(0,10);
    private String nombre;
    private String foto;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonBackReference
    private Categoria categoria;
}
