<div align="center">
<pre>
  ███████╗   █████╗  ██████╗  ███████╗ ██████╗    ██████╗ 
  ██╔════╝  ██╔══██╗ ██╔══██╗ ██╔════╝ ██╔══██╗  ██╔════╝ 
 ██║  ███╗ ███████║ ██║  ██║ ███████╗ ██████╔╝ ╚█████╗  
 ██║   ██║ ██╔══██║ ██║  ██║ ██╔════╝ ██╔══██╗  ╚═══██╗ 
╚██████╔╝ ██║  ██║ ██████╔╝ ███████╗ ██║  ██║ ██████╔╝
 ╚═════╝  ╚═╝  ╚═╝ ╚═════╝  ╚══════╝ ╚═╝  ╚═╝ ╚═════╝
---------------------------------------------------
Grading API For Teachers
</pre>

</div>

## Description

This project is 1/2 of a student tracker Web Application, responsible for tracking, and maintaining 
Student grades for teachers. This part is the backend Service, Using Spring boot, spring security, Auth02,
JWT, RSA, H2, AWS RDS, And PostgreSQL.

## Installation

### Pre-requisites 
- Java 17 or greater
- IntelliJ or VS Code (optimal)
- Postman for testing 


To Install the application fork the repo and clone it to your local machine.

### Disclaimer
This application is a 2 part repo, if you wish to run in full-stack, visit
[Student-tracker-UI](https://github.com/AndrewL14/Student-tracker-UI)  and clone the repo, making sure to run both applications

Currently, the UI has not been styled but the corresponding login, register, and dashboard pages
have all been implemented. 

Note: If running in dev mode the data will not be stored across runtimes as the project uses an H2 database
Configuration for testing and dev work. During a future update this project will be deployed using AWS and Render.

If you wish to deploy this project for your self, create an application-prod.properties file and 
Configure accordingly

### Documentation
[![Simplified Class Diagram](documentation/ClassRelationsSimplified.png)](documentation/ClassRelationsSimplified.puml)
or visit the [compressive Diagram](documentation/ClassRelations.puml)

## Sample request
The way the project is set up you will need to log in before calling any methods, as you will need a 
VALID JWT to call the other methods. And make sure the program is running. 

Replace YOUR_ACCESS_TOKEN with the JWT you get when logging in.
### POST Register
```sh 
curl -X POST -H "Content-Type: application/json" -d '{"username": "your_username", "email": "<your_email>"  "password": "your_password"}' http://localhost:8000/auth/register
```
### POST Log in
``` sh
curl -X POST -H "Content-Type: application/json" -d '{"username": "james", "password": "password"}' http://localhost:8000/auth/login/basic
```

All other request are in postman. In the Auth tab select `Bearer Token` and put your JWT in.
request files are formatted in `.yaml` to show what is needed, for request requiring a body, a
corresponding `.json` format has been provided for quick copy and paste.

Here is all the necessary information needed to carry out request in postman:
### GET all students
```yaml
request:
  method: GET
  url: http://localhost:8000/teacher/all
  headers:
    Authorization: "Bearer YOUR_ACCESS_TOKEN"
```
### GET student by name
```yaml
request_get_unique_student:
  method: GET
  url: http://localhost:8000/teacher/unique?studentName=John
  headers:
    Authorization: "Bearer YOUR_ACCESS_TOKEN"
```
### PUT add student
```yaml
request_put_add_student:
  method: PUT
  url: http://localhost:8000/teacher/add
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
Request uses name and period to find the correct student.
```yaml
request_post_edit_student:
  method: POST
  url: http://localhost:8000/teacher/edit
  headers:
    Authorization: "Bearer YOUR_ACCESS_TOKEN"
  body:
    studentId: 1
    periodToChange: 2
    nameToChange: colin
    gradeToChange: 75
```
```json
{
   "studentId": 1,
   "periodToChange": 2,
   "nameToChange": "colin",
   "gradeToChange": 75.6
}
```
### DELETE student
```yaml
request_delete_delete_student:
  method: DELETE
  url: http://localhost:8000/teacher/delete
  headers:
    Authorization: "Bearer YOUR_ACCESS_TOKEN"
  body:
    studentName: "jhon"
```
```json
{
  "studentName": "jhon"
}
```
## Development setup
1. Fork Repo
2. Clone Repo
3. Uncomment beaned command line runner in StudentTrackerApiApplication.class If needed
4. Set Application.properties file to run application-dev
`spring.profiles.active=dev`
5. If needed configure application-dev file to fit your needs
   These are the default parameters for the H2 database
```
spring.datasource.url=jdbc:h2:file:/data/demo;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=create-drop
```
6. Run Application

## Meta

A. Lam – andrew.lam965@Gmail.com
Github profile
[https://github.com/AndrewL14/](https://github.com/AndrewL14/)

## Contributing

1. Fork it (<https://github.com/AndrewL14/Student-tracker-API/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request
