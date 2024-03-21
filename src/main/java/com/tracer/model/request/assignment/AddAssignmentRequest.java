package com.tracer.model.request.assignment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddAssignmentRequest {
    private Long studentId;
    private String assignmentName;
    private double grade;
    private boolean completed;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private String assignmentType;
}
