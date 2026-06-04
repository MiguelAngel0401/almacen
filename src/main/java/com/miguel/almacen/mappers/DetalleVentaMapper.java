package com.miguel.almacen.mappers;

import com.miguel.almacen.dto.ventas.DetalleVentaRequest;
import com.miguel.almacen.dto.ventas.DetalleVentaResponse;
import com.miguel.almacen.entities.DetalleVenta;
import com.miguel.almacen.entities.Producto;
import org.springframework.stereotype.Component;

@Component
public class DetalleVentaMapper {

    public DetalleVenta requestAEntidad(DetalleVentaRequest request, Producto producto) {
        if (request == null) return null;

        return DetalleVenta.builder()
                .producto(producto)
                .cantidadProducto(request.cantidadProducto())
                .precioProducto(producto.getPrecio())
                .build();
    }

    public DetalleVentaResponse entidadAResponse(DetalleVenta entidad) {
        if (entidad == null) return null;

        return new DetalleVentaResponse(
                entidad.getProducto().getId(),
                entidad.getProducto().getNombre(),
                entidad.getCantidadProducto(),
                entidad.getPrecioProducto(),
                entidad.calcularSubtotal()
        );
    }
}
