package com.tracer.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BasicLoginRequest {
    private String username;
    private String password;
}
