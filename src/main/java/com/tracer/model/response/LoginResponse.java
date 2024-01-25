package com.tracer.model.response;

import com.tracer.model.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String teacherUsername;
    private String teacherEmail;
    private String jwt;
}
