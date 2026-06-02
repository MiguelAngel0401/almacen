package com.miguel.almacen.controllers;

import com.miguel.almacen.dto.sucursales.SucursalRequest;
import com.miguel.almacen.dto.sucursales.SucursalResponse;
import com.miguel.almacen.services.SucursalesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@AllArgsConstructor
@Validated
public class SucursalController {

    private final SucursalesService sucursalService;

    @GetMapping
    public ResponseEntity<List<SucursalResponse>>listar(){
        return ResponseEntity.ok(sucursalService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalResponse> obtenerPorId(@PathVariable @Positive(message =
    "El ID debe ser positivo") Long id){
        return ResponseEntity.ok(sucursalService.obtnerPorId(id));
    }

    @PostMapping
    public ResponseEntity<SucursalResponse> registrar(@Valid @RequestBody SucursalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sucursalService.registrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponse> actualizar(@PathVariable @Positive(message =
            "El ID debe ser positivo") Long id, @Valid @RequestBody SucursalRequest request){
        return ResponseEntity.ok(sucursalService.actualizar(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable @Positive(message =
            "El ID debe ser positivo") Long id){
        sucursalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
