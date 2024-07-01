package com.tracer.repository.tokens;

import com.tracer.model.tokens.EmailToken;
import com.tracer.model.users.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {
    Optional<EmailToken> findByToken(String token);
    @Transactional
    void deleteAllByTeacher(Teacher teacher);
}
