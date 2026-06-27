# Coursework Deadline Optimiser

A comprehensive academic workload management system comprising a Spring Boot REST backend and a vanilla HTML/CSS/JavaScript frontend. This application helps students manage coursework deadlines, schedule study sessions, and optimise their workload allocation.

This branch is an extension of the original project. The frontend was built with AI assistance as a clean demonstration of the backend — it is not the focus of the project. The backend is built entirely by me using Spring Boot and Java, following a layered architecture with RESTful APIs, JPA persistence, Spring Security, and JWT authentication.

Live demo: https://coursework-deadline-optimiser.onrender.com/

## Overview

The Coursework Deadline Optimiser is a full-stack web application designed to:
- Track multiple students, their academic modules, and coursework assignments
- Allocate study time efficiently based on deadlines and workload priorities
- Generate optimised study schedules with per-student daily capacity limits
- Provide an intuitive web-based interface for managing academic data
- Secure all data behind JWT-based authentication with role-based access control

## Technology Stack

**Backend:**
- Java 26
- Spring Boot 4.1.0
- Spring Security 7.x (JWT authentication, BCrypt password hashing)
- Spring Data JPA / Hibernate
- PostgreSQL (production)
- H2 Database (development)
- Maven

**Frontend:**
- HTML5
- CSS3
- Vanilla JavaScript (no frameworks)
- Fetch API for backend communication

## Key Features

**Core Functionality:**
- Manage Students with configurable daily study capacity
- Manage Modules with course codes and credit information
- Manage Coursework assignments with deadlines, weightings, and estimated hours
- Full CRUD operations for all entities
- Per-student daily study hour limits (default 4 hours/day, customisable per student)

**Security:**
- User registration and login via `/api/auth/register` and `/api/auth/login`
- Passwords hashed with BCrypt
- Stateless JWT authentication — token issued on login, validated on every request
- Role-based access control (ROLE_USER, ROLE_ADMIN)
- All API endpoints protected; only auth endpoints are public

**Schedule Generation:**
- Intelligent schedule optimiser that generates daily study plans
- Groups coursework by student and respects individual daily capacity limits
- Allocates study time based on deadline urgency and task priority
- Distributes workload over an upcoming 60-day window

**Frontend Interface:**
- Login and register pages
- Dashboard with navigation to all sections
- Students page: add, view, and delete students with max hours/day configuration
- Modules page: manage academic modules with add/edit/delete capabilities
- Courseworks page: manage individual coursework with student and module assignments
- Schedule page: generate optimised schedules and view results grouped by date
- Logout button clears JWT token and redirects to login

## Architecture

The backend follows a **layered architecture pattern**:

1. **Controller Layer** (`/controller`)
   - RESTful API endpoints for each entity
   - Auth endpoints (`/api/auth/register`, `/api/auth/login`)
   - Request/Response handling and HTTP method routing

2. **Service Layer** (`/service`)
   - Business logic and calculations
   - Schedule generation and optimisation
   - User registration and login logic

3. **Repository Layer** (`/repo`)
   - Spring Data JPA repositories
   - Database query abstraction

4. **Model Layer** (`/model`)
   - JPA entity classes
   - Database schema definition
   - Relationships and constraints

5. **DTO Layer** (`/dto`)
   - Data Transfer Objects for request payloads
   - Input validation and serialisation

6. **Security Layer** (`/security`)
   - `SecurityConfig` — filter chain, endpoint rules, session policy
   - `JwtAuthFilter` — validates JWT on every request
   - `JwtService` — token generation and parsing
   - `AuthController` — register and login endpoints

## Project Structure

```
src/main/java/com/chiggs/coursework_deadline_optimiser/
├── controller/           # REST API endpoints
├── service/             # Business logic
│   └── optimisation/    # Schedule optimisation algorithms
├── repo/                # JPA repositories
├── model/               # Entity classes (Student, Module, Coursework, StudySession, Users)
├── dto/                 # Request DTOs
└── security/            # Spring Security, JWT filter, auth controller

src/main/resources/
├── static/              # Frontend (HTML, CSS, JS)
├── application.properties          # Shared config + default profile (dev)
├── application-dev.properties      # H2 config for local development
├── application-prod.properties     # PostgreSQL config for production
└── data.sql             # Sample data initialisation
```

## REST API Endpoints

**Auth (public):**
- POST /api/auth/register — register a new user
- POST /api/auth/login — login and receive a JWT token

**Students (authenticated):**
- GET /api/students
- GET /api/students/{id}
- POST /api/students
- PUT /api/students/{id}
- DELETE /api/students/{id}

