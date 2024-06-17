# Endpoint Sample request

- Visit [Authentication doc](EndpointSecitions/AUTHENTICATIONENDPOINTS.MD) for more information on the authentication endpoints.
- Visit [Student doc](EndpointSecitions/STUDENTENDPOINTS.MD) for more information on Student endpoints.
- Visit [Teacher doc](EndpointSecitions/TEACHERENDPOINTS.MD) for more information on Teacher endpoints.
- Visit [Request objects doc](REQUETSOBJECTS.MD) for more information on request bodies.


The way the project is set up you will need to log in before calling any methods, as you will need a
VALID JWT to call the other methods. And make sure the program is running.
Replace YOUR_ACCESS_TOKEN with the JWT you get when logging in.
When using Postman put the JWT in authorization -> type -> bearer token. For any request that need it.

Application will be running on localhost:5000 on dev mode.

Sample Request Requiring a JWT token.

![example 1](example-1.png)
![example 2](example-2.png)


### (When in dev mode) This program will run on `http://localhost:5000/`
## Authentication request
___

### POST Register
```sh 
curl -X POST -H "Content-Type: application/json" -d '{"username": "your_username", "email": "<your_email>"  "password": "your_password"}' http://localhost:5000/auth/register
```
```json
{
  "username": "example",
  "email": "email",
  "password": "password"
}
```
### POST Log in
``` sh
curl -X POST -H "Content-Type: application/json" -d '{"username": "james", "password": "password"}' http://localhost:5000/auth/login/basic
```
```json
{
  "username": "james",
  "password": "password"
}
```
```yaml
  - path: "/auth/refresh-token{token}"
    method: "POST"
    description: "uses Refresh token to generate a new JWT."
    request_params:
      - name: "token"
        type: "string"
        description: "Refresh token."
    headers:
      - name: "Authorization"
        type: "string"
        description: "JWT token for authentication."
```
All other requests are in Postman. In the Auth tab select `Bearer Token` and put your JWT in.
request files are formatted in `.yaml` to show what is needed, for requests requiring a body, a
corresponding `.json` format has been provided for quick copy and paste.
```yaml
endpoints:
  - path: "/auth/generate/refresh-token"
    method: "POST"
    description: "Generate a refresh token."
    headers:
      - name: "Authorization"
        type: "string"
        description: "JWT token for authentication."
```

## Student request
___
### GET all students
All request should be sent with a body even if the body is empty.
```yaml
request:
  method: GET
  url: http://localhost:5000/teacher/all
  headers:
    Authorization: "Bearer YOUR_ACCESS_TOKEN"
  body:
```
### GET student by name
```yaml
request_get_unique_student:
  method: GET
  url: http://localhost:5000/teacher/unique?studentName=John%20Smith
  headers:
    Authorization: "Bearer YOUR_ACCESS_TOKEN"
```
### PUT add student
```yaml
request_put_add_student:
  method: PUT
  url: http://localhost:5000/teacher/add/student
  headers:
    Authorization: "Bearer YOUR_ACCESS_TOKEN"
  body:
    name: "John Doe"
    period: 2
    grade: 75
```
```json
{
  "name": "John Doe",
  "period": 2,
  "grade": 75
}
```
### POST edit student
```yaml
request_post_edit_student:
  method: POST
  url: http://localhost:5000/teacher/edit/student
  headers:
    Authorization: "Bearer YOUR_ACCESS_TOKEN"
  body:
    studentId: 1
    periodToChange: 2
    nameToChange: colin
```
```json
{
   "studentId": 1,
   "periodToChange": 2,
   "nameToChange": "colin"
}
```
### DELETE student
```yaml
request_delete_delete_student:
  method: DELETE
  url: http://localhost:5000/teacher/delete/student
  headers:
    Authorization: "Bearer YOUR_ACCESS_TOKEN"
  body:
    studentName: "Jhon Smith"
```
```json
{
  "studentName": "Jhon Smith"
}
```
## Assignment request
___
## Endpoints

```yaml
endpoints:
  - path: "/teacher/get-student{id}"
    method: "GET"
    description: "Get student by ID."
    request_params:
      - name: "id"
        type: "Long"
        description: "ID of the student."
    headers:
      - name: "Authorization"
        type: "string"
        description: "JWT token for authentication."

  - path: "/teacher/get-all{id}"
    method: "GET"
    description: "Get all assignments for a student."
    request_params:
      - name: "id"
        type: "Long"
        description: "ID of the student."
    headers:
      - name: "Authorization"
        type: "string"
        description: "JWT token for authentication."

  - path: "/teacher/add/assignment"
    method: "PUT"
    description: "Add a new assignment for a student."
    request_body:
      type: "AddAssignmentRequest"
      description: "Request body containing details of the assignment to be added."
    headers:
      - name: "Authorization"
        type: "string"
        description: "JWT token for authentication."

  - path: "/teacher/update-status/"
    method: "POST"
    description: "Update assignment status for a student."
    request_body:
      type: "UpdateAssignmentStatusRequest"
      description: "Request body containing student ID and assignment ID to update status."
    headers:
      - name: "Authorization"
        type: "string"
        description: "JWT token for authentication."

  - path: "/teacher/edit/assignment"
    method: "POST"
    description: "Edit an existing assignment for a student."
    request_body:
      type: "EditAssignmentRequest"
      description: "Request body containing details of the assignment to be edited."
    headers:
      - name: "Authorization"
        type: "string"
        description: "JWT token for authentication."

  - path: "/teacher/delete/assignment"
    method: "DELETE"
    description: "Delete an assignment for a student."
    request_body:
      type: "DeleteAssignmentRequest"
      description: "Request body containing details of the assignment to be deleted."
    headers:
      - name: "Authorization"
        type: "string"
        description: "JWT token for authentication."

```

