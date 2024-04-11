package com.tracer.service;

import com.tracer.exception.ExpiredTokenException;
import com.tracer.model.tokens.RefreshToken;
import com.tracer.model.tokens.Token;
import com.tracer.repository.TeacherRepository;
import com.tracer.repository.tokens.RefreshTokenRepository;
import com.tracer.repository.tokens.TokenRepository;
import com.tracer.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TokenService {
    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private static final int TOKEN_LENGTH = 6;

    /**
     * Creates a new JWT with the authentication object.
     * @param auth contains information needed to create JWT
     * @return A new JWT.
     */
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
                .expiresAt(now.plusMillis(3600000))
                .build();
        String jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        tokenRepository.save(
                new Token(auth.getName(), jwt,  false, LocalDateTime.now().plusHours(1))
        );
        return jwt;
    }

    public String generateRefreshToken(Authentication auth) {
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
                .expiresAt(now.plusMillis(25200000))
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        refreshTokenRepository.save(
                new RefreshToken(auth.getName() , token)
        );
        return token;
    }
    public String refreshToken(String token) {
        var refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow();
        validateRefreshToken(token);
        String username = refreshToken.getUsername();
        invalidateAllUserToken(username);
        var teacher = teacherRepository.findByUsername(username)
                .orElseThrow();

        return generateJwt(new UsernamePasswordAuthenticationToken(
                teacher, null, teacher.getAuthorities()
        ));
    }

    /**
     * Creates a new email token to send the user.
     * @return a string of characters.
     */
    public String generateEmailVerificationToken() {
        logger.info("beginning creation of email token");
        byte[] randomBytes = new byte[TOKEN_LENGTH];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * Makes all JWTs associated with a given user invalid.
     * @param username user's username.
     */
    public void invalidateAllUserToken(String username) {
        var tokens = tokenRepository.findAllByUsername(username);
        if (!tokens.isEmpty()) {
            tokens.parallelStream().forEach(token -> {
                if (token.isExpired())  {
                    tokenRepository.delete(token);
                } else {
                    token.setValid(false);
                    tokenRepository.save(token);
                }
            });
        }
    }

    public void invalidateUserToken(String jwt) {
        var token = tokenRepository.findByJwt(jwt)
                .orElseThrow();
        if (token.isExpired())  {
            tokenRepository.delete(token);
        } else {
            token.setValid(false);
            tokenRepository.save(token);
        }
    }

    /**
     * Custom JWT validation method, validating expiration and if the token matches the
     * user.
     * @param servletRequest request body and headers to be used.
     * @param auth used to get the username.
     */
    public void validateJwt(HttpServletRequest servletRequest, Authentication auth) {
        logger.info("made it here");
        String jwt = JwtUtils.extractJwtFromRequest(servletRequest);
        var token = tokenRepository.findByUsernameAndJwt(auth.getName() , jwt)
                .orElseThrow(ExpiredTokenException::new);
        if (JwtUtils.isTokenExpired(jwtDecoder.decode(jwt)) || !token.isValid()) {
            logger.error("token invalid");
            token.setExpired(true);
            token.setValid(false);
            tokenRepository.save(token);
            throw new ExpiredTokenException("Token has Expired.");
        }
    }

    public void validateRefreshToken(String token) {
        var refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow();
        Jwt jwt = jwtDecoder.decode(token);
        if (!refreshToken.isValid() || JwtUtils.isTokenExpired(jwt)) {
            refreshToken.setValid(false);
            refreshToken.setExpired(true);
            throw new ExpiredTokenException("refreshToken has expired.");
        }
    }
}