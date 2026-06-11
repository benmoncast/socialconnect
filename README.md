# SocialConnect

SocialConnect is an original full-stack social networking MVP inspired by common social media platform features. It allows users to create profiles, publish posts, upload images, react, comment, send friend requests, manage notifications, search users, and view a personalized feed.

This project does **not** use Facebook, Meta, or any real company branding, logos, copyrighted assets, or copied UI elements. It is built as an original portfolio and learning project.

---

## Tech Stack

### Backend

* Java 17
* Spring Boot 3
* Spring Security
* JWT Authentication
* Spring Data JPA
* Hibernate
* MySQL
* Maven

### Frontend

* React
* Vite
* TailwindCSS v4
* Axios
* React Router
* Context API

### Database

* MySQL

---

## Features

### Authentication

* User registration
* User login
* JWT-based authentication using HttpOnly cookies
* Authenticated `/api/auth/me` endpoint
* Password encryption using BCrypt
* Protected backend routes
* Protected frontend routes
* Persistent login using secure cookies and CSRF protection

### User Profile

* View own profile
* View other user profiles
* Edit profile information
* Upload profile picture
* Upload cover photo
* Display user posts on profile page

### Posts

* Create text posts
* Create posts with optional uploaded image
* Set post privacy:

  * `PUBLIC`
  * `FRIENDS`
  * `PRIVATE`
* Edit own posts
* Delete own posts
* View posts in personalized feed
* View posts by specific user

### Feed

The feed includes:

* Current user's own posts
* Public posts
* Friends-only posts from accepted friends
* Posts ordered by newest first

### Comments

* Add comments to posts
* Edit own comments
* Delete own comments
* View comments under each post

### Reactions

Supported reaction types:

* `LIKE`
* `LOVE`
* `CARE`
* `HAHA`
* `WOW`
* `SAD`
* `ANGRY`

Users can:

* React to posts
* Change reaction type
* Remove reaction
* View reaction summary

### Friends

* Send friend requests
* Cancel sent friend requests
* Accept friend requests
* Reject friend requests
* Unfriend users
* View friends list
* View received friend requests
* View sent friend requests
* Check friendship status

### Notifications

Notifications are created for:

* Friend requests
* Accepted friend requests
* Post reactions
* Post comments

Users can:

* View notifications
* Mark one notification as read
* Mark all notifications as read

### Search

Users can search by:

* First name
* Last name
* Username
* Email

### Responsive UI

* Original SocialConnect branding
* Blue, white, and gray theme
* Sticky top navigation
* Left sidebar on desktop
* Main feed layout
* Right utility sidebar
* Mobile-friendly responsive design

---

## Project Structure

```bash
socialconnect/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/socialconnect/
│   │   │   │       ├── config/
│   │   │   │       ├── controller/
│   │   │   │       ├── dto/
│   │   │   │       ├── entity/
│   │   │   │       ├── exception/
│   │   │   │       ├── repository/
│   │   │   │       ├── security/
│   │   │   │       ├── service/
│   │   │   │       └── SocialConnectApplication.java
│   │   │   │
│   │   │   └── resources/
│   │   │       └── application.properties
│   │
│   ├── uploads/
│   ├── pom.xml
│   └── README.md
│
├── frontend/
│   ├── src/
│   │   ├── api/
│   │   ├── assets/
│   │   ├── components/
│   │   ├── context/
│   │   ├── hooks/
│   │   ├── layouts/
│   │   ├── pages/
│   │   ├── routes/
│   │   ├── utils/
│   │   ├── App.jsx
│   │   └── main.jsx
│   │
│   ├── public/
│   ├── package.json
│   ├── vite.config.js
│   └── README.md
│
└── README.md
```

---

## Database Entities

The backend includes the following main entities:

### User

Stores account credentials and profile information.

### Post

Stores user-created posts, image URLs, privacy settings, and timestamps.

### Comment

Stores comments made by users on posts.

### Reaction

Stores post reactions made by users.

### FriendRequest

Stores friend request records and statuses between users.

