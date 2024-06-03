package com.tracer.service;

import com.tracer.model.tokens.EmailToken;
import com.tracer.model.tokens.PasswordResetToken;
import com.tracer.model.Role;
import com.tracer.model.Teacher;
import com.tracer.model.response.TeacherLoginResponse;
import com.tracer.repository.AuthorityRepository;
import com.tracer.repository.tokens.EmailTokenRepository;
import com.tracer.repository.tokens.PasswordResetTokenRepository;
import com.tracer.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

public class AuthenticationServiceTest {
    @Mock
    private EmailTokenRepository emailTokenRepository;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Mock
    private TeacherService teacherService;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private Authentication authentication;
    @Mock
    private MailSenderService mailSenderService;
    @InjectMocks
    private AuthenticationService authenticationService;
    private final Teacher testTeacher = new Teacher();
    private final Role role = new Role("TEACHER");

    @BeforeEach
    public void setup() {
        initMocks(this);
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
    }

    @Test
    public void registerUser_validRequest_Teacher() {
        // GIVEN
        String username = "james";
        String email = "example@gmail.com";
        String password = "password";
        String mockToken = "mockToken";
        String encodedPassword = passwordEncoder.encode(password);
        testTeacher.setStudents(new ArrayList<>());
        testTeacher.setUsername(username);
        testTeacher.setPassword(encodedPassword);
        // WHEN
        when(tokenService.generateJwt(any())).thenReturn(mockToken);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(authorityRepository.findByAuthority("TEACHER")).thenReturn(Optional.of(role));
        when(teacherService.saveNewTeacher(any())).thenReturn(testTeacher);
        TeacherLoginResponse response = authenticationService.registerUser(username, email, password);

        // THEN
        assertNotNull(response, "expected response to NOT be null");
        assertEquals(response.getTeacherUsername(), testTeacher.getUsername(), String.format("expected username to be: %s but got: %s", testTeacher.getUsername(), response.getTeacherUsername()));
        assertNotNull(response.getJwt(), "expected there to be a JWT");
    }

    @Test
    public void loginUser_validRequest_LoginResponse() {
        // GIVEN
        String username = "james";
        String password = "Password";
        String mockToken = "mockToken";
        testTeacher.setStudents(new ArrayList<>());

        // WHEN
        when(tokenService.generateJwt(any())).thenReturn(mockToken);
        when(teacherService.loadUserByUsername(username)).thenReturn(testTeacher);
        TeacherLoginResponse response = authenticationService.loginTeacherByUsername(username, password);

        // THEN
        assertNotNull(response);
        assertEquals(response.getTeacherUsername(), testTeacher.getUsername(), String.format("expected username to be: %s but got: %s", testTeacher.getUsername(), response.getTeacherUsername()));
        assertEquals(mockToken, response.getJwt());
        verify(authenticationManager).authenticate(argThat(auth ->
                auth instanceof UsernamePasswordAuthenticationToken
                        && ((UsernamePasswordAuthenticationToken) auth).getPrincipal().equals(username)
                        && ((UsernamePasswordAuthenticationToken) auth).getCredentials().equals(password)));
        verify(tokenService).generateJwt(any());
        verify(teacherService).loadUserByUsername(username);
    }

    @Test
    public void loginUser_InvalidUser_NullPointer() {
        String username = "james";
        String password = "Password";
        String mockToken = "mockToken";

        // WHEN
        when(tokenService.generateJwt(any())).thenReturn(mockToken);

        // THEN
        assertThrows(NullPointerException.class, ()-> {
            TeacherLoginResponse result = authenticationService.loginTeacherByUsername(username, password);
        });
        verify(tokenService).generateJwt(any());
        verify(teacherService).loadUserByUsername(username);
    }

    @Test
    public void verifyEmail_validToken_emailVerified() {
        // GIVEN
        testTeacher.setUsername("test");
        EmailToken emailToken = new EmailToken("12345", testTeacher, LocalDateTime.now()
        ,LocalDateTime.now().plusMinutes(10));
        String validToken = "12345";

        // WHEN
        when(emailTokenRepository.findByToken("12345")).thenReturn(Optional.of(emailToken));
        String response = authenticationService.verifyEmail(validToken, testTeacher.getUsername());

        // THEN
        assertNotNull(response);
        assertTrue(testTeacher.isEmailVerified());
        assertEquals("Email verified", response);
    }

    @Test
    public void verifyEmail_expiredToken_exception() {
        // GIVEN
        testTeacher.setUsername("test");
        EmailToken expiredEmailToken = new EmailToken("12345", testTeacher, LocalDateTime.now().plusMinutes(5)
                ,LocalDateTime.now().minusMinutes(5));
        String validToken = "12345";

        // WHEN / THEN
        when(emailTokenRepository.findByToken("12345")).thenReturn(Optional.of(expiredEmailToken));
        assertThrowsExactly(IllegalStateException.class, () ->{
            authenticationService.verifyEmail(validToken, testTeacher.getUsername());
        }, "Token Expired");
    }

