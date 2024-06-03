package com.tracer.model.DTO;

import com.tracer.model.assignments.Assignments;

import java.util.List;
import java.util.Map;

public record PublicStudentDTO(
        Long id,
        String name,
        int period,
        Assignments assignments
) {
}
