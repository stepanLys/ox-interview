
# CRM System

This project is a monorepo and uses docker-compose for orchestration.

## Prerequisites

Before you begin, ensure you have the following installed:
* Git
* Docker Desktop (make sure it is running)
* Maven (for building the .jar files locally)
* JDK 25 (or the version specified in your pom.xml)

## First-time Setup
These steps only need to be performed once to prepare the project.
1. Clone the repository:
```bash
git clone https://github.com/stepanLys/ox-interview.git
cd ox-interview
```
2. Build the local .jar files: Our Dockerfiles (for crm-service and notification-service) use layertools and expect the .jar files to exist. We must build them locally first.
Run this command from the root project folder:
```bash
mvn clean install -U
```
## Running with Docker Compose
1. Start everything in the background. Run this command from the root project folder:
```bash
docker-compose up -d --build
```
## Accessing the Services
Once running, the services are available at these addresses:
* Main Application (Frontend): `http://localhost:3000`
* Swagger API (Backend): `http://localhost:8080/swagger-ui/index.html`
* Kafka UI (Topic Monitoring): `http://localhost:8090`
