package com.miguel.almacen.services;

import com.miguel.almacen.dto.ventas.DetalleVentaRequest;
import com.miguel.almacen.dto.ventas.VentaRequest;
import com.miguel.almacen.dto.ventas.VentaResponse;
import com.miguel.almacen.entities.DetalleVenta;
import com.miguel.almacen.entities.Producto;
import com.miguel.almacen.entities.Sucursal;
import com.miguel.almacen.entities.Venta;
import com.miguel.almacen.enums.EstadoVenta;
import com.miguel.almacen.exceptions.RecursoNoEncontradoException;
import com.miguel.almacen.mappers.VentaMapper;
import com.miguel.almacen.repositories.ProductoRepository;
import com.miguel.almacen.repositories.SucursalRepository;
import com.miguel.almacen.repositories.VentaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    private final VentaMapper ventaMapper;

    private final ProductoRepository productoRepository;

    private final SucursalRepository sucursalRepository;

    @Override
    public List<VentaResponse> listar() {
        log.info("Listando las ventas");
        return ventaRepository.findByEstadoVenta(EstadoVenta.REGISTRADA).stream()
                .map(ventaMapper::entidadAResponse).toList();
    }

    @Override
    public VentaResponse obtenerPorId(Long id) {
        return ventaMapper.entidadAResponse(obtenerVentaOException(id));
    }

    @Override
    public VentaResponse registrar(VentaRequest request) {
            Sucursal sucursal = sucursalRepository.findById(request.idSucursal())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada"));

            Venta venta = ventaMapper.requestAEntidad(request, sucursal);

            for (DetalleVentaRequest dto : request.productos()) {
                Producto producto = productoRepository.findById(dto.idProducto())
                        .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + dto.idProducto()));

                producto.descontarCantidad(dto.cantidadProducto());

                DetalleVenta detalle = DetalleVenta.builder()
                        .producto(producto)
                        .cantidadProducto(dto.cantidadProducto())
                        .precioProducto(producto.getPrecio())
                        .build();
                venta.agregarDetalle(detalle);
            }
            return ventaMapper.entidadAResponse(ventaRepository.save(venta));
    }

    @Override
    public List<VentaResponse> listarCanceladas() {
        log.info("Listando ventas canceladas");
        return ventaRepository.findByEstadoVenta(EstadoVenta.CANCELADA).stream()
                .map(ventaMapper::entidadAResponse).toList();
    }

    @Override
    public void cancelar(Long id) {
        log.info("Cancelando venta ID: {}", id);
        Venta venta = obtenerVentaOException(id);

        for (DetalleVenta detalle : venta.getDetalleVentas()) {
            Producto producto = detalle.getProducto();
            producto.aumentarCantidad(detalle.getCantidadProducto());
        }
        venta.cancelar();
    }

    private Venta obtenerVentaOException(Long id){
        log.info("Buscando venta con id: {} ", id);
        return ventaRepository.findByIdAndEstadoVenta(id, EstadoVenta.REGISTRADA).orElseThrow(() ->
                new RecursoNoEncontradoException("Venta no encontrada con id: " + id));
    }
}
