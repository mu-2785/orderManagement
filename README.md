# Order Platform

Production-style modular monolith used as a backend engineering simulation.

## Stack
Java 21, Spring Boot 4, Spring Web MVC, Spring Data JPA, MySQL, Flyway,
MapStruct, Validation, Actuator, OpenAPI, JUnit, Mockito, Testcontainers,
Maven and Docker.

## Profiles
- dev: local Docker MySQL
- test: Testcontainers MySQL
- prod: environment-variable based database configuration

## Local startup
```bash
docker compose up -d mysql
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Swagger: http://localhost:8080/swagger-ui.html
Health: http://localhost:8080/actuator/health

Tests:
```bash
mvn test
```

Do not implement backlog items without a ticket. This project is developed incrementally as a simulated company codebase.
