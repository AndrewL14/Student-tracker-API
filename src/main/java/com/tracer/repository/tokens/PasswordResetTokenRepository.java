package com.tracer.repository.tokens;

import com.tracer.model.tokens.PasswordResetToken;
import com.tracer.model.users.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    @Transactional
    void deleteAllByTeacher(Teacher teacher);
}
