package com.tracer.model.tokens;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "refresh_token_id", unique = true)
    private Long refreshTokenId;
    private String username;
    private boolean expired;
    private LocalDateTime expirationTime;

    public RefreshToken(String username , boolean expired , LocalDateTime expirationTime) {
        this.username = username;
        this.expired = expired;
        this.expirationTime = expirationTime;
    }
}
