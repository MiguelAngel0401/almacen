package com.miguel.almacen.dto.sucursales;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SucursalRequest(
        @NotBlank(message = "El nombre es requerdio")
        @Size(min =5, max=50, message = "El nombre debe tener entre 5 y 50 Caracteres")
        String nombre,

        @NotBlank(message = "La direccion es requerida")
        @Size(min = 10, max = 150, message = "La direccion debe tener entre 10 y 150 caracteres")
        String direccion

) {

}
