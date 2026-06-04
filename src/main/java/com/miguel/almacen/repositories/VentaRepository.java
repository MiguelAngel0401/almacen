package com.miguel.almacen.repositories;

import com.miguel.almacen.entities.DetalleVenta;
import com.miguel.almacen.entities.Producto;
import com.miguel.almacen.entities.Venta;
import com.miguel.almacen.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByEstadoVenta(EstadoVenta estadoVenta);

    Optional<Venta> findByIdAndEstadoVenta(Long id, EstadoVenta estadoVenta);

    boolean existsBySucursalIdAndEstadoVenta(Long id, EstadoVenta estadoVenta);

    boolean existsByDetalleVentasProductoIdAndEstadoVenta(Long id, EstadoVenta estadoVenta);


}
