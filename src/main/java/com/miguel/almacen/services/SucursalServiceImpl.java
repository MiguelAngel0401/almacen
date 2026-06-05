package com.miguel.almacen.services;

import com.miguel.almacen.dto.sucursales.SucursalRequest;
import com.miguel.almacen.dto.sucursales.SucursalResponse;
import com.miguel.almacen.entities.Sucursal;
import com.miguel.almacen.enums.EstadoVenta;
import com.miguel.almacen.exceptions.RecursoNoEncontradoException;
import com.miguel.almacen.mappers.SucursalMapper;
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

public class SucursalServiceImpl implements SucursalesService {

    private final SucursalRepository sucursalRepository;

    private final SucursalMapper sucursalMapper;

    private final VentaRepository ventaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> listar() {
        log.info("Listando todas las sucursales");
        return sucursalRepository.findAll().stream()
                //.map( entidad -> sucursalMapper.entidadAResponse(entidad))
                .map(sucursalMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalResponse obtnerPorId(Long id) {
        return sucursalMapper.entidadAResponse(obtenerSucursalIOException(id));
    }

    @Override
    public SucursalResponse registrar(SucursalRequest request) {
        log.info("Registrando nueva sucursal ...");

        validarDatosUnicos(request);

        Sucursal sucursal = sucursalMapper.requestAEntidad(request);

        sucursalRepository.save(sucursal);

        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public SucursalResponse actualizar(SucursalRequest request, Long id) {
        Sucursal sucursal = obtenerSucursalIOException(id);
        log.info("Actualizando sucursal con id: {}", id);

        validarCambiosUnicos(request, id);

        sucursal.actualizar(request.nombre(), request.direccion());

        log.info("Sucursal con id {} actualizada correctamente" ,id);

        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public void eliminar(Long id) {
        Sucursal sucursal = obtenerSucursalIOException(id);

        if (ventaRepository.existsBySucursalIdAndEstadoVenta(id, EstadoVenta.REGISTRADA))
            throw new IllegalArgumentException("No se puede eliminar la sucursal, tiene ventas registradas");

        log.info("Eliminando sucursal con id: {}", id);
        sucursalRepository.delete(sucursal);
        log.info("Sucursal con id {} eliminada", id);
    }

    private Sucursal obtenerSucursalIOException(Long id){
        log.info("Buscando sucursal con id: {}", id);
        return sucursalRepository.findById(id).orElseThrow(() ->
        new RecursoNoEncontradoException("Sucursal no encontrada con id: " +id));
    }

    private void validarDatosUnicos(SucursalRequest request){
        log.info("Validando nombre unico ...");
        if(sucursalRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new IllegalArgumentException("Ya existe una sucursal con el nombre de: " +request.nombre());
    }

    private void validarCambiosUnicos(SucursalRequest request, Long id) {
        log.info("Validando cambio en nombre unico...");
        if (sucursalRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(), id))
            throw new IllegalArgumentException("Ya existe una sucursal con el nombre de : " + request.nombre());
    }
}
