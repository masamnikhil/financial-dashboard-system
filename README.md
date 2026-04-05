# 💰 Finance Dashboard System

A Spring Boot REST API for tracking income and expenses.
It allows users to register, authenticate, manage financial records, search/filter transactions, and view dashboard analytics such as monthly trends based on their roles.

The API is secured using JWT authentication and documented using Springdoc OpenAPI with interactive testing in Swagger UI.

## 🚀 Features
- 🔐 JWT Authentication
- 👤 User profile management
- 💰 Income & Expense tracking
- 🔎 Search & filtering support
- 📄 Pagination
- 📊 dashboard overview
- 🗑️ Soft delete support
- 📘 Swagger API documentation

## 🛠️ Tech Stack
| ### Technology        | ### Purpose                          |
| ----------------- | -------------------------------- |
| Java 25           | Programming language             |
| Spring Boot       | Backend framework                |
| Spring Security   | Authentication and authorization |
| JWT               | Token-based authentication       |
| Spring Data JPA   | Database ORM                     |
| MySQL             | Relational database              |
| Springdoc OpenAPI | API documentation                |
| Maven             | Dependency management            |

src/main/java/com/system/finance_dashboard
├── config            → configuration
├── controller        → REST Controllers
├── dto               → Data transfer objects
├── entity            → JPA entities
├── exception         → Global exception handling
├── jpaspecification  → Dynamic filtering queries
├── repository        → Database repositories
├── security          → JWT and security components
├── service           → interface for operations
├── serviceimpl       → Business logic
└── util              → utility classes

# Setup Instructions
## 1. Clone the repository
``` text git clone  https://github.com/masamnikhil/financial-dashboard-system.git
cd finance-dashboard
```

## 2. Configure database
Example application.properties

spring:
  application:
    name: finance-dashboard

  datasource:
    url: jdbc:mysql://localhost:3306/${DB_NAME}?createDatabaseIfNotExist=true
    username: ${DB_USER}
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update

jwt:
  secret: ${JWT_SECRET}
  token-expiration: ${JWT_ACCESS_EXPIRATION}

  ### Note : Before running the application, create a `.env` file based on the provided `.env.example` file and configure the required environment variables.






