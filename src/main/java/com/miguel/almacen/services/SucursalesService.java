package com.miguel.almacen.services;

import com.miguel.almacen.dto.sucursales.SucursalRequest;
import com.miguel.almacen.dto.sucursales.SucursalResponse;
import com.miguel.almacen.entities.Sucursal;

import java.util.List;

public interface SucursalesService {

    List<SucursalResponse> listar();

    SucursalResponse obtnerPorId(Long id);

    SucursalResponse registrar(SucursalRequest request);

    SucursalResponse actualizar(SucursalRequest request, Long id);

    void eliminar(Long id);
}
