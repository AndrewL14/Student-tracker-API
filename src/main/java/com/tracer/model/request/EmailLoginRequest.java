package com.tracer.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailLoginRequest {
    private String email;
    private String password;
}
