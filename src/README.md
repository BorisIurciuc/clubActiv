# ClubApp

ClubApp is a web application designed to manage activities, user registrations, and role-based permissions within a school or club setting. The project includes features for users, mentors, and admins, with role-based access to control actions, allowing users to participate in activities and manage their accounts.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Key Entities](#key-entities)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Endpoints](#endpoints)
    - [Activity Management](#activity-management)
    - [User Management](#user-management)
    - [News Management](#news-management)
    - [Reviews and Responses](#reviews-and-responses)
    - [Authentication & Registration](#authentication--registration)
    - [Password Management](#password-management)
- [Role-based Permissions](#role-based-permissions)
- [Testing](#testing)
- [Known Issues](#known-issues)

## Overview

ClubApp allows users to browse activities, register accounts, and participate in activities, with distinct permissions for each role:
- **Admin**: Full permissions, including activity, news, and user management.
- **User**: Limited permissions to view, create, and participate in activities.

This application was created as a graduation project for AIT-TR.

## Features

- **User Registration**: New users can register with email validation to ensure unique registrations.
- **Activity Management**: Admins and users can create, view, update, and delete activities. Users can manage only their own activities.
- **Role-based Access Control**: Different roles (Admin, User) with associated permissions.
- **Password Reset**: Users can reset their passwords via email.
- **Image Upload**: Activity creators can upload images with AWS S3 integration.
- **Secure Authentication**: JWT tokens with refresh token support for secure login.

## Project Structure

The application follows a layered structure:
- **Controller**: REST controllers for handling HTTP requests.
- **Service**: Business logic and application services.
- **Repository**: Interfaces for data persistence.
- **DTOs**: Data transfer objects for structuring API responses.
- **Security**: Configurations for securing endpoints and JWT authentication.

## Key Entities

- **User**: Represents an individual with distinct roles within the application (Admin, User). Each user has a unique email and can register, reset passwords, create their own and join activities.
- **Activity**: Defines an activity that users can join or create.
- **ConfirmationCode**: Manages email confirmations for new user registrations. When a user registers, a confirmation code is generated and sent via email to verify the account.
- **News**: Represents news items related to the club or school. Each news entry includes an identifier (`newsId`), title, content, and optional publication dates.
- **PasswordResetToken**: Handles password reset requests. When a user requests a password reset, a token is generated and sent via email, allowing the user to securely reset their password.
- **Role**: Defines user roles within the application. While not represented as an entity, roles determine access to specific actions within the application, such as creating, viewing, updating, and deleting resources.

## Technologies Used

- **Backend**: Java, Spring Boot, Spring Security, JWT
- **Database**: PostgreSQL
- **Build Tools**: Maven
- **Frontend**: React, Redux
- **Containerization**: Docker
- **Cloud Storage**: AWS S3 for image uploads
- **Testing**: JUnit, Mockito

## Getting Started

### Prerequisites

- Java 17
- Maven
- Docker & Docker Compose
- PostgreSQL

## Endpoints

### Activity Management

- **Public**
    - `GET /api/activity`: Retrieve a list of all activities.
    - `GET /api/activity/{id}/author`: Retrieve the author of an activity by ID.

- **Admin & User**
    - `GET /api/activity/{id}`: View activity details by ID.
    - `POST /api/activity`: Create a new activity.
    - `PUT /api/activity/update/{id}`: Update an activity by ID.
    - `DELETE /api/activity/{id}`: Delete an activity by ID.
    - `PUT /api/activity/{activity_id}/add-user`: Add a user to an activity.
    - `DELETE /api/activity/{activity_id}/remove-user`: Remove a user from an activity.
    - `GET /api/activity/{id}/is-registered`: Check if a user is registered for an activity.
    - `GET /api/activity/my-activities`: Retrieve activities associated with the user.

- **Authenticated Users**
    - `GET /api/activity/user/registered-activities`: View registered activities for the current user.
    - `GET /api/activity/user/activities/created`: View activities created by the current user.

### User Management

- **Admin**
    - `GET /api/users`: Retrieve a list of all users.
    - `DELETE /api/users/{id}`: Delete a user by ID.

- **Admin & User**
    - `GET /api/users/{id}`: View details of a specific user by ID.

- **Authenticated Users**
    - `PUT /api/users/{id}`: Update user details for the current user.

### News Management

- **Admin**
    - `POST /api/news`: Create a new news entry.
    - `PUT /api/news/{id}`: Update a news entry by ID.
    - `DELETE /api/news/{id}`: Delete a news entry by ID.

- **Admin & User**
    - `GET /api/news`: Retrieve a list of all news.
    - `GET /api/news/{id}`: Retrieve details of a specific news entry by ID.

### Reviews and Responses

- **Public**
    - `GET /api/reviews`: Retrieve a list of all reviews.
    - `GET /api/responses/review/{reviewId}`: Retrieve responses associated with a review.

- **Admin & User**
    - `POST /api/reviews`: Create a new review.
    - `POST /api/review/{id}`: Add a review by ID.
    - `POST /api/responses/review/{reviewId}`: Add a response to a review.
    - `DELETE /api/responses/{id}`: Delete a response by ID.

### Authentication & Registration

- **Public**
    - `POST /api/auth/login`: Log in with JWT tokens.
    - `POST /api/auth/refresh`: Refresh JWT tokens.
    - `POST /api/register`: Register a new user.
    - `GET /api/register`: Access registration page.

- **Authenticated Users**
    - `GET /api/auth/me`: Retrieve current user profile.
    - `DELETE /api/auth/logout`: Log out the current user.

### Password Management

- **Public**
    - `POST /api/forgot-password`: Request a password reset.
    - `GET /api/validate-reset-token`: Validate a password reset token.
    - `PUT /api/reset-password`: Reset the password.

## Role-based Permissions

| Role  | Permissions                                                           |
|-------|------------------------------------------------------------------------|
| Admin | Full access to all resources, including user and activity management. |
| User  | View and participate in activities; limited to managing their own account. |

## Testing

Tests are organized in the `test` folder, covering a range of functionality for each service layer. Key tests include:

- **ActivityServiceImplTest**: Covers tests for creating, retrieving, updating, and managing users within activities. Ensures correct handling of user registration, validation, and role-based restrictions in activity management.
- **ConfirmationServiceImplTest**: Tests confirmation code generation and validation for user registration. Ensures that confirmation codes are generated, saved, and validated accurately during the registration process.
- **NewsServiceImplTest**: Focuses on CRUD operations for news entries, validating that news is created, retrieved, updated, and deleted correctly, with role-based access controls for Admins.
- **ResponseServiceImplTest**: Verifies the functionality of creating, retrieving, and deleting responses to reviews, testing permissions for users and admins and ensuring that responses are associated correctly with their respective reviews.
- **ReviewServiceImplTest**: Tests review management functionality, including creating, retrieving, and managing reviews, as well as ensuring that users can leave and manage reviews based on their roles.
- **UserServiceImplTest**: Includes tests for user registration, login, password reset, and profile updates. Also checks email uniqueness validation and password encryption to ensure secure handling of user data.


