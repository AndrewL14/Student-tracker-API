package com.tracer.service;

import com.tracer.model.EmailToken;
import com.tracer.model.PasswordResetToken;
import com.tracer.model.Role;
import com.tracer.model.Teacher;
import com.tracer.model.response.LoginResponse;
import com.tracer.repository.AuthorityRepository;
import com.tracer.repository.EmailTokenRepository;
import com.tracer.repository.PasswordResetTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
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
    private PasswordResetTokenRepository resetTokenRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private TokenService tokenService;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    /**
     * Creates and saves a new teacher object using the provided username and password.
     * @param username A unique combination of characters to be used.
     * @param password A string of characters to be used.
     * @return A loginResponse containing a new List of students, and a valid JWT.
     */
    public LoginResponse registerUser(String username, String email, String password){
        logger.info(String.format("Registering new user with username: %s", username));
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("TEACHER").get();

        Set<Role> authorities = new HashSet<>();

        authorities.add(userRole);
        Teacher teacher = new Teacher(username, email, encodedPassword, new HashSet<>(),
                authorities);
        teacherService.saveNewTeacher(teacher);
        mailSenderService.sendEmailVerification(email);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        String token = tokenService.generateJwt(authentication);
        return new LoginResponse(teacher.getUsername(), teacher.getEmail(), token);
    }

    /**
     * Checks if a teacher object containing matching username and password, then
     * generates a new JWT, finally returns a loginResponse.
     * @param username A unique combination of characters to be used.
     * @param password A string of characters to be used.
     * @return A loginResponse containing a new List of students, and a valid JWT.
     */
    public LoginResponse loginUserByUsername(String username, String password){
        logger.info(String.format("logging in user with username: %s", username));
        Authentication authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(username, password)
        );
        String token = tokenService.generateJwt(authentication);
        Teacher teacher = (Teacher) teacherService.loadUserByUsername(username);
        return new LoginResponse(teacher.getUsername(), teacher.getEmail(), token);
    }

    public LoginResponse loginUserByEmail(String email, String password) {
        logger.info(String.format("logging in user with email: %s", email));
        Teacher teacher = (Teacher) teacherService.loadUserByEmail(email);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(teacher.getUsername(), password)
        );
        String token = tokenService.generateJwt(authentication);
        return new LoginResponse(teacher.getUsername(), teacher.getEmail(), token);
    }

    public void sendEmailVerification(String email) {
        logger.info(String.format("beginning email verification process for email: %s", email));
        mailSenderService.sendEmailVerification(email);
    }

    public String verifyEmail(String token, String username) {
        logger.info(String.format("beginning user verification process for user: %s", username));
        EmailToken tokenToBeVerified = emailTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        validateToken(tokenToBeVerified);
        Teacher teacher = tokenToBeVerified.getTeacher();
        if (!Objects.equals(teacher.getUsername() , username)) throw new RuntimeException("Token does not match with given username");
        teacher.setEmailVerified(true);
        tokenToBeVerified.setVerifiedAt(LocalDateTime.now());
        tokenToBeVerified.setVerified(true);
        emailTokenRepository.save(tokenToBeVerified);
        teacherService.saveTeacher(teacher);
        emailTokenRepository.deleteAllByTeacher(teacher);

        return "Email verified";
    }

    public void initiatePasswordReset(String email) {
        logger.info(String.format("beginning password reset process for email: %s", email));
        teacherService.loadUserByEmail(email);
        mailSenderService.sendPasswordReset(email);
    }

    public LoginResponse completePasswordReset(String token, String password) {
        logger.info("completing password reset process");
        PasswordResetToken resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(NullPointerException::new);
        String encodedPassword = passwordEncoder.encode(password);
        validatePasswordResetToken(resetToken);

        Teacher teacherToUpdate = resetToken.getTeacher();
        teacherToUpdate.setPassword(encodedPassword);
        resetToken.setVerifiedAt(LocalDateTime.now());
        teacherService.saveTeacher(teacherToUpdate);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(teacherToUpdate.getUsername(), password)
        );
        String jwt = tokenService.generateJwt(authentication);
        resetTokenRepository.deleteAllByTeacher(teacherToUpdate);
        return new LoginResponse(teacherToUpdate.getUsername(), teacherToUpdate.getEmail(), jwt);
    }

    private void validateToken(EmailToken token) {
        if (LocalDateTime.now().isAfter(token.getExpiresAt())) throw new IllegalStateException("Token expired");
        if (token.getTeacher() == null) throw new IllegalStateException("Token not associated with a teacher");
        if (token.getTeacher().isEmailVerified()) throw new IllegalStateException("Email already verified");
    }

    private void validatePasswordResetToken(PasswordResetToken token) {
        if (LocalDateTime.now().isAfter(token.getExpiresAt())) throw new IllegalStateException("Token expired");
        if (token.getTeacher() == null) throw new IllegalStateException("Token not associated with a teacher");
    }
}