**Modules (authenticated):**
- GET /api/modules
- GET /api/modules/{id}
- POST /api/modules
- PUT /api/modules/{id}
- DELETE /api/modules/{id}

**Courseworks (authenticated):**
- GET /api/courseworks
- GET /api/courseworks/{id}
- POST /api/courseworks
- PUT /api/courseworks/{id}
- DELETE /api/courseworks/{id}

**Schedule (authenticated):**
- GET /api/schedule/generate
- GET /api/schedule

## Data Models

**Users:**
- id (Long)
- username (String, unique)
- password (String, BCrypt hashed)
- role (Role enum: ROLE_USER, ROLE_ADMIN)

**Student:**
- id (Long)
- name (String)
- email (String)
- maxHoursPerDay (Integer) — daily study capacity, defaults to 4

**Module:**
- id (Long)
- name (String)
- moduleCode (String)
- credits (Integer)

**Coursework:**
- id (Long)
- title (String)
- deadline (LocalDate)
- weighting (Integer)
- estimatedHours (Integer)
- difficulty (Integer)
- progress (Integer)
- student (Student)
- module (Module)

**StudySession:**
- id (Long)
- date (LocalDate)
- task (String)
- hoursAllocated (Integer)
- coursework (Coursework)

## Getting Started

**Prerequisites:**
- Java 26
- Maven 3.6+
- PostgreSQL (for production profile)
- Modern web browser

**Build and Run (dev — uses H2):**
```bash
mvn clean install
./mvnw spring-boot:run
```

The default profile is `dev`, which uses H2 in-memory database. No setup required.

**Run with PostgreSQL (prod):**
```bash
# Set active profile to prod
# In application.properties: spring.profiles.active=prod
# Then configure application-prod.properties with your DB credentials
./mvnw spring-boot:run
```

The application starts at `http://localhost:8080`

## Using the Application

1. Navigate to `http://localhost:8080/register.html` to create an account
2. Login at `http://localhost:8080/login.html`
3. On successful login you are redirected to the dashboard
4. Use the navigation to manage Students, Modules, and Courseworks
5. Generate a schedule from the Schedule page
6. Logout via the button in the navigation bar

## Security Flow

1. User registers — password is BCrypt hashed and stored in the `users` table
2. User logs in — credentials validated, JWT token returned
3. Frontend stores the JWT in `localStorage`
4. Every subsequent API request includes `Authorization: Bearer <token>` header
5. `JwtAuthFilter` intercepts each request, validates the token, and sets the security context
6. Unauthenticated requests to protected endpoints return `401`; frontend redirects to login

## Configuration

**Spring Profiles:**
- `dev` — H2 in-memory database, `ddl-auto=update`, H2 console enabled
- `prod` — PostgreSQL, `ddl-auto=update`

Set the active profile in `application.properties`:
```properties
spring.profiles.active=dev
```

**JWT:**
```properties
jwt.secret=your-secret-key
jwt.expiration=86400000
```

## Schedule Optimisation Algorithm

1. For each day over 60 days:
   - Identify all courseworks with work remaining and not past deadline
   - Group courseworks by assigned student
   - For each student independently:
      - Calculate priority weight (deadline proximity, difficulty, weighting)
      - Allocate daily capacity proportionally to priorities
      - Ensure no allocation exceeds remaining work or student capacity
      - Generate and persist study sessions

2. Fair Distribution:
   - Respects per-student daily capacity limits
   - Prevents one student's workload from affecting another's schedule

## Planned Features

**Per-user data isolation**
Each user will only be able to view and manage their own students, modules, coursework, and schedule. Currently all authenticated users share the same data pool.

**Admin panel**
A dedicated admin interface for users with `ROLE_ADMIN`, including the ability to view all users, delete accounts, and promote users to admin.

**Global exception handling**
A `@ControllerAdvice` class to intercept all exceptions and return clean, consistent JSON error responses across the entire API instead of Spring's default error pages.

**Input validation**
Bean Validation annotations (`@NotBlank`, `@Size`, `@Min`) on all DTO classes with proper `400 Bad Request` responses when invalid data is submitted.

**API documentation**
Springdoc/Swagger UI integration, providing an interactive API explorer at `/swagger-ui.html` so the full API can be browsed and tested without any external tools.

**Docker**
A `Dockerfile` and `docker-compose.yml` to containerise the application and PostgreSQL database, allowing the entire stack to be spun up with a single `docker compose up` command.

**Email notifications**
Automated deadline reminder emails using Spring Mail, notifying users when a coursework deadline is approaching based on their schedule.

**Export to CSV/ICS**
Allow users to download their generated schedule as a CSV file or an ICS calendar file for import into Google Calendar, Outlook, or Apple Calendar.