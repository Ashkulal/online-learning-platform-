# Online Learning Platform

**Live Demo:** [https://online-learning-platform-2-8gkr.onrender.com/login](https://online-learning-platform-2-8gkr.onrender.com/login)

A full-stack Spring Boot application for an online learning platform. 

## Overview
This platform allows users to browse courses, enroll as students, and track their progress. It also includes an admin dashboard for managing courses and monitoring student progress.

## Technologies Used
* **Backend**: Java, Spring Boot
* **Frontend**: HTML, CSS, Thymeleaf
* **Database**: MongoDB

## MongoDB configuration

The application uses MongoDB for users, courses, and enrollments. For local
development, it connects to `mongodb://localhost:27017/learningplatform` by
default. Set `SPRING_DATA_MONGODB_URI` to a MongoDB Atlas connection URI when
using Atlas or deploying to Render. The URI must include a database name in its
path, for example
`mongodb+srv://<user>:<password>@<cluster>.mongodb.net/learningplatform?retryWrites=true&w=majority`.

In Render, set `SPRING_DATA_MONGODB_URI` as a secret environment variable on the
web service. Create a MongoDB database user and ensure the Atlas network access
list allows connections from the deployed service. If the Atlas-provided URI
has an empty path (for example, it ends in `.mongodb.net/?...`), add the
database name before the query string, such as
`.mongodb.net/learningplatform?....` Keep the URI out of source control. If
`SPRING_DATA_MONGODB_URI` is unset, the app falls back to the local
`learningplatform` database URI.

Existing H2 data is not migrated automatically; users and courses must be
re-created in MongoDB or migrated separately.

## Credits / Authors
* **Akash H R** (USN: 4cb22cs006)
* **Bhavith** (USN: 4cb22cb014)

## Local Development Notes

- Java 21 recommended
- Run with .\mvnw.cmd spring-boot:run`n- Set SPRING_DATA_MONGODB_URI for Atlas or run MongoDB locally
