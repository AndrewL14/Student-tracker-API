package com.tracer.model.response;

public record StudentLoginResponse(
        String name,
        String jwt,
        String refreshToken
) {
}
