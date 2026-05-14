# OpenRental Booking

OpenRental Booking is a Spring Boot service for managing bookings in a rental system.
It provides backend functionality for managing rental bookings, including booking creation, retrieval, and integration with persistence and deployment infrastructure.

## Features

- Booking management for rental resources
- Integration with OpenRental Vehicles application
- REST-style API support
- Database-backed persistence
- Spring-based application architecture
- Container-ready deployment
- Kubernetes deployment support
- Environment-based configuration for secure runtime settings

## Tech Stack

- Java 25
- Spring MVC
- Spring Data JPA
- Maven
- Docker
- Kubernetes
- Relational database support through JDBC/JPA

## Requirements

Before running the project, make sure you have the following installed:

- Java 25
- Maven, or use the included Maven Wrapper
- Docker, if running with containers
- Kubernetes CLI, if deploying to Kubernetes
- PostgreSQL, if running without Docker

## Configuration

The application should be configured using environmental variables or external configuration files.

Common environmental variables may include:
```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/openrental_booking 
SPRING_DATASOURCE_USERNAME=your_username 
SPRING_DATASOURCE_PASSWORD=your_password
```

## Build the Application

Using the Maven Wrapper:
```bash
./mvnw clean package
```

## Run Locally

After building the project, run the generated JAR:
```bash
java -jar target/*.jar
```

By default, the service runs on:
```text
localhost:8080
```

## Run with Docker
Build the Docker image:
```bash 
docker build -t openrental-booking .
```
Run the container:
```bash 
docker run -p 8081:8080
-e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/openrental_booking
-e SPRING_DATASOURCE_USERNAME=your_username
-e SPRING_DATASOURCE_PASSWORD=your_password
openrental-booking
```

The service will be available at:
```text
localhost:8081
```

## Run with Docker Compose
Requires setting value for POSTGRES_PASSWORD environmental variable.
```bash 
docker compose up --build
```

## Run with Kubernetes
Apply the Kubernetes manifests from the project openrental-infra.
