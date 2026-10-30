# Bank of Kigali Customer Complaint Management System Backend

## Student Information

- **Name:** Uwera Adeline
- **Student ID:** 27520
- **Institution:** Adventist University of Central Africa (AUCA)
- **Program:** Information Technology (IT)

## Project Description

The Bank of Kigali Customer Complaint Management System Backend is a REST API application developed using Java and Spring Boot.

The system is designed to manage customer complaints and their resolution processes. It provides functionality for managing customers, complaint types, service desks, support agents, complaints, and resolution actions.

## Technologies Used

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- REST API
- Postman
- Visual Studio Code

## Database Configuration

- **Database:** PostgreSQL
- **Database Name:** bk_complaints_db

Configure your local database credentials in `src/main/resources/application.properties`. This file is not included in the public repository for security reasons.

## Project Structure

```text
src/
 ├── main/
 │    ├── java/
 │    │    └── rw/bk/complaints/
 │    └── resources/
 └── test/
pom.xml
mvnw.cmd
README.md
```

## How to Run the Project

1. Install Java 21.
2. Install PostgreSQL.
3. Create a database named `bk_complaints_db`.
4. Configure your local database connection.
5. Open the project folder in Visual Studio Code.
6. Open the terminal in the project directory.
7. Run:

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs at:

`http://localhost:8080`

## API Testing

The REST API endpoints can be tested using Postman.

## Author

**Uwera Adeline**

Adventist University of Central Africa (AUCA)