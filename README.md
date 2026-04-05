# 💰 Finance Dashboard System

A Spring Boot REST API for tracking income and expenses.
It allows users to register, authenticate, manage financial records, search/filter transactions, and view dashboard analytics such as monthly trends based on their roles.

The API is secured using JWT authentication and documented using Springdoc OpenAPI with interactive testing in Swagger UI.

## 🚀 Features
- 🧑‍💻 **User Registration & Login** (JWT Authentication) 
- 🔑 **Role Based Authorization** (USER / ADMIN / ANALYST)
- ⚠️ **Global Exception Handling**
- ✅ **Validation & Error Responses** 
- 🔍 **Dynamic Filtering** (Date, Category, Type)  
- 💰 **Income & Expense tracking**
- 🔎 **Search support**
- 📄 **Pagination**
- 📊 **Dashboard Summary** (Total Income, Expense, Balance)
- 🗑️ **Soft delete support**
- ⚙️ **API Testing with Postman**
- 📘 **Swagger API documentation**

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

## Project Structure
```
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
```

## 👑 Admin Pre-Seeding
On application startup, a default Admin user is automatically created if it does not already exist.
This allows immediate access to admin features without manual database insertion.
### Default Admin Credentials
```
Username: admin
Password: Admin@123
Role: ADMIN
```
### ⚠️ It is recommended to change the password after first login.

### How it works
- On startup, the application checks if an admin exists
- If not found, it creates one
- Password is encoded using BCrypt
- Role is assigned as ADMIN

# Setup Instructions
## 1. Clone the repository
```
git clone  https://github.com/masamnikhil/financial-dashboard-system.git
cd finance-dashboard
```

## 2. Configure database
Example application.properties
```
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
```

## 📦 Environment Setup
1. create a .env file and copy the contents from .env.example.
2. Update the values in .env with your local configuration.
3. Run the application.

### ⚠️ It is Mandatory configiure Environment Variables to the application.

## 3. Run the application 
```
mvn spring-boot:run or ./mvnw spring-boot:run (if maven not installed in your system)
```
### Application runs on:
```
http://localhost:8080
```

## 🔌 Complete API Endpoints Reference

### BASE URL
```
http://localhost:8080
```
## 👥 USER MANAGEMENT ENDPOINTS

### Admin and User Login
```
POST /api/users/login

request body :
{
  "username": "admin",
  "password": "Admin@123"
}
```
- Response:
```
{
  "accessToken": "your unique access token"
}
```
    
### Note : Admin has to first log in to register users and then user logs in.

### User Registration (Admin Only)
```
POST /api/users/register
Header : Authorization: Bearer <JWT Token>,
Body :
  {
    "username" : "analyst",
    "name" : "Analyst",
    "email" : "analyst@gmail.com",
    "role" : "ANALYST"
  }
```
- Response (Success - 201 ✅):
```
{
  "username": "nikhil",
  "caution": "Create new Password",
  "Temporary password": "kcJz*h5"
}
```
### ⚠️ User is forced to change the password

### Update User Status (Admin Only)
```
PATCH /api/users/{id}/status
Header : Authorization: Bearer <JWT Token>
Body:
  {
    "status" : "INACTIVE" or "ACTIVE"
  }
```
- Response (Success - 200 ✅):
```
Status successfully updated to (ACTIVE or INACTIVE)
```

### Update User Role (Admin only)
```
PATCH /api/users/{id}/role
Header : Authorization: Bearer <JWT Token>
Body:
  {
    "role": "ANALYST" | "VIEWER" | "ADMIN"
  }
```
- Response (Success - 200 ✅):
```
Role successfully updated to ADMIN | VIEWER | ADMIN
```

### change Password (Admin Only)
```
PATCH /api/users/change-password
Header : Authorization: Bearer <JWT Token>
Body:
  {
    "old_password" : "tcCndZn",
    "new_password" : "Analyst@123"
  }
```
- Response (Success - 200 ✅):
```
Password changed successfully, log in again
```
 
