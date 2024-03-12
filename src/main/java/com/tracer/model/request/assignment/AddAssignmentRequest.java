package com.tracer.model.request;

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
    private LocalDate dueDate;
    private String assignmentType;
}
