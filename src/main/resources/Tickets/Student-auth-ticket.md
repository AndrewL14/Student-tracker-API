# Student authentication
___

### Requirements / Plan

- Student must be able to login using their given email and password.
- Students should not be able to register their own accounts. as The teacher or admin must be the ones
  To register the students.
- Student must be able to be linked to multiple teacher objects. in a one-to-many fashion.
- API must be able to store and sort student assignments based on the following instructions.
  - Sort by period.
  - Store the average grade for that subject.
  - Store a list of assignments for that subject.
  - Assignments should be stored as a Treemap with the key being the A Integer representing the period number,
    and the value being an AssignmentList object containing the subject, average grade, and list of assignments.
    - Note the subjects should be stored as a string with all characters upper-cased.
- Teacher must only see assignments corresponding to that subject.
- API must be able to store and sort students based on the following instructions.
  - Sort by period.
  - Store students as a TreeMap of students' key being the period and AssignmentList object as the value.
    - Note: all periods should be stored as an integer
- Update any unit test and documentation needed.
### Future HTTP endpoints

- /auth/student/login
- /auth/teacher/login/basic
- /auth/teacher/login/email
- /auth/teacher/register
- /student/grades
- /student/get-assignments


### User stories

- as a student I should be able to view my average grade for each subject once logged in.
- as a student I should be able to view a list of my assignments for each subject and see the grade or missing.
- as a student I should be able to have my assignments organized by subject.
- as a teacher I should be able to see my students organized by period number.
- as a teacher I should be able to view a List of students based on their period number.
- as a teacher I should only see students grades that pertain to my class.



### Json example responses

#### Student format
```yaml
students:
  period1:
    students:
  period2:
    students:
```

```json
{
  "students": {
    "1": [
      {"name": "John", "grade": 10},
      {"name": "Alice", "grade": 11}
    ],
    "2": [
      {"name": "Bob", "grade": 10},
      {"name": "Sarah", "grade": 11}
    ]
  }
}
```

#### Assignment format
```yaml
AssignmentList:
  subject:
  averageGrade:
  assignments:
```
```yaml
assignments:
  period1:
    AssignmentList:
  period2:
    AssignmentList:
```
```json
{
  "assignments": {
    "1": {
      "subject": "Math",
      "averageGrade": 85.5,
      "assignments": [
        {
          "id": 1,
          "name": "Algebra Homework",
          "grade": 90.0,
          "completed": false,
          "overdue": false,
          "dueDate": "2024-05-10",
          "assignmentType": "Homework"
        },
        {
          "id": 2,
          "name": "Geometry Quiz",
          "grade": 81.5,
          "completed": false,
          "overdue": false,
          "dueDate": "2024-05-15",
          "assignmentType": "Quiz"
        }
      ]
    },
    "2": {
      "subject": "Science",
      "averageGrade": 78.2,
      "assignments": [
        {
          "id": 3,
          "name": "Biology Lab Report",
          "grade": 85.0,
          "completed": false,
          "overdue": false,
          "dueDate": "2024-05-12",
          "assignmentType": "Lab Report"
        },
        {
          "id": 4,
          "name": "Physics Project",
          "grade": 71.4,
          "completed": false,
          "overdue": false,
          "dueDate": "2024-05-20",
          "assignmentType": "Project"
        }
      ]
    }
  }
}
```