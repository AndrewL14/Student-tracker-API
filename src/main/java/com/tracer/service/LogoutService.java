package com.tracer.service;

import com.tracer.model.tokens.RefreshToken;
import com.tracer.repository.tokens.RefreshTokenRepository;
import com.tracer.repository.tokens.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * A Custom Logout method invalidating any tokens created during users session.
     * @param request HTTP request sent by user.
     * @param response HTTP response sent by server.
     * @param authentication N\A.
     */
    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByJwt(jwt)
                .orElse(null);
        if (storedToken != null) {
            var refreshTokens = refreshTokenRepository.findAllByUsername(storedToken.getUsername());
            for (RefreshToken refreshToken : refreshTokens) {
                refreshToken.setValid(false);
                refreshToken.setExpired(true);
            }
            storedToken.setExpired(true);
            storedToken.setValid(false);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}