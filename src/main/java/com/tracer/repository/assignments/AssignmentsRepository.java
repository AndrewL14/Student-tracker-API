package com.tracer.repository.assignments;

import com.tracer.model.assignments.Assignments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentsRepository extends JpaRepository<Assignments, Long> {
}
