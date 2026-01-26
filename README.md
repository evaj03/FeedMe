# FeedMe Recipe Web App

## Overview
FeedMe is a free-to-use web application for managing recipes, creating meal planners, and generating shopping lists. Users can browse a collection of main meal recipes, add their own, organize weekly meal plans, and consolidate ingredients for easy shopping. The app is designed for convenience, accessibility, and scalability, with secure user authentication and sharing features.

## Features
- User registration, login, and secure authentication
- Browse, search, filter, and sort recipes
- Add, edit, and delete personal recipes
- Create and manage weekly meal planners
- Generate and manage shopping lists from meal planners
- Share recipes and meal planners via public links
- User profile management
- Responsive and accessible UI

## Tech Stack
- **Frontend:** React, TypeScript, Material-UI/Chakra UI, React Router, Axios
- **Backend:** Java (Spring Boot), Spring Security, Spring Data JPA, JavaMail, RESTful API
- **Database:** PostgreSQL (AWS RDS)
- **File/Image Storage:** AWS S3
- **Other AWS Services:** CloudFront (CDN), SES (email), IAM (access control)

## Architecture
```mermaid
flowchart TD
    A[User Browser]
    B[React Frontend (AWS Amplify/S3 + CloudFront)]
    C[Spring Boot REST API (AWS Elastic Beanstalk/ECS)]
    D[AWS RDS (Database)]
    E[AWS S3 (Image/File Storage)]
    F[AWS SES (Email Service)]

    A --> B
    B --> C
    C --> D
    C --> E
    C --> F
```

## Database Schema
See [DB-SCHEMA.md](./DB-SCHEMA.md) for a detailed, normalized schema supporting structured ingredients, meal planners, shopping lists, and sharing.

## Requirements & Rules
- Functional and non-functional requirements are detailed in [REQUIREMENTS.md](./REQUIREMENTS.md)
- Coding, architecture, and workflow standards are defined in [RULESET.md](./RULESET.md)

## Setup & Running
1. **Clone the repository:**
   ```bash
   git clone <your-repo-url>
   cd FeedMe
   ```
2. **Install frontend dependencies:**
   ```bash
   cd frontend
   npm install
   npm start
   ```
3. **Setup backend:**
   - Ensure Java 17+ and PostgreSQL are installed
   - Configure environment variables for AWS and database access
   - Build and run Spring Boot app:
     ```bash
     cd backend
     ./mvnw spring-boot:run
     ```
4. **Database migration:**
   - Use provided schema in [DB-SCHEMA.md](./DB-SCHEMA.md) to create tables
   - Optionally use Flyway or Liquibase for migrations

## Testing
- Frontend: `npm test` (Jest, React Testing Library)
- Backend: `./mvnw test` (JUnit, Mockito)

## Contribution
- Follow the standards in [RULESET.md](./RULESET.md)
- One prompt = one commit, with tests and documentation
- Update [REQUIREMENTS.md](./REQUIREMENTS.md) and [DB-SCHEMA.md](./DB-SCHEMA.md) as features evolve

## License
This project is free to use and open source. See LICENSE for details.

## Contact
For questions or support, open an issue or contact the maintainer.

