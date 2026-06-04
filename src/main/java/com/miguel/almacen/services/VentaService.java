package com.miguel.almacen.services;


import com.miguel.almacen.dto.ventas.VentaRequest;
import com.miguel.almacen.dto.ventas.VentaResponse;

import java.util.List;

public interface VentaService {
    List<VentaResponse> listar();

    VentaResponse obtenerPorId(Long id);

    VentaResponse registrar(VentaRequest request);

    List<VentaResponse> listarCanceladas();

    void cancelar(Long id);
}
