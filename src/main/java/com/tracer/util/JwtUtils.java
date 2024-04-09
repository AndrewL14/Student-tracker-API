package com.tracer.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.Objects;
@UtilityClass
public class JwtUtils {
    @Autowired
    private JwtDecoder jwtDecoder;
    public static String extractJwtFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public static Long getTokenExpiration(Jwt jwt) {
        return Objects.requireNonNull(jwt.getExpiresAt()).toEpochMilli();
    }
}