### Notification

Stores user notifications for friend activity and post engagement.

---

## Backend Setup

### 1. Create MySQL Database

Create the database manually if your MySQL user cannot create databases from JDBC:

```sql
CREATE DATABASE socialconnect_db;
```

The default JDBC URL may also include:

```txt
createDatabaseIfNotExist=true
```

---

### 2. Configure Backend Properties

Open:

```bash
backend/src/main/resources/application.properties
```

Production configuration is intentionally strict and reads deployment values from
environment variables:

```properties
spring.application.name=socialconnect

server.port=${SERVER_PORT:8080}

spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

app.cors.allowed-origins=${CORS_ALLOWED_ORIGINS}
app.jwt.secret=${JWT_SECRET}
app.jwt.expiration=${JWT_EXPIRATION:3600000}

app.upload.dir=${UPLOAD_DIR:uploads}
```

For local development, `mvn spring-boot:run` activates the `dev` profile, which
provides local-only defaults from `application-dev.properties`. Do not deploy
with the `dev` profile.

---

### 3. Run the Backend

From the `backend` folder:

```bash
mvn spring-boot:run
```

Default backend URL:

```txt
http://localhost:8080
```

---

## Frontend Setup

### 1. Install Dependencies

From the `frontend` folder:

```bash
npm install
```

---

### 2. Configure API Base URL

Create a `.env` file inside the `frontend` folder:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

Example Axios configuration:

```javascript
import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api",
  withCredentials: true,
});

// See frontend/src/api/client.js for the full CSRF bootstrap flow.

export default api;
```

---

### 3. Run the Frontend

```bash
npm run dev
```

Default frontend URL:

```txt
http://localhost:5173
```

---

## API Overview

### Auth

| Method | Endpoint             | Description            |
| ------ | -------------------- | ---------------------- |
| POST   | `/api/auth/register` | Register a new user    |
| POST   | `/api/auth/login`    | Login user             |
| POST   | `/api/auth/logout`   | Clear auth cookie      |
| GET    | `/api/auth/csrf`     | Get CSRF token cookie  |
| GET    | `/api/auth/me`       | Get authenticated user |

---

### Users

| Method | Endpoint                        | Description                 |
| ------ | ------------------------------- | --------------------------- |
| GET    | `/api/users/me`                 | Get current user profile    |
| PUT    | `/api/users/me`                 | Update current user profile |
| GET    | `/api/users/{id}`               | Get user profile by ID      |
| GET    | `/api/users/search?query=`      | Search users                |
| POST   | `/api/users/me/profile-picture` | Upload profile picture      |
| POST   | `/api/users/me/cover-photo`     | Upload cover photo          |

---

### Posts

| Method | Endpoint                   | Description           |
| ------ | -------------------------- | --------------------- |
| POST   | `/api/posts`               | Create a post         |
| GET    | `/api/posts/feed`          | Get personalized feed |
| GET    | `/api/posts/user/{userId}` | Get posts by user     |
| PUT    | `/api/posts/{postId}`      | Update own post       |
| DELETE | `/api/posts/{postId}`      | Delete own post       |
| POST   | `/api/posts/images`        | Upload post image     |

---

### Comments

| Method | Endpoint                       | Description          |
| ------ | ------------------------------ | -------------------- |
| POST   | `/api/posts/{postId}/comments` | Add comment to post  |
| GET    | `/api/posts/{postId}/comments` | Get comments by post |
| PUT    | `/api/comments/{commentId}`    | Update own comment   |
| DELETE | `/api/comments/{commentId}`    | Delete own comment   |

---

### Reactions

| Method | Endpoint                        | Description            |
| ------ | ------------------------------- | ---------------------- |
| POST   | `/api/posts/{postId}/reactions` | Add or update reaction |
| DELETE | `/api/posts/{postId}/reactions` | Remove reaction        |

---

### Friends

