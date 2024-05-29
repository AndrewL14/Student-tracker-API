package com.tracer.model.DTO;

import com.tracer.model.assignments.Assignment;

import java.math.BigDecimal;
import java.util.List;

public record PublicStudent(
        String name,
        BigDecimal grade,
        List<Assignment> assignments
) {
}
