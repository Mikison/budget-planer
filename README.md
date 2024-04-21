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
<p>
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white"/>
  <img alt="Spring Web" src="https://img.shields.io/badge/Spring%20Web-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
  <img alt="Spring Security" src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white"/>
  <img alt="Spring Data JPA" src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
  <img alt="Spring AMQP" src="https://img.shields.io/badge/Spring%20AMQP-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
  <img alt="RabbitMQ" src="https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white"/>
  <img alt="Lombok" src="https://img.shields.io/badge/Lombok-000000?style=for-the-badge&logo=lombok&logoColor=white"/>
</p>

### Testing
<p>
  <img alt="JUnit" src="https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit5&logoColor=white"/>
  <img alt="Mockito" src="https://img.shields.io/badge/Mockito-25A162?style=for-the-badge&logo=mockito&logoColor=white"/>
</p>

### Database
<p>
  <img alt="PostgreSQL" src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white"/>
</p>

### Other Technologies
<p>
  <img alt="Docker" src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"/>
  <img alt="Swagger" src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white"/>
</p>

### CI/CD
<img alt="Jenkins" src="https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white"/>


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
