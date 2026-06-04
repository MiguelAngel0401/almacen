package com.miguel.almacen.mappers;

import com.miguel.almacen.dto.ventas.VentaRequest;
import com.miguel.almacen.dto.ventas.VentaResponse;
import com.miguel.almacen.entities.Sucursal;
import com.miguel.almacen.entities.Venta;
import com.miguel.almacen.enums.EstadoVenta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class VentaMapper {

    private final SucursalMapper sucursalMapper;

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

        return new VentaResponse(
                entidad.getId(),
                entidad.getFecha().toString(),
                entidad.getEstadoVenta().getDescripcion(),
                sucursalMapper.entidadAResponse(entidad.getSucursal()),
                null,
                null
        );
    }
}
