# Online Learning Platform

A full-stack Spring Boot application for an online learning platform. It features course management, student enrollment, and server-side progress tracking.

## Features

*   **Course Browsing & Enrollment**: Students can view available courses and enroll in them.
*   **Progress Tracking**: Reliable server-side tracking of completed lessons, storing progress in the database to eliminate client-side state synchronization issues.
*   **Admin Dashboard**: Administrators can view real-time student progress and course statistics.
*   **Thymeleaf UI**: Server-side rendered pages for dynamic content display.
*   **Render Ready**: Pre-configured `Dockerfile` and `render.yaml` for easy deployment to Render.com.

## Technology Stack

*   **Backend**: Java 17, Spring Boot 3.1.0
*   **Frontend**: HTML/CSS, Thymeleaf Templates
*   **Database**: H2 (file-based persistence), MySQL/PostgreSQL support included.
*   **ORM**: Spring Data JPA / Hibernate

## Getting Started

### Prerequisites

*   [Java Development Kit (JDK) 17](https://adoptium.net/)
*   [Maven](https://maven.apache.org/) (or use the included Maven wrapper)

### Running Locally

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd "major project 1"
    ```

2.  **Build the application:**
    ```bash
    ./mvnw clean install
    ```
    *(On Windows, you can use `mvnw.cmd clean install`)*

3.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```

4.  **Access the application:**
    Open your web browser and navigate to `http://localhost:9090`.

### Database Configuration

By default, the application uses an H2 database configured with file-based persistence (saving data to the `./data/learning_platform` folder). You can change this configuration in `src/main/resources/application.properties`.

```properties
spring.datasource.url=jdbc:h2:file:./data/learning_platform;AUTO_SERVER=TRUE
spring.datasource.username=sa
spring.datasource.password=
```

## Deployment

This project is configured for deployment on [Render.com](https://render.com/).

It uses Docker for containerization, ensuring the Java environment is correctly set up instead of defaulting to Node.js. The included `Dockerfile` builds and packages the Spring Boot application using Maven and runs it via an OpenJDK base image.

1.  Connect your GitHub repository to Render.
2.  Create a new "Web Service" on Render.
3.  Render will automatically detect the `render.yaml` and `Dockerfile` to build and deploy the application.