### get Users (Admin Only)
```
Get /api/users
Header : Authorization: Bearer <JWT Token>
```
- Response (Success - 200 ✅):
```
[
  {
    "id": 1,
    "name": "Admin",
    "email": "admin@gmail.com",
    "username": "admin",
    "role": "ADMIN",
    "status": "ACTIVE"
  },
  {
    "id": 3,
    "name": "Analyst",
    "email": "analyst@gmail.com",
    "username": "analyst",
    "role": "ADMIN",
    "status": "ACTIVE"
  }]
```

### get user by id (Admin Only)
```
GET /api/users/{id}
Header : Authorization: Bearer <JWT Token>
```
- Response (Success - 200 ✅):
```
     {
      "id": 1,
      "name": "Admin",
      "email": "admin@gmail.com",
      "username": "admin",
      "role": "ADMIN",
      "status": "ACTIVE"
     }
```

## 💰 FINANCIAL RECORDS ENDPOINTS

### Create Record (Admin Only)
```
POST /api/records
Header : Authorization: Bearer <JWT Token>
Body: {
      "amount": 2000,
      "type": "EXPENSE",
      "category": "Investment",
      "notes": "invested"
    }
```
- Response (Success - 201 ✅):
```
   {
  "id": 33,
  "amount": 2000,
  "type": "EXPENSE",
  "category": "Investment",
  "notes": "invested",
  "createdBy": "admin",
  "createdAt": "2026-04-05, 14:36"
 }
```

### filter Records (Admin and Analyst Only)
```
GET /api/records?type=EXPENSE&category=catering&date=2026-04-11&page=0&size=10
Header : Authorization: Bearer <JWT Token>
```
- Response (Success - 200 ✅):
```
{
  "content": [
    {
      "id": 11,
      "amount": 700,
      "type": "EXPENSE",
      "category": "Catering",
      "notes": "Lunch for meeting",
      "createdBy": "organization_admin",
      "createdAt": "2026-04-11, 13:00"
    },
    {
      "id": 21,
      "amount": 700,
      "type": "EXPENSE",
      "category": "Catering",
      "notes": "Lunch for meeting",
      "createdBy": "organization_admin",
      "createdAt": "2026-04-11, 13:00"
    }
  ], "empty": false,
  "first": true,
  "last": true,
  "number": 0,
  "numberOfElements": 1,
  "pageable": {
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 10,
    "paged": true,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "unpaged": false
  },
  "size": 10,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "totalElements": 1,
  "totalPages": 1
}
```
### Note: filter records based on category, date, date range, type, to

### Get Record by record id (Admin and Analyst Only)
```
GET  /api/records/{id}
Header : Authorization: Bearer <JWT Token>
```
- Response (Success - 200 ✅):
```
  {
    "id": 5,
    "amount": 3200,
    "type": "EXPENSE",
    "category": "Salaries",
    "notes": "Staff salaries for April",
    "createdBy": "organization_admin",
    "createdAt": "2026-04-05, 16:45"
  }
```

### Search Records (Admin and Analyst Only)
```
GET /api/records/search?keyword=expense&page=0&size=10
Header : Authorization: Bearer <JWT Token>
```
- Response (Success - 200 ✅):
```
{
  "content": [
    {
      "id": 9,
      "amount": 1100.75,
      "type": "EXPENSE",
      "category": "Travel",
      "notes": "Travel expenses for meetings",
      "createdBy": "organization_admin",
      "createdAt": "2026-04-09, 09:50"
    }
  ],
  "empty": false,
  "first": true,
  "last": true,
  "number": 0,
  "numberOfElements": 1,
  "pageable": {
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 10,
    "paged": true,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "unpaged": false
  },
  "size": 10,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "totalElements": 1,
  "totalPages": 1
}
```
### Note: search records based on category, date, type

### Update record (Admin Only)
```
PATCH /api/records/{id}
Header : Authorization: Bearer <JWT Token>
Body :
    {
    "amount": 1500,
    "type": "INCOME",
    "notes": "some notes",
    "category": "Food"
  }
```
- Response (Success - 200 ✅):
```
{
  "id": 6,
  "amount": 1500,
  "type": "INCOME",
  "category": "Food",
  "notes": "some notes",
  "createdBy": "organization_admin",
  "createdAt": "2026-04-06, 12:00"
}
```

### Soft Delete Record (Admin Only)
```
DELETE /api/records/{id}
Header : Authorization: Bearer <JWT Token>
```
- Response (Success - 204 ✅): Not Content

