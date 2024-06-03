package com.tracer.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherLoginResponse {
    private String teacherUsername;
    private String teacherEmail;
    private String jwt;
    private String refreshToken;
}
