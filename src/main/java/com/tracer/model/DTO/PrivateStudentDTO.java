package com.tracer.model.DTO;

import com.tracer.model.assignments.Assignments;

import java.util.List;
import java.util.Map;

public record PrivateStudentDTO(
        String name,
        Map<Integer, Assignments> assignments
) {
}
