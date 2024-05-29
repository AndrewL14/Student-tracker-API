package com.tracer.model.request.student;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddStudentRequest {
    private String name;
    private Integer period;
    private String subject;
    private BigDecimal grade;
}