    @Test
    public void verifyEmail_InvalidToken_exception() {
        // GIVEN
        testTeacher.setUsername("test");
        EmailToken invalidEmailToken = new EmailToken();
        invalidEmailToken.setToken("12345");
        invalidEmailToken.setCreatedAt(LocalDateTime.now());
        invalidEmailToken.setExpiresAt(LocalDateTime.now().plusMinutes(1));
        String validToken = "12345";

        // WHEN / THEN
        when(emailTokenRepository.findByToken("12345")).thenReturn(Optional.of(invalidEmailToken));
        assertThrowsExactly(IllegalStateException.class, () ->{
            authenticationService.verifyEmail(validToken, testTeacher.getUsername());
            verify(IllegalStateException.class);
        }, "Token not associated with a teacher");
    }

    @Test
    public void verifyEmail_emailAlreadyVerified_exception() {
        // GIVEN
        testTeacher.setUsername("test");
        testTeacher.setEmailVerified(true);
        EmailToken alreadyVerifiedToken = new EmailToken("12345", testTeacher, LocalDateTime.now().plusMinutes(5)
                ,LocalDateTime.now());
        String validToken = "12345";

        // WHEN / THEN
        when(emailTokenRepository.findByToken("12345")).thenReturn(Optional.of(alreadyVerifiedToken));
        assertThrowsExactly(IllegalStateException.class, () ->{
            authenticationService.verifyEmail(validToken, testTeacher.getUsername());
            verify(IllegalStateException.class);
        }, "Email already verified");
    }

    @Test
    public void verifyEmail_nullToken_exception() {
        // GIVEN
        String invalidToken = "12345";

        // WHEN / THEN
        assertThrowsExactly(RuntimeException.class, () ->{
            authenticationService.verifyEmail(invalidToken, testTeacher.getUsername());
            verify(RuntimeException.class);
        }, "Invalid token");
    }

    @Test
    public void completePasswordReset_validRequest_loginResponse() {
        // GIVEN
        PasswordResetToken passwordResetToken = new PasswordResetToken("12345", "example@gmail.com",
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), testTeacher);
        String validToken = "12345";
        String newPassword = "new_password";
        String encodedPassword = passwordEncoder.encode(newPassword);
        testTeacher.setUsername("username");

        // WHEN
        when(passwordResetTokenRepository.findByToken("12345")).thenReturn(Optional.of(passwordResetToken));
        TeacherLoginResponse response = authenticationService.completePasswordReset(validToken, newPassword);

        // THEN
        assertNotNull(response);
        assertEquals(encodedPassword, testTeacher.getPassword());
        assertEquals("username", response.getTeacherUsername());
        verify(passwordEncoder, times(2)).encode(any());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    public void completePasswordReset_expiredToken_exception() {
        // GIVEN
        PasswordResetToken expiredToken = new PasswordResetToken("12345", "example@gmail.com",
                LocalDateTime.now().plusMinutes(10), LocalDateTime.now().minusMinutes(5), testTeacher);
        String validToken = "12345";
        String validPassword = "password";

        // WHEN / THEN
        when(passwordResetTokenRepository.findByToken("12345")).thenReturn(Optional.of(expiredToken));
        assertThrowsExactly(IllegalStateException.class, () ->{
            authenticationService.completePasswordReset(validToken, validPassword);
        }, "Token Expired");
    }

    @Test
    public void completePasswordReset_InvalidToken_exception() {
        // GIVEN
        PasswordResetToken invalidToken = new PasswordResetToken();
        invalidToken.setToken("12345");
        invalidToken.setCreatedAt(LocalDateTime.now());
        invalidToken.setExpiresAt(LocalDateTime.now().plusMinutes(1));
        String validToken = "12345";
        String validPassword = "password";

        // WHEN / THEN
        when(passwordResetTokenRepository.findByToken("12345")).thenReturn(Optional.of(invalidToken));
        assertThrowsExactly(IllegalStateException.class , () -> {
            authenticationService.completePasswordReset(validToken , validPassword);
            verify(IllegalStateException.class);
        } , "Token not associated with a teacher");
    }

    @Test
    public void completePasswordReset_nullToken_exception() {
        // GIVEN
        String invalidToken = "12345";
        String validPassword = "password";

        // WHEN / THEN
        assertThrowsExactly(NullPointerException.class, () ->{
            authenticationService.completePasswordReset(invalidToken, validPassword);
            verify(NullPointerException.class);
        }, "Invalid token");
    }
}
