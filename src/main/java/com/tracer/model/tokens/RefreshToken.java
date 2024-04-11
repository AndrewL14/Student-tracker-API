package com.tracer.model.tokens;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@NoArgsConstructor
@Data
@ToString
@Getter
@Setter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id", unique = true)
    private Long tokenId;
    private String username;
    @Column(length = 4000)
    private String token;
    private boolean valid;
    private boolean expired;

    public RefreshToken(String username , String token) {
        this.username = username;
        this.token = token;
        this.valid = true;
        this.expired = false;
    }
}
