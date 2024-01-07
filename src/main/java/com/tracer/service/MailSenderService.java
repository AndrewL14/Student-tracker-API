package com.tracer.service;

import com.tracer.model.EmailToken;
import com.tracer.model.PasswordResetToken;
import com.tracer.model.Teacher;
import com.tracer.repository.EmailTokenRepository;
import com.tracer.repository.PasswordResetTokenRepository;
import com.tracer.repository.TeacherRepository;
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
    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendPasswordReset(String email) {
        PasswordResetToken passwordResetToken = generateNewPasswordResetToken(email);
        String subject = "Grader: Reset Password";
        String resetLink = "http://localhost:8000/auth/reset-password";
        String emailContent = "<html><body>" +
                "<p>Click the button below to reset your password:</p>" +
                "<form method=\"post\" action=\"" + resetLink + "\">" +
                "<input type=\"hidden\" name=\"token\" value=\"" + passwordResetToken.getToken() + "\">" +
                "<label for=\"password\">New Password:</label>" +
                "<input type=\"password\" id=\"password\" name=\"password\" required>" +
                "<button type=\"submit\">Reset Password</button>" +
                "</form>" +
                "</body></html>";

        javaMailSender.send(mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(emailContent, true);
            helper.setFrom(senderEmail);
        });
    }

    public void SendEmailVerification(String email) {
        String subject = "Grader: Verify Email";
        String verificationLink = "http://localhost:8000/auth/verify/email?token=";
        EmailToken emailToken = generateNewEmailToken(email);
        String emailContent = "<html><body>" +
                "<p>Click the button below to verify your email address:</p>" +
                "<form action=\"" + verificationLink + "\">" +
                "<input type=\"hidden\" name=\"token\" value=\"" + emailToken.getToken() + "\">" +
                "<button type=\"submit\">Verify Email</button>" +
                "</form>" +
                "</body></html>";


        javaMailSender.send(mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(emailContent, true);
            helper.setFrom(senderEmail);
        });
    }

    public PasswordResetToken generateNewPasswordResetToken(String email) {
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
        Teacher teacher =  teacherRepository.findByEmail(email)
                .orElseThrow(NullPointerException::new);
        String token = tokenService.generateEmailVerificationToken();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = createdAt.plusMinutes(10);

        return emailTokenRepository.save(new EmailToken(token, teacher, createdAt, expiresAt));
    }
}
