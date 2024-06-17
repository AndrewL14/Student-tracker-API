# Authentication Endpoints

```yaml
  endpoints:
    - path: "/register"
      method: "POST"
      handler: "registerUser"
      parameters:
        - name: "RegistrationRequest"
          type: "RegistrationRequest"
      response:
        type: "ResponseEntity<TeacherLoginResponse>"
      responses:
        - status: 200
          description: "User registered successfully"
          body:
            type: "TeacherLoginResponse"
        - status: 400
          description: "Invalid input"

    - path: "/login/basic"
      method: "POST"
      handler: "loginUserByUsername"
      parameters:
        - name: "BasicLoginRequest"
          type: "BasicLoginRequest"
      response:
        type: "ResponseEntity<TeacherLoginResponse>"
      responses:
        - status: 200
          description: "User logged in successfully"
          body:
            type: "TeacherLoginResponse"
        - status: 401
          description: "Unauthorized"

    - path: "/login/email"
      method: "POST"
      handler: "loginUserByEmail"
      parameters:
        - name: "EmailLoginRequest"
          type: "EmailLoginRequest"
      response:
        type: "ResponseEntity<TeacherLoginResponse>"
      responses:
        - status: 200
          description: "User logged in successfully"
          body:
            type: "TeacherLoginResponse"
        - status: 401
          description: "Unauthorized"

    - path: "/student/login"
      method: "POST"
      handler: "studentLogin"
      parameters:
        - name: "EmailLoginRequest"
          type: "EmailLoginRequest"
      response:
        type: "ResponseEntity<StudentLoginResponse>"
      responses:
        - status: 200
          description: "Student logged in successfully"
          body:
            type: "StudentLoginResponse"
        - status: 401
          description: "Unauthorized"

    - path: "/verify/email"
      method: "GET"
      handler: "verifyEmail"
      parameters:
        - name: "token"
          type: "String"
        - name: "Authentication"
          type: "Authentication"
      response:
        type: "ResponseEntity<String>"
      responses:
        - status: 200
          description: "Email verified successfully"
          body:
            type: "String"
        - status: 401
          description: "Unauthorized"
        - status: 404
          description: "Verification token not found"

    - path: "/verify/send{email}"
      method: "GET"
      handler: "sendEmailVerification"
      parameters:
        - name: "email"
          type: "String"
      response:
        type: "ResponseEntity<String>"
      responses:
        - status: 200
          description: "Verification email sent successfully"
          body:
            type: "String"
        - status: 400
          description: "Invalid email address"

    - path: "/initiate-reset{email}"
      method: "GET"
      handler: "initiatePasswordReset"
      parameters:
        - name: "email"
          type: "String"
      response:
        type: "ResponseEntity"
      responses:
        - status: 200
          description: "Password reset initiated successfully"
        - status: 400
          description: "Invalid email address"

    - path: "/reset-password"
      method: "POST"
      handler: "completePasswordReset"
      parameters:
        - name: "CompletePasswordResetRequest"
          type: "CompletePasswordResetRequest"
      response:
        type: "ResponseEntity<TeacherLoginResponse>"
      responses:
        - status: 200
          description: "Password reset completed successfully"
          body:
            type: "TeacherLoginResponse"
        - status: 400
          description: "Invalid input"
        - status: 404
          description: "Reset token not found"

    - path: "/generate/refresh-token"
      method: "POST"
      handler: "generateRefreshToken"
      parameters:
        - name: "Authentication"
          type: "Authentication"
      response:
        type: "ResponseEntity<String>"
      responses:
        - status: 200
          description: "Refresh token generated successfully"
          body:
            type: "String"
        - status: 401
          description: "Unauthorized"

    - path: "/refresh-token{token}"
      method: "POST"
      handler: "refreshToken"
      parameters:
        - name: "token"
          type: "String"
      response:
        type: "ResponseEntity<String>"
      responses:
        - status: 200
          description: "Token refreshed successfully"
          body:
            type: "String"
        - status: 400
          description: "Invalid token"
        - status: 401
          description: "Unauthorized"
```