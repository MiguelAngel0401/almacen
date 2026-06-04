package com.miguel.almacen.mappers;

import com.miguel.almacen.dto.ventas.DetalleVentaResponse;
import com.miguel.almacen.dto.ventas.VentaRequest;
import com.miguel.almacen.dto.ventas.VentaResponse;
import com.miguel.almacen.entities.DetalleVenta;
import com.miguel.almacen.entities.Sucursal;
import com.miguel.almacen.entities.Venta;
import com.miguel.almacen.enums.EstadoVenta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class VentaMapper {

    private final SucursalMapper sucursalMapper;

    private final DetalleVentaMapper detalleVentaMapper;

    public Venta requestAEntidad(VentaRequest request, Sucursal sucursal){
        if (request == null) return null;

        return Venta.builder()
                .estadoVenta(EstadoVenta.REGISTRADA)
                .fecha(LocalDate.now())
                .sucursal(sucursal)
                .build();
    }

    public VentaResponse entidadAResponse(Venta entidad){
        if(entidad == null) return null;

        List<DetalleVentaResponse> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        if (entidad.getDetalleVentas() != null) {
            for (DetalleVenta detalle : entidad.getDetalleVentas()) {
                DetalleVentaResponse detalleResponse = detalleVentaMapper.entidadAResponse(detalle);
                detalles.add(detalleResponse);
                total = total.add(detalleResponse.subtotal());
            }
        }

        return new VentaResponse(
                entidad.getId(),
                entidad.getFecha().toString(),
                entidad.getEstadoVenta().getDescripcion(),
                sucursalMapper.entidadAResponse(entidad.getSucursal()),
                detalles,
                total
        );
    }
}