| Method | Endpoint                                   | Description                  |
| ------ | ------------------------------------------ | ---------------------------- |
| POST   | `/api/friends/request/{receiverId}`        | Send friend request          |
| DELETE | `/api/friends/request/{receiverId}/cancel` | Cancel sent friend request   |
| POST   | `/api/friends/request/{requestId}/accept`  | Accept friend request        |
| POST   | `/api/friends/request/{requestId}/reject`  | Reject friend request        |
| DELETE | `/api/friends/{friendId}`                  | Unfriend user                |
| GET    | `/api/friends`                             | Get friends list             |
| GET    | `/api/friends/requests/received`           | Get received friend requests |
| GET    | `/api/friends/requests/sent`               | Get sent friend requests     |
| GET    | `/api/friends/status/{userId}`             | Get friendship status        |

---

### Notifications

| Method | Endpoint                       | Description                    |
| ------ | ------------------------------ | ------------------------------ |
| GET    | `/api/notifications`           | Get user notifications         |
| PUT    | `/api/notifications/{id}/read` | Mark notification as read      |
| PUT    | `/api/notifications/read-all`  | Mark all notifications as read |

---

## Sample Requests

### Register

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

---

### Login

```json
{
  "emailOrUsername": "benedictcastro",
  "password": "secret123"
}
```

---

### Create Post

```json
{
  "content": "Building SocialConnect today.",
  "imageUrl": null,
  "privacy": "PUBLIC"
}
```

---

### Add Reaction

```json
{
  "type": "LIKE"
}
```

---

### Add Comment

```json
{
  "content": "Great post!"
}
```

---

## UI Guide

### Login and Register Pages

* Centered card layout
* Simple form fields
* Error and success messages
* Link between login and register pages

### Authenticated Layout

Authenticated pages use:

* Sticky top navigation bar
* Left sidebar on desktop
* Main content feed
* Right utility sidebar
* Responsive mobile layout

### Feed Page

Feed cards include:

* Author avatar
* Author name
* Username
* Timestamp
* Privacy label
* Post content
* Optional post image
* Reaction picker
* Comment section
* Delete confirmation for post owners

### Profile Page

Profile pages include:

* Full-width cover photo
* User avatar
* Full name
* Username
* Bio
* Location
* Friend action button
* Profile details
* Owner posts

### Friends Page

The friends page includes:

* Friends list
* Received friend requests
* Sent friend requests
* Friend status actions

### Notifications Page

The notifications page includes:

* Notification list
* Read/unread state
* Mark as read action
* Mark all as read action

---

## File Uploads

Uploaded files are stored locally inside:

```bash
backend/uploads
```

Supported file types:

* JPG
* JPEG
* PNG
* WEBP

Upload features include:

* Profile picture upload
* Cover photo upload
* Post image upload
* MIME type and file signature validation
* JPEG and PNG re-encoding to strip embedded metadata

---

## Security Features

* JWT authentication using HttpOnly cookies
* CSRF protection for cookie-authenticated writes
* Login/register rate limiting and failed-login cooldown
* BCrypt password hashing
* Spring Security protected routes
* HTTPS enforcement in production profile
* Reverse proxy header support
* Health endpoint support through Spring Boot Actuator
* CORS configuration for frontend access
* Authorization checks for editing and deleting own posts/comments
* Authorization checks for friend request and notification ownership
* Duplicate email validation
* Duplicate username validation
* Friend request validation
* Role-ready user structure for future admin features

### Production Security Notes

* Production requires real environment values for database, CORS, and JWT settings.
* The local Maven run uses the `dev` profile; do not deploy with that profile.
* Browser authentication uses an HttpOnly JWT cookie and an `X-XSRF-TOKEN` header.
* The current abuse limiter is in-memory. Use Redis, a gateway, or a WAF for multi-instance deployments.
* Local upload storage is acceptable for development. Use object storage and malware scanning for public production.
* CI runs npm audit and backend dependency checks, and Dependabot is configured for Maven, npm, and GitHub Actions.

### Vercel And Railway Deployment

Frontend Vercel environment:

