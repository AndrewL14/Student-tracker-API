package com.tracer.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Data
@Table(name="password_reset_token")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "password_reset_token_id")
    private Long id;
    private String token;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime verifiedAt;
    private LocalDateTime expiresAt;
    @ManyToOne
    private Teacher teacher;

    public PasswordResetToken(String token , String email , LocalDateTime createdAt, LocalDateTime expiresAt , Teacher teacher) {
        this.token = token;
        this.email = email;
        this.createdAt = createdAt;
        this.verifiedAt = null;
        this.expiresAt = expiresAt;
        this.teacher = teacher;
    }
}
