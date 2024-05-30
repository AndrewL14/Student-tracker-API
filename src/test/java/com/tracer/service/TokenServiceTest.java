package com.tracer.service;

import com.tracer.model.Teacher;
import com.tracer.repository.AuthorityRepository;
import com.tracer.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

public class TokenServiceTest {

    @MockBean
    private JwtEncoder jwtEncoder;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @InjectMocks
    private TokenService tokenService;

    private final Teacher TEST_TEACHER = new Teacher("username", "password");

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    @Test
    public void generateEmailVerificationToken() {
        // GIVEN
        int expectedTokenLength = 8;

        // WHEN
        String response = tokenService.generateEmailVerificationToken();

        // THEN
        assertNotNull(response);
        assertEquals(expectedTokenLength, response.length());
    }


}
