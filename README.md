# OpenRental Booking Service

Microservice responsible for vehicle bookings and availability checks.

## Features

- Check vehicle availability for selected dates
- Create vehicle bookings
- Communication with Vehicles Service
- PostgreSQL persistence
- Docker support
- Kubernetes deployment

## Tech Stack

- Java 25
- Spring Boot 4
- PostgreSQL
- RestClient
- Maven
- Docker
- Kubernetes (kind)

## Architecture

Booking Service communicates with Vehicles Service via REST API.

Flow:
1. Booking Service requests matching vehicles from Vehicles Service
2. Booking Service filters unavailable vehicles based on existing reservations
3. Available vehicles are returned to the client

Booking Service also checks the existence of a vehicle before saving a reservation.

## Run with Kubernetes

Build image:
```bash
./mvnw clean package

docker build -t openrental-booking:1.0 .

kind load docker-image openrental-booking:1.0 --name openrental
```
Deploy (files in project openrental-infra):
```bash
kubectl apply -f k8s/base/booking
```

## Access API
```bash
kubectl port-forward service/booking 8081:8080
```
API available at:

http://localhost:8081

## API Examples

### Check available vehicles

GET /booking/api/v1/available-cars

Example:
```text
/booking/api/v1/available-cars?brand=Honda&seats=5&startDate=2026-10-07&endDate=2026-10-08
```
## Planned Improvements

- Authentication & Authorization
- Notification service
- Kafka integration
- API Gateway / Ingress

