package com.tracer.service;
import com.tracer.model.tokens.EmailToken;
import com.tracer.model.tokens.PasswordResetToken;
import com.tracer.model.users.Teacher;
import com.tracer.repository.tokens.EmailTokenRepository;
import com.tracer.repository.tokens.PasswordResetTokenRepository;
import com.tracer.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class MailSenderServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private EmailTokenRepository emailTokenRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private MailSenderService mailSenderService;

    private static final Teacher TEST_TEACHER = new Teacher("username", "password");

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    @Test
    void generateNewPasswordResetToken_shouldGenerateToken() {
        // GIVEN
        TEST_TEACHER.setEmail("test@example.com");

        // WHEN
        when(tokenService.generateEmailVerificationToken()).thenReturn("testToken");
        when(teacherRepository.findByEmail("test@example.com")).thenReturn(Optional.of(TEST_TEACHER));
        when(passwordResetTokenRepository.save(any())).thenReturn(
                new PasswordResetToken("testToken", "test@example.com",
                        LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), TEST_TEACHER)
        );
        PasswordResetToken passwordResetToken = mailSenderService.generateNewPasswordResetToken("test@example.com");

        // THEN
        assertNotNull(passwordResetToken);
        assertEquals("testToken", passwordResetToken.getToken());
        verify(passwordResetTokenRepository, times(1)).save(any(PasswordResetToken.class));
    }

    @Test
    void generateNewEmailToken_shouldGenerateToken() {
        // GIVEN
        TEST_TEACHER.setEmail("test@example.com");

        // THEN
        when(tokenService.generateEmailVerificationToken()).thenReturn("testToken");
        when(teacherRepository.findByEmail(any())).thenReturn(java.util.Optional.of(TEST_TEACHER));
        when(emailTokenRepository.save(any())).thenReturn(
                new EmailToken("testToken", TEST_TEACHER,
                        LocalDateTime.now(), LocalDateTime.now().plusMinutes(1))
        );
        EmailToken emailToken = mailSenderService.generateNewEmailToken("test@example.com");

        // THEN
        assertNotNull(emailToken);
        assertEquals("testToken", emailToken.getToken());
        verify(emailTokenRepository, times(1)).save(any(EmailToken.class));
    }
}