```env
VITE_API_BASE_URL=https://replace-with-your-railway-backend-domain.up.railway.app/api
VITE_PUBLIC_APP_HOSTS=socialconnect-green.vercel.app
```

Backend Railway environment:

```env
CORS_ALLOWED_ORIGINS=https://socialconnect-green.vercel.app
JWT_SECRET=replace_with_at_least_32_random_characters
JWT_EXPIRATION=3600000
REQUIRE_HTTPS=true
AUTH_COOKIE_SECURE=true
AUTH_COOKIE_SAME_SITE=None
UPLOAD_DIR=uploads
```

For the database, either set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`, or let
the backend use Railway's MySQL variables:

```env
MYSQLHOST=mysql.railway.internal
MYSQLPORT=3306
MYSQLDATABASE=railway
MYSQLUSER=replace_with_railway_mysql_user
MYSQLPASSWORD=replace_with_railway_mysql_password
```

Do not paste Railway's raw `mysql://...` URL into `DB_URL`. Spring Boot expects
a JDBC URL such as `jdbc:mysql://host:port/database?...`.

If the frontend and backend are deployed on different sites, `AUTH_COOKIE_SAME_SITE=None`
and `AUTH_COOKIE_SECURE=true` are required. For the most reliable cookie behavior,
use a custom domain later, such as `app.example.com` and `api.example.com`.

---

## Validation Rules

### Backend Validation

* Required fields must not be empty
* Email must be valid
* Username must be unique
* Email must be unique
* Password must be encrypted before saving
* Users cannot send friend requests to themselves
* Users cannot send duplicate friend requests
* Users cannot edit or delete posts they do not own
* Users cannot edit or delete comments they do not own

### Frontend Validation

* Required fields checked before submit
* Password field validation
* Error messages shown from API responses
* Loading states while submitting forms
* Confirmation dialog before delete actions

---

## Development Commands

### Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The Maven run goal uses the local `dev` profile. Packaged production runs still
require real environment values for database, CORS, and JWT settings.

### Frontend

```bash
cd frontend
npm install
npm run dev
npm run build
```

---

## Common Issues

### MySQL Connection Error

Make sure MySQL is running and your database credentials are correct in:

```bash
backend/src/main/resources/application.properties
```

---

### CORS Error

Make sure your backend allows requests from:

```txt
http://localhost:5173
```

---

### Authentication Error

Make sure the frontend sends credentials and first bootstraps CSRF through:

```txt
GET /api/auth/csrf
```

---

### Uploaded Images Not Showing

Make sure the backend exposes the uploads folder through static resource mapping.

Example uploaded file URL:

```txt
http://localhost:8080/uploads/example-image.png
```

---

## Sample User Flow

1. User creates an account.
2. User logs in.
3. User updates profile details.
4. User uploads a profile picture and cover photo.
5. User creates a post.
6. Other users react and comment.
7. User sends a friend request.
8. Another user accepts or rejects the request.
9. User views personalized feed.
10. User receives notifications for social activity.

---

## Future Improvements

Possible future enhancements:

* Real-time chat
* WebSocket notifications
* Stories feature
* Groups
* Pages
* Admin dashboard
* Report post/user feature
* Save/bookmark posts
* Infinite scroll feed
* Dark mode
* Email verification
* Forgot password/reset password
* Cloud image storage using AWS S3 or Cloudinary
* Docker support
* Unit and integration tests
* Deployment guide
* CI/CD pipeline

---

## Screenshots

Add screenshots here after running the project.

Example:

```markdown
### Login Page
![Login Page](screenshots/login.png)

### News Feed
![News Feed](screenshots/feed.png)

### Profile Page
![Profile Page](screenshots/profile.png)
```

---

## License

This project is for educational and portfolio purposes.

You may modify, improve, and use this project as a learning reference.

---

## Disclaimer

SocialConnect is an original social networking application created for learning and portfolio development.

It is not affiliated with, endorsed by, or connected to Facebook, Meta, or any other social media company.

No copyrighted branding, logos, names, or proprietary assets are used in this project.
