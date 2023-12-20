package com.tracer.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EditStudentRequest {
    private Long studentId;
    private Optional<Integer> periodToChange = Optional.empty();
    private String nameToChange = "";
    private Optional<Double> gradeToChange = Optional.empty();
}
