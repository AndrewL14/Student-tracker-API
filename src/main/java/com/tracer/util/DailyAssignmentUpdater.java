package com.tracer.util;

import com.tracer.model.assignments.Assignment;
import com.tracer.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DailyAssignmentUpdater {
    @Autowired
    private AssignmentRepository assignmentRepository;


    @Scheduled(cron = "0 0 0 * * *")
    public void CheckIfAssignmentIsLate() {
        List<Assignment> assignments = assignmentRepository.findAll();
        ExecutorService executorService = Executors.newFixedThreadPool(assignments.size() / 4);

        for (Assignment assignment : assignments) {
            executorService.submit(() -> {
                if (isLate(assignment)) {
                    assignment.setOverdue(true);
                    assignmentRepository.save(assignment);
                }
            });
        }
    }

    private boolean isLate(Assignment assignment) {
        return assignment.getDueDate().isBefore(LocalDate.now()) && !assignment.isCompleted();
    }
}
