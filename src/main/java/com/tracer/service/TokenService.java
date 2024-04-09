package com.tracer.service;

import com.tracer.exception.ExpiredTokenException;
import com.tracer.model.tokens.Token;
import com.tracer.repository.tokens.TokenRepository;
import com.tracer.util.JwtUtils;
import com.tracer.util.KeyGeneratorUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private TokenRepository tokenRepository;

    private final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private static final int TOKEN_LENGTH = 6;

    public String generateJwt(Authentication auth){
        logger.info("beginning creation of JWT");
        Instant now = Instant.now();

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(auth.getName())
                .claim("roles", scope)
                .issuedAt(now)
                .expiresAt(now.plusMillis(1800000))
                .build();
        String jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        tokenRepository.save(
                new Token(auth.getName(), jwt,  false, LocalDateTime.now().plusHours(1))
        );
        return jwt;
    }

    public String generateRefreshToken(Authentication auth) {
        logger.info("beginning creation of refresh token");
        String username = auth.getName();
        var token = tokenRepository.findByUsername(username)
                .orElseThrow();
        if (token.isExpired()) throw new ExpiredTokenException("token is expired");
        invalidateUserToken(username);
        return generateJwt(auth);
    }
    public String generateEmailVerificationToken() {
        logger.info("beginning creation of email token");
        byte[] randomBytes = new byte[TOKEN_LENGTH];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
    public void invalidateUserToken(String username) {
        var tokens = tokenRepository.findAllByUsername(username);
        if (!tokens.isEmpty()) {
            tokens.parallelStream().forEach(token -> {
                token.setExpired(true);
            });
        }
        tokenRepository.saveAll(tokens);
    }

    public void validateToken(HttpServletRequest servletRequest, Authentication auth) {
        String jwt = JwtUtils.extractJwtFromRequest(servletRequest);
        Jwt webToken = jwtDecoder.decode(jwt);
        Long expirationTime = JwtUtils.getTokenExpiration(webToken);
        var token = tokenRepository.findByUsernameAndJwt(auth.getName() , jwt)
                .orElseThrow(ExpiredTokenException::new);
        Long currentTime = System.currentTimeMillis();
        if (expirationTime < currentTime || token.isExpired()) {
            token.setExpired(true);
            tokenRepository.save(token);
            throw new ExpiredTokenException("Token has Expired.");
        }
    }
}