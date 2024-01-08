package com.tracer.service;

import com.tracer.model.Role;
import com.tracer.model.Teacher;
import com.tracer.model.response.LoginResponse;
import com.tracer.repository.AuthorityRepository;
import com.tracer.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

public class AuthenticationServiceTest {
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
        testTeacher.setStudents(new HashSet<>());
        testTeacher.setUsername(username);
        testTeacher.setPassword(encodedPassword);
        // WHEN
        when(tokenService.generateJwt(any())).thenReturn(mockToken);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(authorityRepository.findByAuthority("TEACHER")).thenReturn(Optional.of(role));
        when(teacherService.saveNewTeacher(any())).thenReturn(testTeacher);
        LoginResponse response = authenticationService.registerUser(username, email, password);

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
        testTeacher.setStudents(new HashSet<>());

        // WHEN
        when(tokenService.generateJwt(any())).thenReturn(mockToken);
        when(teacherService.loadUserByUsername(username)).thenReturn(testTeacher);
        LoginResponse response = authenticationService.loginUserByUsername(username, password);

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
            LoginResponse result = authenticationService.loginUserByUsername(username, password);
        });
        verify(tokenService).generateJwt(any());
        verify(teacherService).loadUserByUsername(username);
    }
}
