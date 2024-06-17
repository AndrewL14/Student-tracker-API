# Request Objects documentation

```yaml
AddAssignmentRequest:
  type: object
  properties:
    studentId:
      type: integer
      description: "ID of the student."
    assignmentName:
      type: string
      description: "Name of the assignment."
    grade:
      type: number
      format: double
      description: "Grade of the assignment."
    completed:
      type: boolean
      description: "Indicates if the assignment is completed or not."
    dueDate:
      type: string
      format: date
      description: "Due date of the assignment. Format: yyyy-MM-dd."
    assignmentType:
      type: string
      description: "Type of the assignment."

DeleteAssignmentRequest:
  type: object
  properties:
    studentId:
      type: integer
      description: "ID of the student."
    assignmentId:
      type: integer
      description: "ID of the assignment to be deleted."

EditAssignmentRequest:
  type: object
  properties:
    studentId:
      type: integer
      description: "ID of the student."
    assignmentId:
      type: integer
      description: "ID of the assignment to be edited."
    assignmentNameToChange:
      type: string
      description: "New name for the assignment."
    gradeToChange:
      type: number
      format: double
      description: "New grade for the assignment."
    dueDateToChange:
      type: string
      format: date
      description: "New due date for the assignment. Format: yyyy-MM-dd."
    assignmentTypeToChange:
      type: string
      description: "New type for the assignment."

UpdateAssignmentStatusRequest:
  type: object
  properties:
    studentId:
      type: integer
      description: "ID of the student."
    assignmentId:
      type: integer
      description: "ID of the assignment to update status."

```