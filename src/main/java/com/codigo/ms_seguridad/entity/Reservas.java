package com.codigo.ms_seguridad.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "reservas")
@Getter
@Setter
public class Reservas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codigo_res", unique = true)
    private String codigo = UUID.randomUUID().toString();
    private String tipoReserva;
    private Date fechaReserva;
    private LocalDateTime fechaHora;
    private LocalTime tiempoReserva;
    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restauranteReserva;
}
