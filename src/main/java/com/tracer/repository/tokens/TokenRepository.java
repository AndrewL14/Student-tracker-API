package com.tracer.repository.tokens;

import com.tracer.model.tokens.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUsername(String username);
    List<Token> findAllByUsername(String username);
    Optional<Token> findByUsernameAndJwt(String username, String jwt);
    Optional<Token> findByJwt(String jwt);
}
