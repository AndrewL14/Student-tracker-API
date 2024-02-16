package com.tracer.service;

import com.tracer.model.EmailToken;
import com.tracer.model.PasswordResetToken;
import com.tracer.model.Teacher;
import com.tracer.repository.EmailTokenRepository;
import com.tracer.repository.PasswordResetTokenRepository;
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

    public void sendEmailVerification(String email) {
        String subject = "Grader: Verify Email";
        EmailToken emailToken = generateNewEmailToken(email);
        String emailContent = "<html><body>" +
                "<p>Here is your password reset token:</p>" +
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
