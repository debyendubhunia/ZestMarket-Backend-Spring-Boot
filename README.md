# ZestMarket-Backend-Spring-Boot-
ZestMarket is an enterprise e-commerce microservices platform backend built with Java 21, Spring Boot 3.4, Spring Cloud Gateway, Eureka Server, MySQL, Redis, Kafka, and Razorpay. Features JWT authentication, product search with specifications, shopping cart, order tracking, admin analytics, and aggregated Swagger OpenAPI documentation.

# ZestMarket - Enterprise Microservices Platform Backend

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![Java](https://img.shields.io/badge/Java-21%2B-orange.svg)](https://www.oracle.com/java/)
[![Swagger API Docs](https://img.shields.io/badge/Swagger%20UI-OpenAPI%203.1-green.svg)](http://localhost:8080/zestmarket-api-doc)
[![Repository](https://img.shields.io/badge/GitHub-Repository-181717?logo=github)](https://github.com/debyendubhunia/ZestMarket-Backend-Spring-Boot.git)

**Repository URL**: `https://github.com/debyendubhunia/ZestMarket-Backend-Spring-Boot.git`

ZestMarket is an enterprise-grade e-commerce microservices platform built using **Java 21/26**, **Spring Boot 3.4.3**, **Spring Cloud Gateway**, **Eureka Discovery Server**, **MySQL**, **Redis**, **Kafka**, and **Razorpay Sandbox Integration**.

---

## 🚀 Interactive API Documentation (Swagger UI)

Access the aggregated OpenAPI 3.1 Swagger UI for all microservices at:

👉 **[http://localhost:8080/zestmarket-api-doc](http://localhost:8080/zestmarket-api-doc)**

---

## 🏛️ Microservice Directory & Port Mapping

| Service Name | Port | Base Path | Description |
|---|---|---|---|
| **API Gateway** | `8080` | `/` | Central Entry Point, Global CORS, JWT Forwarding & Swagger Aggregator |
| **Eureka Discovery Server** | `8761` | `/` | Service Registry & Discovery |
| **Config Server** | `8888` | `/` | Centralized Configuration Management |
| **Auth Service** | `8081` | `/api/v1/auth` | User Registration, Authentication & JWT Management |
| **User Service** | `8082` | `/api/v1/users` | Profile Management & Admin User Control |
| **Product Service** | `8083` | `/api/v1/products`, `/api/v1/categories` | Product Catalog, Specifications, Image Upload & Redis Caching |
| **Cart Service** | `8084` | `/api/v1/cart`, `/api/v1/wishlist`, `/api/v1/coupons` | Cart Items, Wishlist & Discount Coupon Calculation |
| **Order Service** | `8085` | `/api/v1/orders` | Order Processing, Status Lifecycle & Admin Analytics |
| **Payment Service** | `8086` | `/api/v1/payments` | Razorpay Order Creation & Payment Verification |
| **Notification Service**| `8087` | `/api/v1/notifications` | Asynchronous Email & Kafka Event Listener |

---

## 📌 Complete API Endpoint Reference

### 🔐 1. Auth Service (`/api/v1/auth`)

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/api/v1/auth/register` | Register a new user account | No |
| `POST` | `/api/v1/auth/login` | User login with email and password | No |
| `POST` | `/api/v1/auth/refresh` | Refresh expired access token using refresh token | No |

---

### 👤 2. User Service (`/api/v1/users`)

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/v1/users/me` | Get logged-in user profile details | Yes (`X-User-Id`) |
| `GET` | `/api/v1/users/{id}` | Get user details by User ID | Yes |
| `PUT` | `/api/v1/users/profile` | Update current user profile | Yes (`X-User-Id`) |
| `GET` | `/api/v1/users/admin/all` | **[Admin]** List all registered users | Admin |
| `PUT` | `/api/v1/users/admin/{id}/status` | **[Admin]** Enable or disable user account | Admin |

---

### 🛍️ 3. Product Service (`/api/v1/products` & `/api/v1/categories`)

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/v1/products` | Dynamic search (search, categoryId, minPrice, maxPrice, page, size, sortBy, sortDir) | No |
| `GET` | `/api/v1/products/{id}` | Get product details by ID (Redis cached) | No |
| `POST` | `/api/v1/products` | **[Admin]** Create new product | Admin |
| `PUT` | `/api/v1/products/{id}` | **[Admin]** Update existing product | Admin |
| `DELETE` | `/api/v1/products/{id}` | **[Admin]** Delete product | Admin |
| `POST` | `/api/v1/products/{id}/images` | **[Admin]** Upload product image file | Admin |
| `GET` | `/api/v1/categories` | List all product categories | No |
| `POST` | `/api/v1/categories` | **[Admin]** Create new category | Admin |

---

### 🛒 4. Cart, Wishlist & Coupon Service (`/api/v1/cart`, `/api/v1/wishlist`, `/api/v1/coupons`)

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/v1/cart` | Get shopping cart for user | Yes (`X-User-Id`) |
| `POST` | `/api/v1/cart/items` | Add product item to shopping cart | Yes (`X-User-Id`) |
| `DELETE` | `/api/v1/cart/items/{productId}` | Remove item from shopping cart | Yes (`X-User-Id`) |
| `DELETE` | `/api/v1/cart/clear` | Clear all items from shopping cart | Yes (`X-User-Id`) |
| `GET` | `/api/v1/wishlist` | Get list of user wishlist product IDs | Yes (`X-User-Id`) |
| `POST` | `/api/v1/wishlist/{productId}` | Add product to user wishlist | Yes (`X-User-Id`) |
| `DELETE` | `/api/v1/wishlist/{productId}` | Remove product from user wishlist | Yes (`X-User-Id`) |
| `POST` | `/api/v1/coupons` | **[Admin]** Create discount coupon code | Admin |
| `POST` | `/api/v1/coupons/apply` | Apply coupon code and calculate discount | Yes |

---

### 📦 5. Order Service (`/api/v1/orders`)

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/api/v1/orders` | Place a new customer order | Yes (`X-User-Id`) |
| `GET` | `/api/v1/orders/{id}` | Get order details by Order ID | Yes |
| `GET` | `/api/v1/orders/my-orders` | Get all orders placed by logged-in user | Yes (`X-User-Id`) |
| `PUT` | `/api/v1/orders/{id}/status` | **[Admin]** Update order status | Admin |
| `GET` | `/api/v1/orders/admin/dashboard/stats` | **[Admin]** Dashboard sales stats & analytics | Admin |

---

### 💳 6. Payment Service (`/api/v1/payments`)

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/api/v1/payments/create-order` | Create Razorpay payment order | Yes |
| `POST` | `/api/v1/payments/verify` | Verify Razorpay payment signature & update order status | Yes |

---

### 🔔 7. Notification Service (`/api/v1/notifications`)

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/v1/notifications/health` | Check Kafka consumer notification service health status | No |

---

## 🛠️ How to Run Locally

### Option A: Running Individual Microservices via Maven Wrapper

From the project root directory, run any microservice using:

```powershell
# 1. Start Eureka Discovery Server (Port 8761)
.\.maven\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run -pl eureka-server

# 2. Start API Gateway (Port 8080)
.\.maven\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run -pl api-gateway

# 3. Start Core Services
.\.maven\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run -pl auth-service
.\.maven\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run -pl user-service
.\.maven\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run -pl product-service
.\.maven\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run -pl cart-service
.\.maven\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run -pl order-service
.\.maven\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run -pl payment-service
.\.maven\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run -pl notification-service
```

### Option B: Running Entire Infrastructure via Docker Compose

```bash
docker-compose up -d
```

---

## 📤 Git Commit & Push Guide

To push all changes and documentation to your GitHub repository:

```bash
# Initialize git if not already initialized
git init

# Add remote repository (if not already added)
git remote add origin https://github.com/debyendubhunia/ZestMarket-Backend-Spring-Boot.git

# Stage all files, commit and push
git add .
git commit -m "feat: Add comprehensive README documentation and custom API doc route"
git branch -M main
git push -u origin main
```

