# Company Z Portal & Security Management System

A Java-based Data Access Object (DAO) application featuring Role-Based Access Control (RBAC) for managing employee data, generating payroll reports, and executing automated password rotation workflows.

---

## System Architecture

The project follows a standard multi-tier design separating data access, core models, external integrations, and user interfaces:

├── models/
│   ├── User.java                   # Access levels and authorization attributes
│   ├── Employee.java               # Core employee domain entity
│   └── Address.java                # Relational address data structure
├── dao/
│   ├── UserDAO.java                # Authentication logic
│   └── EmployeeDAO.java            # Employee CRUD operations and payroll aggregations
├── services/
│   ├── Security.java               # Cryptographic utilities and password generation
│   └── PasswordRotationService.java # Scheduled SMTP automated password rotation
├── config/
│   └── DatabaseConfig.java         # JDBC connection manager
└── entrypoints/
├── EmployeeTestMyVersion.java  # Interactive portal CLI interface
└── SoroTest.java               # Secondary interactive dashboard shell

---

## Key Features

* **Role-Based Access Control (RBAC):**
  * **`HR_ADMIN` Portal:** Employee record lookup, percentage-based salary adjustments with cap validations, and payroll aggregation reports by division or job title.
  * **`EMPLOYEE` Portal:** Self-service viewing of demographic data and personal pay history.
* **Database Aggregations & Reporting:**
  * Multi-table `LEFT JOIN` queries linking `employees`, `addresses`, `cities`, and `states`.
  * Division and job title payroll summaries calculated over configurable date ranges.
* **Security & Automation:**
  * **Password Generator:** Secure password generation leveraging Java's `SecureRandom` OS entropy.
  * **Automated Rotation Service:** Scheduled monthly job utilizing the JavaMail API over TLS to rotate credentials and dispatch notifications via email.

---

## Configuration & Environment Setup

To keep sensitive database and email credentials secure, the repository utilizes environment variables or runtime configuration parameters.

### Required Environment Variables

| Variable | Description |
| :--- | :--- |
| `DB_URL` | JDBC Connection URL (e.g., `jdbc:mysql://localhost:3306/your_db`) |
| `DB_USER` | MySQL Database Username |
| `DB_PASS` | MySQL Database Password |
| `SMTP_USER` | Email address used by `PasswordRotationService` |
| `SMTP_PASS` | Password or App Key for the SMTP service |

*Note: Ensure `DatabaseConfig.java` is updated locally or configured via system environment variables before attempting to run the application.*

---

## Prerequisites & Dependencies

* **Java Development Kit (JDK):** Version 17 or higher
* **Database:** MySQL
* **Libraries Required:**
  * MySQL Connector/J (`com.mysql.cj.jdbc.Driver`)
  * JavaMail API (`javax.mail` / `jakarta.mail`)

---

## Getting Started

### 1. Database Setup
Initialize your MySQL database using your schema definition (tables required: `employees`, `payroll`, `users`, `addresses`, `cities`, `states`, `divisions`, `employee_division`, `employee_job_titles`, `job_titles`).

### 2. Compilation
Compile all source files from your project root:

```bash
javac -cp "libs/*" -d bin src/**/*.java
