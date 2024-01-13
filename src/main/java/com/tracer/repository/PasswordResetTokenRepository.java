package com.tracer.repository;

import com.tracer.model.PasswordResetToken;
import com.tracer.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    @Transactional
    void deleteAllByTeacher(Teacher teacher);
}
