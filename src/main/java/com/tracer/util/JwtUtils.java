package com.tracer.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public class JwtUtils {
    public static String extractJwtFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public static Long getTokenExpiration(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            String token = (String) principal;
            Claims claims = Jwts.parser().parseClaimsJws(token).getBody();
            return claims.getExpiration().getTime();
        }
        return null;
    }
}
