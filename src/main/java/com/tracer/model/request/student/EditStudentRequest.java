package com.tracer.model.request.student;

import lombok.*;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EditStudentRequest {
    private Long studentId;
    private Optional<Integer> periodToChange = Optional.empty();
    private String nameToChange = "";
}
