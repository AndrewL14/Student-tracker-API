# Service Organization

## Description
Currently, `teacherController` and `StudentController` and service classes methods are placed wrongly.
Meaning Student endpoints have access to modify assignments. the `TeacherSerivce` is
focused on student modification. `StudentService` class is focused on assignment modification.
This could lead to data breaches and students being able to modify their grades in the future.

## User Stories
1. As a teacher I should be able to modify my account, and have full access to all my students, and their assignments.
2. As a student I should only be able to see my assignments and not be able to modify them.

## Requirements
- `TeacherSerivce` should only contain methods that modify the `Teacher` entity or methods
only a teacher or admin can use.
- `StudentService` should only contain methods allowing students to view assignments.
- A new service class should be created called `AssignmentsSerivce`.
  - This class should be accessible for teachers and admins.
  - This class should contain methods that modify `Assignments` and `Assignment` objects.
- `TeacherController` should be updated for changes made.
  - should have access to `TeacherService`, `StudentService`, and `AssignmentsService`.
- `StudentController` should be updated for changes made.
  - should have access to `StudentService`.
- Re-organize unit test for changes made.
- Updated Documentation to include changes made.

This ticket is simple Code refactoring.