package com.tracer.service;

import com.tracer.model.tokens.EmailToken;
import com.tracer.model.tokens.PasswordResetToken;
import com.tracer.model.Role;
import com.tracer.model.Teacher;
import com.tracer.model.response.LoginResponse;
import com.tracer.repository.AuthorityRepository;
import com.tracer.repository.tokens.EmailTokenRepository;
import com.tracer.repository.tokens.PasswordResetTokenRepository;
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

    /**
     * Logs user in using their email and password, returning a JWT, username, and email.
     * @param email String of characters
     * @param password String of characters
     * @return A loginResponse containing a JWT, username, and email (All string objects)
     */
    public LoginResponse loginUserByEmail(String email, String password) {
        logger.info(String.format("logging in user with email: %s", email));
        Teacher teacher = (Teacher) teacherService.loadUserByEmail(email);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(teacher.getUsername(), password)
        );
        String token = tokenService.generateJwt(authentication);
        return new LoginResponse(teacher.getUsername(), teacher.getEmail(), token);
    }

    /**
     * Calls MailSender service to send an email with a code of characters to the users email.
     * @param email an email address to sand the email.
     */
    public void sendEmailVerification(String email) {
        logger.info(String.format("beginning email verification process for email: %s", email));
        mailSenderService.sendEmailVerification(email);
    }

    /**
     * Completes the email verification process by taking the token sent to the users email,
     * and verifies weather that token is valid and was given to the right user.
     * @param token A Code given by the user.
     * @param username the user's username taken from the JWT.
     * @return A placeholder string notifying the user there email has been verified.
     */
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

    /**
     * Calls MailSender to send a token to the users email address.
     * @param email User's email address.
     */
    public void initiatePasswordReset(String email) {
        logger.info(String.format("beginning password reset process for email: %s", email));
        teacherService.loadUserByEmail(email);
        mailSenderService.sendPasswordReset(email);
    }

    /**
     * Completes the password reset by taking the token to verify the correct user and the
     * new password to be used.
     * @param token Code used to authorize the method.
     * @param password new password for the user.
     * @return A username, email, and new JWT for the user.
     */
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

    /**
     *  Calls GenerateRefreshToken to create a new JWT token for the user. if the users jwt
     *  has expired.
     * @param auth users authentication to be used to verify and create a new JWT.
     * @return new JWT.
     */
    public String generateRefreshToken(Authentication auth) {
         return tokenService.generateRefreshToken(auth);
    }

    /**
     * Makes Sure the token given is still valid and ready to be used.
     * @param token containing the code, user, and validation information.
     */
    private void validateToken(EmailToken token) {
        if (LocalDateTime.now().isAfter(token.getExpiresAt())) throw new IllegalStateException("Token expired");
        if (token.getTeacher() == null) throw new IllegalStateException("Token not associated with a teacher");
        if (token.getTeacher().isEmailVerified()) throw new IllegalStateException("Email already verified");
    }

    /**
     * Makes sure the token given is still valid and ready to be used.
     * @param token containing the code, user, and validation information
     */
    private void validatePasswordResetToken(PasswordResetToken token) {
        if (LocalDateTime.now().isAfter(token.getExpiresAt())) throw new IllegalStateException("Token expired");
        if (token.getTeacher() == null) throw new IllegalStateException("Token not associated with a teacher");
    }
}
