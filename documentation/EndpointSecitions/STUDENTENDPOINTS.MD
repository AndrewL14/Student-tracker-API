# Student Endpoints

```yaml
  endpoints:
    - path: "/get/student/assignments/by/username"
      method: "GET"
      handler: "getStudentAssignmentsByUsername"
      parameters:
        - name: "HttpServletRequest"
          type: "HttpServletRequest"
        - name: "Authentication"
          type: "Authentication"
      response:
        type: "ResponseEntity<PrivateStudentDTO>"
      responses:
        - status: 200
          description: "Student assignments retrieved successfully"
          body: 
            type: "PrivateStudentDTO"
        - status: 401
          description: "Unauthorized"
        - status: 404
          description: "Student not found"
```