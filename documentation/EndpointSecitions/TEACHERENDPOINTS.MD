# Teacher Endpoints

```yaml
 endpoints:
   - path: "/edit/teacher"
     method: "POST"
     handler: "editTeacher"
     parameters:
       - name: "HttpServletRequest"
         type: "HttpServletRequest"
       - name: "EditTeacherRequest"
         type: "EditTeacherRequest"
       - name: "Authentication"
         type: "Authentication"
     response:
       type: "ResponseEntity<Teacher>"
     responses:
       - status: 200
         description: "Teacher edited successfully"
         body:
           type: "Teacher"
       - status: 400
         description: "Invalid input"
       - status: 401
         description: "Unauthorized"
       - status: 404
         description: "Teacher not found"

   - path: "/delete/teacher"
     method: "DELETE"
     handler: "deleteTeacher"
     parameters:
       - name: "HttpServletRequest"
         type: "HttpServletRequest"
       - name: "Authentication"
         type: "Authentication"
     response:
       type: "ResponseEntity"
     responses:
       - status: 200
         description: "Teacher deleted successfully"
       - status: 401
         description: "Unauthorized"
       - status: 404
         description: "Teacher not found"

   - path: "/all"
     method: "GET"
     handler: "getAllStudents"
     parameters:
       - name: "HttpServletRequest"
         type: "HttpServletRequest"
       - name: "Authentication"
         type: "Authentication"
     response:
       type: "ResponseEntity<TeacherStudentList>"
     responses:
       - status: 200
         description: "All students retrieved successfully"
         body:
           type: "TeacherStudentList"
       - status: 401
         description: "Unauthorized"

   - path: "/get-student{id}"
     method: "GET"
     handler: "getStudentById"
     parameters:
       - name: "id"
         type: "Long"
     response:
       type: "ResponseEntity<PublicStudentDTO>"
     responses:
       - status: 200
         description: "Student retrieved successfully"
         body:
           type: "PublicStudentDTO"
       - status: 404
         description: "Student not found"

   - path: "/unique{studentName}"
     method: "GET"
     handler: "getStudentsByName"
     parameters:
       - name: "studentName"
         type: "String"
       - name: "HttpServletRequest"
         type: "HttpServletRequest"
       - name: "Authentication"
         type: "Authentication"
     response:
       type: "ResponseEntity<PublicStudentDTO>"
     responses:
       - status: 200
         description: "Student retrieved successfully"
         body:
           type: "PublicStudentDTO"
       - status: 401
         description: "Unauthorized"
       - status: 404
         description: "Student not found"

   - path: "/add/student"
     method: "PUT"
     handler: "addStudent"
     parameters:
       - name: "AddStudentRequest"
         type: "AddStudentRequest"
       - name: "HttpServletRequest"
         type: "HttpServletRequest"
       - name: "Authentication"
         type: "Authentication"
     response:
       type: "ResponseEntity<TeacherStudentList>"
     responses:
       - status: 200
         description: "Student added successfully"
         body:
           type: "TeacherStudentList"
       - status: 400
         description: "Invalid input"
       - status: 401
         description: "Unauthorized"

   - path: "/edit/student"
     method: "POST"
     handler: "editExistingStudent"
     parameters:
       - name: "EditStudentRequest"
         type: "EditStudentRequest"
       - name: "HttpServletRequest"
         type: "HttpServletRequest"
       - name: "Authentication"
         type: "Authentication"
     response:
       type: "ResponseEntity<PublicStudentDTO>"
     responses:
       - status: 200
         description: "Student edited successfully"
         body:
           type: "PublicStudentDTO"
       - status: 400
         description: "Invalid input"
       - status: 401
         description: "Unauthorized"
       - status: 404
         description: "Student not found"

   - path: "/delete/student"
     method: "DELETE"
     handler: "deleteExistingStudent"
     parameters:
       - name: "DeleteStudentRequest"
         type: "DeleteStudentRequest"
       - name: "HttpServletRequest"
         type: "HttpServletRequest"
       - name: "Authentication"
         type: "Authentication"
     response:
       type: "ResponseEntity"
     responses:
       - status: 200
         description: "Student deleted successfully"
       - status: 400
         description: "Invalid input"
       - status: 401
         description: "Unauthorized"
       - status: 404
         description: "Student not found"

   - path: "/add/assignment"
     method: "PUT"
     handler: "addNewAssignment"
     parameters:
       - name: "AddAssignmentRequest"
         type: "AddAssignmentRequest"
       - name: "HttpServletRequest"
         type: "HttpServletRequest"
       - name: "Authentication"
         type: "Authentication"
     response:
       type: "ResponseEntity<Assignments>"
     responses:
       - status: 201
         description: "Assignment added successfully"
         body:
           type: "Assignments"
       - status: 400
         description: "Invalid input"
       - status: 401
         description: "Unauthorized"

   - path: "/get-all{id}"
     method: "GET"
     handler: "getAllAssignmentsByStudentId"
     parameters:
       - name: "id"
         type: "Long"
       - name: "HttpServletRequest"
         type: "HttpServletRequest"
       - name: "Authentication"
         type: "Authentication"
     response:
       type: "ResponseEntity<List<Assignments>>"
     responses:
       - status: 200
         description: "Assignments retrieved successfully"
         body:
           type: "List<Assignments>"
       - status: 401
         description: "Unauthorized"
       - status: 404
         description: "Assignments not found"

   - path: "/update-status/"
     method: "POST"
     handler: "updateStatus"
     parameters:
       - name: "UpdateAssignmentStatusRequest"
         type: "UpdateAssignmentStatusRequest"
       - name: "HttpServletRequest"
         type: "HttpServletRequest"
       - name: "Authentication"
         type: "Authentication"
     response:
       type: "ResponseEntity<List<Assignment>>"
     responses:
       - status: 200
         description: "Assignment status updated successfully"
         body:
           type: "List<Assignment>"
       - status: 400
         description: "Invalid input"
       - status: 401
         description: "Unauthorized"
       - status: 404
         description: "Assignments not found"

   - path: "/edit/assignment"
     method: "POST"
     handler: "editAssignment"
     parameters:
       - name: "EditAssignmentRequest"
         type: "EditAssignmentRequest"
       - name: "HttpServletRequest"
         type: "HttpServletRequest"
       - name: "Authentication"
         type: "Authentication"
     response:
       type: "ResponseEntity<List<Assignment>>"
     responses:
       - status: 200
         description: "Assignment edited successfully"
         body:
           type: "List<Assignment>"
       - status: 400
         description: "Invalid input"
       - status: 401
         description: "Unauthorized"
       - status: 404
         description: "Assignments not found"

   - path: "/delete/assignment"
     method: "DELETE"
     handler: "deleteAssignment"
     parameters:
       - name: "DeleteAssignmentRequest"
         type: "DeleteAssignmentRequest"
       - name: "HttpServletRequest"
         type: "HttpServletRequest"
       - name: "Authentication"
         type: "Authentication"
     response:
       type: "ResponseEntity<List<Assignment>>"
     responses:
       - status: 200
         description: "Assignment deleted successfully"
         body:
           type: "List<Assignment>"
       - status: 400
         description: "Invalid input"
       - status: 401
         description: "Unauthorized"
       - status: 404
         description: "Assignments not found"

```