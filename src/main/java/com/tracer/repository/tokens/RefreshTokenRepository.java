package com.tracer.repository.tokens;

import com.tracer.model.tokens.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUsernameAndToken(String username, String token);
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findAllByUsername(String username);
}
