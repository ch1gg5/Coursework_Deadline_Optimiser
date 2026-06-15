# Coursework Deadline Optimiser

A comprehensive academic workload management system comprising a Spring Boot REST backend and a vanilla HTML/CSS/JavaScript frontend. This application helps students manage coursework deadlines, schedule study sessions, and optimize their workload allocation.
This branch is an extension of the original project. I made the frontend using Ai as i dont have much experience in frontend development, it is just to demonstrate the backend of the project in a clean way. The backend is built all by me using Spring Boot and Java, while the frontend is a simple HTML/CSS/JavaScript interface that communicates with the backend via RESTful APIs.

## Overview

The Coursework Deadline Optimiser is a full-stack web application designed to:
- Track multiple students, their academic modules, and coursework assignments
- Allocate study time efficiently based on deadlines and workload priorities
- Generate optimized study schedules with per-student daily capacity limits
- Provide an intuitive web-based interface for managing academic data

## Technology Stack

Backend:
- Java 17+
- Spring Boot 4.x
- Spring Data JPA
- H2 Database (in-memory for development)
- Maven (build tool)

Frontend:
- HTML5
- CSS3
- Vanilla JavaScript (no frameworks)
- Fetch API for backend communication

## Key Features

Core Functionality:
- Manage Students with configurable daily study capacity
- Manage Modules with course codes and credit information
- Manage Coursework assignments with deadlines, weightings, and estimated hours
- Full CRUD operations for all entities
- Per-student daily study hour limits (default 4 hours/day, customizable per student)

Schedule Generation:
- Intelligent schedule optimizer that generates daily study plans
- Groups coursework by student and respects individual daily capacity limits
- Allocates study time based on deadline urgency and task priority
- Distributes workload over an upcoming 60-day window

Frontend Interface:
- Dashboard with navigation to all sections
- Students page: add, view, and delete students with max hours/day configuration
- Modules page: manage academic modules with add/edit/delete capabilities
- Courseworks page: manage individual coursework with student and module assignments
- Schedule page: generate optimized schedules and view results grouped by date
- Responsive tables showing task, coursework, module, student, and hours allocated

Data Model:
- Strong relational design (Student, Module, Coursework, StudySession)
- JPA relationships: Student ↔ Coursework ↔ Module
- Study sessions automatically generated and linked to coursework

## Architecture

The backend follows a **layered architecture pattern**:

1. **Controller Layer** (`/controller`)
   - RESTful API endpoints for each entity
   - Request/Response handling
   - HTTP method routing

2. **Service Layer** (`/service`)
   - Business logic and calculations
   - Schedule generation and optimization
   - Data transformation between models and DTOs

3. **Repository Layer** (`/repo`)
   - Spring Data JPA repositories
   - Database query abstraction

4. **Model Layer** (`/model`)
   - JPA entity classes
   - Database schema definition
   - Relationships and constraints

5. **DTO Layer** (`/dto`)
   - Data Transfer Objects for request payloads
   - Input validation and serialization

## Project Structure

```
src/main/java/com/chiggs/coursework_deadline_optimiser/
├── controller/           # REST API endpoints
├── service/             # Business logic
│   └── optimisation/    # Schedule optimization algorithms
├── repo/                # JPA repositories
├── model/               # Entity classes
└── dto/                 # Request DTOs

src/main/resources/
├── static/              # Frontend (HTML, CSS, JS)
├── application.properties
└── data.sql            # Sample data initialization

frontend/               # Standalone frontend copy
├── *.html
├── style.css
└── script.js
```

## REST API Endpoints

Students:
- GET /api/students - Get all students
- GET /api/students/{id} - Get student by ID
- POST /api/students - Create new student
- PUT /api/students/{id} - Update student
- DELETE /api/students/{id} - Delete student

Modules:
- GET /api/modules - Get all modules
- GET /api/modules/{id} - Get module by ID
- POST /api/modules - Create new module
- PUT /api/modules/{id} - Update module
- DELETE /api/modules/{id} - Delete module

