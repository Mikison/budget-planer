# Home Budget Planner

## Overview

Home Budget Planner is a comprehensive web-based application designed to assist users in managing and planning their budgets on a monthly basis. Users can create categories for budgeting, log expenses within these categories, and track income. The application incorporates robust security features using Spring Security with JWT authentication, supporting two user roles: ADMIN and USER. It also features a report service that automatically generates and sends PDF reports weekly or monthly based on user preferences or on-demand.

## Features

- **Budget Management**: Plan and manage budgets for each month with specific categories.
- **Expense Tracking**: Log and categorize expenses to maintain detailed financial records.
- **Income Recording**: Track different sources of income to get a comprehensive view of financial inflow.
- **Automated Reporting**: Generate and receive PDF reports on a weekly or monthly basis, scheduled or on-demand.
- **Email Notifications**: Automatically receive budget reports via email.
- **Role-Based Access Control**: Secure user roles with specific permissions for ADMIN and USER.
- **Secure Authentication**: Leverage JWT for secure and efficient user authentication.

## Tech Stack

### Backend
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- Spring AMQP with RabbitMQ
- Lombok

### Testing
- JUnit
- Mockito

### Database
- PostgreSQL

### Other Technologies
- Docker for containerization
- Swagger for API documentation

### CI/CD
- Jenkins: Automated Jenkins pipeline for continuous integration and deployment to Docker Hub.

## Getting Started

### Prerequisites
- JDK 11 or higher
- Docker and Docker Compose
- Maven

### Installation and Local Development


1. Clone project

  ``` bash      
   git clone https://github.com/Mikison/budget-planer.git
  ```

2. Open cloned directory
  ``` bash      
   cd budget-planer
  ```

3. Build project

  ``` bash
  mvn clean install
  ```

4. Go to docker directory

  ``` bash      
   cd docker
  ```

5. Run using docker-compose 

  ``` bash
  docker-compose up -d
  ```
