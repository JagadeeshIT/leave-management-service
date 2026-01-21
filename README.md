# Leave Management System – Leave Service

This repository contains the **Leave Management Service**, developed as part of an Employee Leave Management System.

##  Overview
The Leave Service handles employee leave workflows, including:
- Applying for leave
- Viewing leave history
- Cancelling leave requests
- Manager approval or rejection of leave requests
- Admin-level leave statistics

This service is implemented following real-time backend development practices.

---

## 🛠 Tech Stack
- Java 8
- Spring Boot
- Spring Data JPA
- MySQL
- Maven
- REST APIs
- Git & GitHub

---

##  User Roles (Handled at Service Level)
- **Employee** – Apply, view, and cancel leaves
- **Manager** – Approve or reject pending leave requests
- **Admin** – View leave statistics (department-wise)

---

##  Features Implemented
- Leave application with validation
- Leave cancellation rules (only pending leaves)
- Manager approval & rejection logic
- Global exception handling
- Business & resource validation
- Department-wise leave statistics
- Clean layered architecture (Controller → Service → Repository)

---

##  How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/JagadeeshIT/leave-management-service.git

## Navigate to the project:
cd leave-management-service

## Configure database in application.yml
## Run the application:
mvn spring-boot:run

## Notes

Authentication & Authorization will be handled via a separate Auth Service using JWT.
Employee identity is currently resolved via Employee ID.
This service is designed to integrate easily with other microservices.

---


