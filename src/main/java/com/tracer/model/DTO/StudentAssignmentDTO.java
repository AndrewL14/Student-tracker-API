package com.tracer.model.DTO;

public record StudentAssignmentDTO(
       String name,
       Double grade,
       boolean completed,
       boolean overdue,
       String assignmentType
) {
}