Courseworks:
- GET /api/courseworks - Get all courseworks
- GET /api/courseworks/{id} - Get coursework by ID
- POST /api/courseworks - Create new coursework
- PUT /api/courseworks/{id} - Update coursework
- DELETE /api/courseworks/{id} - Delete coursework

Schedule:
- GET /api/schedule/generate - Generate optimized schedule
- GET /api/schedule - Get current schedule

## Data Models

Student:
- id (Long)
- name (String)
- email (String)
- maxHoursPerDay (Integer) - Daily study capacity, defaults to 4

Module:
- id (Long)
- name (String)
- moduleCode (String)
- credits (Integer)

Coursework:
- id (Long)
- title (String)
- deadline (LocalDate)
- weighting (Integer) - Assignment weight/priority
- estimatedHours (Integer) - Total hours needed
- difficulty (Integer)
- progress (Integer) - Completion percentage
- student (Student) - Assigned student
- module (Module) - Associated module

StudySession:
- id (Long)
- date (LocalDate)
- task (String)
- hoursAllocated (Integer)
- coursework (Coursework) - Associated coursework

## Getting Started

Prerequisites:
- Java 17 or higher
- Maven 3.6+
- Modern web browser

Build and Run:
```bash
# Clone the repository
cd Coursework_Deadline_Optimiser

# Build the project
mvn clean install

# Run the application
mvnw spring-boot:run
```

The application will start at http://localhost:8080

## Using the Application

Frontend Access:
Open your browser and navigate to http://localhost:8080/index.html

Creating Data:
1. Go to Students page - Add students with their daily study capacity
2. Go to Modules page - Add academic modules
3. Go to Courseworks page - Create coursework assignments linked to students and modules
4. Set realistic deadlines and estimated hours for each assignment

Generating Schedule:
1. Go to Schedule page
2. Click "Generate Schedule" button
3. The system will create an optimized study plan respecting each student's daily capacity
4. View results grouped by date with breakdown by student, module, and task

Schedule Display:
- Each date shows a table with Task, Coursework, Module, Student, and Hours allocated
- Daily totals per student ensure nobody exceeds their capacity limit
- Schedule spans 60 days from today

## Configuration

Default Study Capacity:
- The default maximum daily study hours is 4 across all students
- Individual student capacity can be overridden when creating or editing a student
- Setting maxHoursPerDay to null uses the system default

Database:
- Uses H2 in-memory database for rapid development
- Database schema auto-generates on startup
- Sample data (if provided) loads from data.sql

## Schedule Optimization Algorithm

The schedule generation employs a weighted allocation strategy:

1. For each day over 60 days:
   - Identify all courseworks with work remaining and not past deadline
   - Group courseworks by assigned student
   - For each student independently:
     - Calculate priority weight for each coursework (based on deadline, difficulty, weighting)
     - Allocate the student's daily capacity proportionally to coursework priorities
     - Ensure no allocation exceeds remaining work or student capacity
     - Generate study sessions and save to database

2. Priority Calculation:
   - Considers deadline proximity
   - Factors in coursework weighting and difficulty
   - Accounts for remaining workload

3. Fair Distribution:
   - Respects per-student daily capacity limits
   - Prevents one student's work from limiting another's schedule
   - Ensures work is distributed fairly across the planning window

## Notes

Frontend Hosting:
The frontend is served directly by Spring Boot from src/main/resources/static/
A standalone copy is maintained in the frontend/ folder for reference

No External Dependencies:
Frontend uses only vanilla JavaScript, HTML, and CSS
No framework dependencies - runs in any modern browser
Fetch API communicates with backend REST endpoints

Development:
The project is structured for easy extension
Services can be unit tested independently
DTOs provide clean API contracts
Repository layer abstracts database operations

## Future Enhancements

Potential improvements:
- User authentication and authorization
- Export schedules to calendar formats (ICS, SVG)
- Collaborative scheduling for group projects
- Progress tracking with completion logging
- Advanced filtering and search capabilities
- Analytics dashboard for workload insights

