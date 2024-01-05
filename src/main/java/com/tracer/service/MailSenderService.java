package com.tracer.service;

import com.tracer.model.EmailToken;
import com.tracer.model.Teacher;
import com.tracer.repository.EmailTokenRepository;
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
    private TokenService tokenService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;

    public void SendPasswordReset(String email, String code) {
        String emailAddress =teacherRepository.findByEmail(email).get().getEmail();

        String subject = "Tracer: Reset Password";
        String message = "Your reset code is: " + code;
            javaMailSender.send(mimeMessage ->

        {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage , true);
            helper.setTo(emailAddress);
            helper.setSubject(subject);
            helper.setText(message);
        });
    }

    public void SendEmailVerification(String email) {
        String subject = "Verify Email";
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

    private EmailToken generateNewEmailToken(String email) {
        Teacher teacher =  teacherRepository.findByEmail(email)
                .orElseThrow(NullPointerException::new);
        String token = tokenService.generateEmailVerificationToken();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = createdAt.plusMinutes(10);

        return emailTokenRepository.save(new EmailToken(token, teacher, createdAt, expiresAt));
    }
}
