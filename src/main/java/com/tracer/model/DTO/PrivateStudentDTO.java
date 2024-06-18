package com.tracer.model.DTO;


import java.util.Map;

public record PrivateStudentDTO(
        String name,
        Map<Integer, StudentAssignmentsDTO> assignments
) {
}
