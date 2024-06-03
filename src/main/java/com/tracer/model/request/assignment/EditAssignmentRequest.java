package com.tracer.model.request.assignment;

import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EditAssignmentRequest {
    private Long studentId;
    private Long assignmentId;
    private int period;
    private String assignmentNameToChange = "";
    private Optional<Double> gradeToChange = Optional.empty();
    private Optional<LocalDate> dueDateToChange = Optional.empty();
    private String assignmentTypeToChange = "";
}
