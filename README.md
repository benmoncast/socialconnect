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
* JWT-based authentication
* Authenticated `/api/auth/me` endpoint
* Password encryption using BCrypt
* Protected backend routes
* Protected frontend routes
* Persistent login using localStorage token

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

Example configuration:

```properties
spring.application.name=socialconnect

server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/socialconnect_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

app.jwt.secret=change-this-secret-key-change-this-secret-key
app.jwt.expiration=86400000

app.upload.dir=uploads
```

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
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

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

---

## Security Features

* JWT authentication
* BCrypt password hashing
* Spring Security protected routes
* CORS configuration for frontend access
* Authorization checks for editing and deleting own posts/comments
* Duplicate email validation
* Duplicate username validation
* Friend request validation
* Role-ready user structure for future admin features

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

### JWT Unauthorized Error

Make sure the token is stored in localStorage and sent in the request header:

```txt
Authorization: Bearer your_token_here
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
