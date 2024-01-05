package com.tracer.service;

import com.tracer.model.EmailToken;
import com.tracer.model.Role;
import com.tracer.model.Student;
import com.tracer.model.Teacher;
import com.tracer.model.response.LoginResponse;
import com.tracer.repository.AuthorityRepository;
import com.tracer.repository.EmailTokenRepository;
import com.tracer.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private AuthorityRepository roleRepository;
    @Autowired
    private EmailTokenRepository emailTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private TokenService tokenService;

    /**
     * Creates and saves a new teacher object using the provided username and password.
     * @param username A unique combination of characters to be used.
     * @param password A string of characters to be used.
     * @return A loginResponse containing a new List of students, and a valid JWT.
     */
    public LoginResponse registerUser(String username, String email, String password){

        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("TEACHER").get();

        Set<Role> authorities = new HashSet<>();

        authorities.add(userRole);
        Teacher teacher = new Teacher(username, email, encodedPassword, new HashSet<>(),
                authorities);
        teacherService.saveNewTeacher(teacher);
        mailSenderService.SendEmailVerification(email);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        String token = tokenService.generateJwt(authentication);
        return new LoginResponse(teacher.getUsername(), token);
    }

    /**
     * Checks if a teacher object containing matching username and password, then
     * generates a new JWT, finally returns a loginResponse.
     * @param username A unique combination of characters to be used.
     * @param password A string of characters to be used.
     * @return A loginResponse containing a new List of students, and a valid JWT.
     */
    public LoginResponse loginUserByUsername(String username, String password){

        Authentication authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(username, password)
        );
        String token = tokenService.generateJwt(authentication);
        Teacher teacher = (Teacher) teacherService.loadUserByUsername(username);
        return new LoginResponse(teacher.getUsername(), token);
    }

    public LoginResponse loginUserByEmail(String email, String password) {
        Teacher teacher = (Teacher) teacherService.loadUserByEmail(email);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(teacher.getUsername(), password)
        );
        String token = tokenService.generateJwt(authentication);
        return new LoginResponse(teacher.getUsername(), token);
    }

    public String verifyEmail(String token) {
        EmailToken tokenToBeVerified = emailTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (tokenToBeVerified.isExpired()) throw new IllegalStateException("Token expired");
        if (tokenToBeVerified.isVerified()) throw new IllegalStateException("Token already verified");
        if (tokenToBeVerified.getTeacher() == null) throw new IllegalStateException("Token not associated with a teacher");
        if (tokenToBeVerified.getTeacher().isEmailVerified()) throw new IllegalStateException("Email already verified");

        Teacher teacher = tokenToBeVerified.getTeacher();
        teacher.setEmailVerified(true);
        tokenToBeVerified.setVerifiedAt(LocalDateTime.now());
        tokenToBeVerified.setVerified(true);
        emailTokenRepository.save(tokenToBeVerified);
        teacherService.updateTeacherEmailStatus(teacher);

        return "Email verified";
    }
}
