package com.tracer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    public String generateJwt(Authentication auth){
        Instant now = Instant.now();

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(auth.getName())
                .claim("roles", scope)
                .expiresAt(now.plusSeconds(18000))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public boolean validateJwt(String token, String expectedUsername) {
        try {
            Jwt claim = jwtDecoder.decode(token);
            String tokenUsername = claim.getSubject();

            return isTokenExpired(token) && tokenUsername.equals(expectedUsername);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            Jwt claim = jwtDecoder.decode(token);
            Instant expirationTime = claim.getExpiresAt();
            assert expirationTime != null;
            return !expirationTime.isBefore(Instant.now());
        } catch (Exception e){
            return false;
        }
    }

}