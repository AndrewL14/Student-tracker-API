package com.tracer.service;

import com.tracer.model.tokens.EmailToken;
import com.tracer.model.tokens.PasswordResetToken;
import com.tracer.model.Teacher;
import com.tracer.repository.tokens.EmailTokenRepository;
import com.tracer.repository.tokens.PasswordResetTokenRepository;
import com.tracer.repository.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MailSenderService {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private EmailTokenRepository emailTokenRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private JavaMailSender javaMailSender;
    private final Logger logger = LoggerFactory.getLogger(MailSenderService.class);
    @Value("${spring.mail.username}")
    private String senderEmail;

    /**
     * Sends a custom email to a users email address with the necessary token for a password
     * reset.
     * @param email A users email address.
     */
    public void sendPasswordReset(String email) {
        PasswordResetToken passwordResetToken = generateNewPasswordResetToken(email);
        String subject = "Grader: Reset Password";
        String emailContent = "<html><body>" +
                "<p>Here is your password reset token:</p>" +
                "<p>" + passwordResetToken.getToken() + "</p>" +
                "</body></html>";
        logger.info("sending password reset email");
        javaMailSender.send(mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(emailContent, true);
            helper.setFrom(senderEmail);
        });
        logger.info("successfully sent password reset email");
    }

    /**
     * Sends a custom email to a users email address with the necessary token to verify
     * the given email address.
     * @param email A users email address.
     */
    public void sendEmailVerification(String email) {
        String subject = "Grader: Verify Email";
        EmailToken emailToken = generateNewEmailToken(email);
        String emailContent = "<html><body>" +
                "<p>Here is your email verification token:</p>" +
                "<p>" + emailToken.getToken() + "</p>" +
                "</body></html>";

        logger.info("sending email verification email");
        javaMailSender.send(mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(emailContent, true);
            helper.setFrom(senderEmail);
        });
        logger.info("successfully sent email verification email");
    }

    /**
     * Generates and saves a new password reset token to verify the users identity and,
     * validate the tokens' authenticity when sent back.
     * @param email A email Address
     * @return A new PasswordResetToken object containing a token, email address, user, and validation times.
     */
    public PasswordResetToken generateNewPasswordResetToken(String email) {
        logger.info(String.format("Creating new password reset token for %s", email));
        Teacher teacher = teacherRepository.findByEmail(email)
                .orElseThrow(NullPointerException::new);
        String token = tokenService.generateEmailVerificationToken();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = createdAt.plusMinutes(10);

        return passwordResetTokenRepository.save(
                new PasswordResetToken(token, email, createdAt, expiresAt, teacher)
        );
    }

    /**
     * Generates and saves a new email verification token to verify the users identity and,
     * validate the tokens' authenticity when sent back.
     * @param email A email Address
     * @return A new EmailToken object containing a token, user, and validation times.
     */
    public EmailToken generateNewEmailToken(String email) {
        logger.info(String.format("Creating new email token for %s", email));
        Teacher teacher =  teacherRepository.findByEmail(email)
                .orElseThrow(NullPointerException::new);
        String token = tokenService.generateEmailVerificationToken();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = createdAt.plusMinutes(10);

        return emailTokenRepository.save(new EmailToken(token, teacher, createdAt, expiresAt));
    }
}
