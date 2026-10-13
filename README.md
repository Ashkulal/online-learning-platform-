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
using Atlas or deploying to Render.

In Render, set `SPRING_DATA_MONGODB_URI` as a secret environment variable on the
web service. Create a MongoDB database user and ensure the Atlas network access
list allows connections from the deployed service. Keep the URI out of source
control.

Existing H2 data is not migrated automatically; users and courses must be
re-created in MongoDB or migrated separately.

## Credits / Authors
* **Akash H R** (USN: 4cb22cs006)
* **Bhavith** (USN: 4cb22cb014)
