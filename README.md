# Coursework Deadline Optimiser

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-green)
![H2 Database](https://img.shields.io/badge/Database-H2-lightgrey)
![Maven](https://img.shields.io/badge/Build-Maven-orange)

A backend **Spring Boot REST API** designed to help students manage coursework deadlines, modules, and academic workload efficiently.

The system models real-world university structures using a clean layered architecture (Controller → Service → Repository) and demonstrates proper use of **JPA relationships, DTOs, and REST design principles**.

---

## Key Features

- Manage Students, Modules, and Coursework
- Strong relational model (Student ↔ Coursework ↔ Module)
- Track deadlines and estimated workload
- DTO-based request handling for clean API design
- H2 in-memory database for fast development/testing
- Full CRUD REST API
- Ready for unit & integration testing expansion

---

## System Design

This project follows a **layered architecture pattern**:
