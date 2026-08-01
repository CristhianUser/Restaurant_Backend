package com.codigo.ms_seguridad.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Table(name = "menu_producto")
@Getter
@Setter
@Entity
public class MenuProducto {
    @Id
    @Column(unique = true)
    private String id = UUID.randomUUID().toString();
    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private ProductoMaster producto;
    private String categoria;
    private String foto;
    private Double precioBase;
    private Double descuento;
    private Double precioFinal;
    private String descripcion;

    public void vincularConProductoCatalogo(ProductoMaster producto){
        this.setProducto(producto);
        this.setFoto(producto.getFoto());
        this.setCategoria(producto.getCategoria().getNombreCategoria());
    }

}
