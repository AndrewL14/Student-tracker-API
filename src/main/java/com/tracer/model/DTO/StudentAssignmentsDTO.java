package com.tracer.model.DTO;

import java.util.List;

public record StudentAssignmentsDTO(
        String subject,
        int period,
        double averageGrade,
        List<StudentAssignmentDTO> assignments
) {
}
