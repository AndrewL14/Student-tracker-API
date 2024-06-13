package com.tracer.model.request.teacher;

import java.util.Set;

public record CreateTeacherRequest(
        String username,
        String password,
        String email,
        Set<String> subjects
) {
}
