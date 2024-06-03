package com.tracer.model.request.student;

public record DeleteStudentRequest(
        int period,
        Long studentId
) {
}