## 📊 DASHBOARD ENDPOINTS

### Summary 
```
GET /api/dashboard/summary
Header : Authorization: Bearer <JWT Token>
```
- Response (Success - 200 ✅):
```
{
  "totalIncome": 58600,
  "totalExpense": 21582.25,
  "netBalance": 37017.75
}
```

### Monthly Trends
```
GET /api/dashboard/trends?year=2026
Header : Authorization: Bearer <JWT Token>
```
- Response (Success - 200 ✅):
```
[
  {
    "month": "APRIL 2026",
    "expenses": 21582.25,
    "income": 58600,
    "netAmount": 37017.75
  }
]
```

### Category Totals 
```
GET /api/dashboard/by-category
Header : Authorization: Bearer <JWT Token>
```
- Response (Success - 200 ✅):
```
[
  {
    "category": "Donations",
    "total": 7500
  },
  {
    "category": "Utilities",
    "total": 2000
  },
  {
    "category": "Salaries",
    "total": 3200
  },
  {
    "category": "Marketing",
    "total": 980
  },
  {
    "category": "Grants",
    "total": 2500
  },
  {
    "category": "Travel",
    "total": 1100.75
  }]
```

### Recent Activity
```
GET /api/dashboard/recent
Header : Authorization: Bearer <JWT Token>
```
- Response (Success - 200 ✅):
```
[
  {
    "amount": 4800,
    "type": "INCOME",
    "date": "2026-04-20, 12:15",
    "notes": "Consulting revenue",
    "category": "Consulting Services"
  },
  {
    "amount": 4800,
    "type": "INCOME",
    "date": "2026-04-20, 12:15",
    "notes": "Consulting revenue",
    "category": "Consulting Services"
  },
  {
    "amount": 900,
    "type": "EXPENSE",
    "date": "2026-04-19, 10:30",
    "notes": "Office maintenance",
    "category": "Maintenance"
  },
  {
    "amount": 900,
    "type": "EXPENSE",
    "date": "2026-04-19, 10:30",
    "notes": "Office maintenance",
    "category": "Maintenance"
  },
  {
    "amount": 2750,
    "type": "INCOME",
    "date": "2026-04-18, 16:00",
    "notes": "Website donations",
    "category": "Online Donations"
  }
]
```

## ⚠️ Common Error Responses

| Status Code | Symbol | Message                 | Description                        |
| ----------- | ------ | ----------------------- | ---------------------------------- |
| 400         | ❌     | `Bad Request`          | Validation failed or invalid input |
| 401         | 🔒     | `Unauthorized`          | No token or invalid token          |
| 403         | 🚫     | `Forbidden`             | User does not have permission      |
| 404         | ❌      | `Not Found`            | Resource not found                 |
| 500         | 💥     | `Internal Server Error` | Something went wrong on the server |

## 📌 Assumptions
- 👤 Users must register before accessing protected APIs
- 🔐 Authentication is handled using JWT tokens
- 🛡️ All endpoints (except login) require authentication
- 👑 Admin-only endpoints are accessible only to users with ADMIN role
- 🗄️ Soft delete is implemented for financial records (data is not permanently removed)
- 📅 createdAt timestamp is automatically generated by the system
- 💰 Amount is always stored as positive value (type determines income/expense)
- 📊 Dashboard calculations are based only on non-deleted records
- ⚙️ Environment variables are used for sensitive configuration (DB, JWT secret)
- 📘 API documentation is available via Swagger/OpenAPI
- 🧪 Validation errors return structured error responses
- 👑 Admin user is pre-seeded for testing purpose
- 🔐 Authorization is enforced using Spring Security `@PreAuthorize`
- 👑 Admin operations are protected with `@PreAuthorize("hasRole('ADMIN')")`

## ✅ Conclusion
This project demonstrates a secure and scalable RESTful backend for managing financial records. It includes JWT-based authentication, role-based authorization, filtering, dashboard summaries, and global exception handling. The application follows clean architecture principles and is designed to be easily extendable for future enhancements.

## 🙌 Acknowledgements
This project was developed as part of a backend assignment to demonstrate REST API design, Spring Boot best practices, and secure role-based access control.






  






  



  














