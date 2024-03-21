package com.tracer.model.request.assignment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateAssignmentStatusRequest {
    private Long studentId;
    private Long assignmentId;
}
