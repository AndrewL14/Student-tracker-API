package com.tracer.model.request.teacher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EditTeacherRequest {
    private String username = "";
    private String email = "";
    private String password = "";
    private Set<String> subjects = new HashSet<>();
}
