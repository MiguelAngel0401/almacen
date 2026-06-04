package com.miguel.almacen.services;

import com.miguel.almacen.dto.ventas.VentaRequest;
import com.miguel.almacen.dto.ventas.VentaResponse;
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
    @Override
    public List<VentaResponse> listar() {
        return List.of();
    }

    @Override
    public VentaResponse obtenerPorId(Long id) {
        return null;
    }

    @Override
    public VentaResponse registrar(VentaRequest request) {
        return null;
    }

    @Override
    public List<VentaResponse> listarCanceladas() {
        return List.of();
    }

    @Override
    public void cancelar(Long id) {

    }
}
