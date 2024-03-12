package com.tracer.model.request.assignment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteAssignmentRequest {
    private Long studentId;
    private Long assignmentId;
}
