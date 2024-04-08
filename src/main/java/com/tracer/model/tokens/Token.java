package com.tracer.model.tokens;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id", unique = true)
    private Long tokenId;
    private String username;
    @Column(length = 4000)
    private String jwt;
    private boolean expired;
    private LocalDateTime expirationTime;

    public Token(String username , String jwt , boolean expired , LocalDateTime expirationTime) {
        this.username = username;
        this.jwt = jwt;
        this.expired = expired;
        this.expirationTime = expirationTime;
    }
}
