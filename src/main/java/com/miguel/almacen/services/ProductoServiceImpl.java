package com.miguel.almacen.services;

import com.miguel.almacen.dto.productos.ProductoRequest;
import com.miguel.almacen.dto.productos.ProductoResponse;
import com.miguel.almacen.entities.Producto;
import com.miguel.almacen.enums.Categoria;
import com.miguel.almacen.enums.EstadoVenta;
import com.miguel.almacen.exceptions.RecursoNoEncontradoException;
import com.miguel.almacen.mappers.ProductoMapper;
import com.miguel.almacen.repositories.ProductoRepository;
import com.miguel.almacen.repositories.VentaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ProductoServiceImpl implements ProductoService{

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    private final VentaRepository ventaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(String nombre, String categoria, BigDecimal precioMin, BigDecimal precioMax) {
        log.info("Listando productos con filtros");

        return productoRepository.findAll().stream()
                .filter(p -> nombre == null || p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .filter(p -> {
                    if (categoria == null || categoria.trim().isEmpty()) {
                        return true;
                    }
                    Categoria cat = Categoria.obtenerCategoriaPorDescripcion(categoria.trim());
                    return p.getCategoria() == cat;
                })
                .filter(p -> precioMin == null || p.getPrecio().compareTo(precioMin) >= 0)
                .filter(p -> precioMax == null || p.getPrecio().compareTo(precioMax) <= 0)
                .map(productoMapper::entidadAResponse)
                .toList();
    }

    @Override
    public ProductoResponse obtenerPorId(Long id) {
        return productoMapper.entidadAResponse(obtenerProductoOException(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {
        log.info("Registrando nuevo producto ...");

        Categoria categoria = Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim());
        Producto producto = productoMapper.requestAEntidad(request, categoria);

        productoRepository.save(producto);

        log.info("Nuevo producto {} resgitrado", producto.getNombre());

        return productoMapper.entidadAResponse(producto);

    }

    @Override
    public ProductoResponse actualizar(ProductoRequest request, Long id) {
        Producto producto = obtenerProductoOException(id);

        log.info("Actualizando producto con id: {}", id);

        producto.actualizar(
                request.nombre(),
                Categoria.obtenerCategoriaPorDescripcion(request.categoria()),
                request.precio(),
                request.cantidad()
        );

        log.info("Producto {} actualizado", producto.getNombre());

        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = obtenerProductoOException(id);

        if (ventaRepository.existsByDetalleVentasProductoIdAndEstadoVenta(id, EstadoVenta.REGISTRADA))
            throw new IllegalArgumentException("No se puede eliminar el producto, tiene ventas registradas");

        log.info("Eliminado producto con id: {}", id);
        productoRepository.delete(producto);
        log.info("Producto con id {} eliminado", id);
    }

    private Producto obtenerProductoOException(Long id){
        log.info("Buscando producto con id: {}", id);
        return productoRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Producto no encontrado con id: " + id));
    }
}
