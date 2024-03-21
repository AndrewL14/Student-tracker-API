package com.tracer.model.request.authentication;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegistrationRequest {
    private String username;
    private String email;
    private String password;
}
