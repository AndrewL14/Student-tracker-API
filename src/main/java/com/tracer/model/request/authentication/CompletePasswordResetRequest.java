package com.tracer.model.request.authentication;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompletePasswordResetRequest {
    private String password;
    private String token;
}
