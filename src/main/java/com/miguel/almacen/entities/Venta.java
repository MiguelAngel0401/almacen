package com.miguel.almacen.entities;

import com.miguel.almacen.enums.EstadoVenta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VENTAS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VENTA")
    private Long id;

    @Column(name = "ESTADO", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoVenta estadoVenta;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUCURSAL", nullable = false)
    private Sucursal sucursal;

    @OneToMany(mappedBy = "venta" , cascade = CascadeType.ALL,
    orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DetalleVenta> detalleVentas = new ArrayList<>();

    public void agregarDetalle(DetalleVenta detalleVenta){
        if(detalleVenta == null)
            throw new IllegalArgumentException("El detalle es requerido");

        this.detalleVentas.add(detalleVenta);
        detalleVenta.setVenta(this);
    }

    public void cancelar(){
        if(this.estadoVenta ==  EstadoVenta.CANCELADA)
            throw new IllegalArgumentException(("La venta ya esta cancelada"));

        this.estadoVenta = EstadoVenta.CANCELADA;
    }
}
