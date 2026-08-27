package com.codigo.ms_seguridad.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "restaurante")
@Getter
@Setter
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codigo_rest", unique = true)
    private String codigo = UUID.randomUUID().toString().replace("-","").substring(0,10);
    @Column(unique = true)
    private String rucRestaurante;
    private String distrito;
    private String nombreRestaurante;
    private String ubicacionRestaurante;
    private String foto;
    @ManyToOne
    @JoinColumn(name = "sede_id")
    private Sede sede;
    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MenuProducto> menuProductos = new HashSet<>();
    @OneToMany(mappedBy = "restauranteReserva", cascade = CascadeType.ALL)
    private Set<Reservas> reservas = new HashSet<>();
    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Usuario> usuarios = new HashSet<>();

    public void agregarAlMenu(MenuProducto menuProducto){
        this.menuProductos.add(menuProducto);
        menuProducto.setRestaurante(this);
    }

    public void vincularUsuario(Usuario usuario){
        this.usuarios.add(usuario);
        usuario.setRestaurante(this);
    }

    public void vinculadoReservas(Reservas reservas){
        this.reservas.add(reservas);
        reservas.setRestauranteReserva(this);
    }

}

