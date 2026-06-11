# SocialConnect

SocialConnect is an original full-stack social networking MVP. It uses common social media features such as profiles, posts, reactions, comments, friend requests, notifications, image uploads, and a personalized feed without using any real company branding or copied assets.

## Tech Stack

- Backend: Java 17, Spring Boot 3, Spring Security, JWT, Spring Data JPA, MySQL, Maven
- Frontend: React, Vite, TailwindCSS v4, Axios, React Router, Context API
- Database: MySQL

## Features

- Register, login, JWT authentication, and authenticated `/api/auth/me`
- View and edit profiles with profile and cover photo uploads
- Create posts with text, optional uploaded image, and privacy
- Feed includes own posts, public posts, and friends-only posts from friends
- Comments and reactions: LIKE, LOVE, CARE, HAHA, WOW, SAD, ANGRY
- Friend requests: send, cancel, accept, reject, unfriend, list friends
- Notifications for friend requests, accepted requests, reactions, and comments
- Search users by first name, last name, username, or email
- Responsive blue, white, and gray UI with original SocialConnect branding

## Backend Setup

Create the database manually if your MySQL user cannot create databases from JDBC:

```sql
CREATE DATABASE socialconnect_db;
```

The default JDBC URL also includes `createDatabaseIfNotExist=true`.

From the `backend` folder:

```bash
mvn spring-boot:run
```

Default backend URL:

```txt
http://localhost:8080
```

Important config in `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/socialconnect_db
spring.datasource.username=root
spring.datasource.password=
app.jwt.secret=change-this-secret-key-change-this-secret-key
app.jwt.expiration=86400000
app.upload.dir=uploads
```

## Frontend Setup

From the `frontend` folder:

```bash
npm install
npm run dev
```

Default frontend URL:

```txt
http://localhost:5173
```

## API Overview

Auth:
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`

Users:
- `GET /api/users/me`
- `PUT /api/users/me`
- `GET /api/users/{id}`
- `GET /api/users/search?query=`
- `POST /api/users/me/profile-picture`
- `POST /api/users/me/cover-photo`

Posts:
- `POST /api/posts`
- `GET /api/posts/feed`
- `GET /api/posts/user/{userId}`
- `PUT /api/posts/{postId}`
- `DELETE /api/posts/{postId}`
- `POST /api/posts/images`

Comments:
- `POST /api/posts/{postId}/comments`
- `GET /api/posts/{postId}/comments`
- `PUT /api/comments/{commentId}`
- `DELETE /api/comments/{commentId}`

Reactions:
- `POST /api/posts/{postId}/reactions`
- `DELETE /api/posts/{postId}/reactions`

Friends:
- `POST /api/friends/request/{receiverId}`
- `DELETE /api/friends/request/{receiverId}/cancel`
- `POST /api/friends/request/{requestId}/accept`
- `POST /api/friends/request/{requestId}/reject`
- `DELETE /api/friends/{friendId}`
- `GET /api/friends`
- `GET /api/friends/requests/received`
- `GET /api/friends/requests/sent`
- `GET /api/friends/status/{userId}`

Notifications:
- `GET /api/notifications`
- `PUT /api/notifications/{id}/read`
- `PUT /api/notifications/read-all`

## Sample Requests

Register:

```json
{
  "firstName": "Benedict",
  "lastName": "Castro",
  "username": "benedictcastro",
  "email": "benedict@example.com",
  "password": "secret123",
  "gender": "Male",
  "birthdate": "1998-05-20"
}
```

Create post:

```json
{
  "content": "Building SocialConnect today.",
  "imageUrl": null,
  "privacy": "PUBLIC"
}
```

## UI Guide

- Login/register pages are centered cards.
- Authenticated pages use a sticky top nav, left sidebar on desktop, main feed, and right utility sidebar.
- Profile pages show a full-width cover, avatar, friend action, profile details, and owner posts.
- Feed cards include author, timestamp, privacy, content, image, reaction picker, comments, and delete confirmation for owners.
# socialconnect
