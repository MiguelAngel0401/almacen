package com.miguel.almacen.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) {
}
