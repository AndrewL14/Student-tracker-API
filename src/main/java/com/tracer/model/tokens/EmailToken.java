package com.tracer.model.tokens;

import com.tracer.model.users.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "email_tokens")
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EmailToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "email_token_id")
    private Long id;
    private String token;
    @ManyToOne(fetch = FetchType.EAGER)
    private Teacher teacher;
    private LocalDateTime createdAt;
    private LocalDateTime verifiedAt;
    private LocalDateTime expiresAt;
    private boolean isExpired;
    private boolean isVerified;

    public EmailToken(String token, Teacher teacher, LocalDateTime createdAt, LocalDateTime expiresAt) {
       this.token = token;
       this.teacher = teacher;
       this.createdAt = createdAt;
       this.verifiedAt = null;
       this.expiresAt = expiresAt;
       this.isExpired = false;
       this.isVerified = false;
    }
}
