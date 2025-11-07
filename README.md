# Spring Boot Social Media API

> A comprehensive, feature-rich REST API for a modern social media platform, built with Spring Boot, Spring Security, and MySQL. This portfolio-ready project implements a secure, scalable backend with full JWT authentication.

---

## 🚀 Core Features

* **🔐 Secure Auth:** Full JWT (Login/Register) & Endpoint Protection.
* **👥 User Management:** Follow/Unfollow, Search (Paginated), Edit Profile.
* **📰 Posts:** Full CRUD with Pagination & Search.
* **❤️ Interactions:** Like/Unlike Posts & Comments.
* **🎬 Reels:** Short video sharing with Pagination.
* **📸 Stories:** Ephemeral 24-hour stories with auto-deletion (`@Scheduled`).
* **💬 Real-time Chat:** 1-on-1 and Group Chat creation.
* **✉️ Messaging:** Sending text and image messages.
* **... and more!**

---

## 🛠️ Tech Stack

* **Language:** Java 25
* **Framework:** Spring Boot 3
* **Security:** Spring Security 6
* **Data:** Spring Data JPA (Hibernate)
* **Database:** MySQL
* **Authentication:** JWT (JSON Web Tokens)
* **API:** RESTful API

---

## 🚀 API Documentation & Testing

The complete API documentation, including all endpoints and request/response examples, is available via the Postman collection.

Click the button below to import the collection directly into your Postman app:

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/48854188-b4af322c-846f-4120-bf91-3c1f54871df4)

---

## ⚙️ How to Run Locally

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/faddboyy/spring-boot-java-social-api.git](https://github.com/faddboyy/spring-boot-java-social-api.git)
    cd spring-boot-java-social-api
    ```
2.  **Create your database:**
    * Create a MySQL database named `socialapp`.
3.  **Setup local environment:**
    * Create an `application-local.properties` file in `src/main/resources/`.
    * Add your database password and JWT secret key:
    ```properties
    spring.datasource.password=your_db_password
    app.jwt.secret-key=your_very_long_and_secret_jwt_key
    ```
4.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
5.  The application will be available at `http://localhost:8081`.

---
