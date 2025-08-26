# CineFund - Complete API Documentation

## Project Overview

**CineFund** is a microservices-based movie investment platform that allows investors to fund movie projects and receive returns based on movie performance. The system consists of multiple services with a centralized API Gateway for routing.

### Architecture
- **API Gateway**: Port 8090 (Routes requests to microservices)
- **User Service**: Port 8084 (User authentication and management)
- **Movie Service**: Port 8082 (Movie catalog and management)
- **Funding Service**: Port 8083 (Investment and return processing)

### Technology Stack
- **Backend**: Spring Boot, Java 17+
- **Database**: PostgreSQL
- **API Gateway**: Spring Cloud Gateway
- **Documentation**: OpenAPI/Swagger

---

## 1. User Service API (Port 8084)

### Base URL: `http://localhost:8090/api/users` (via API Gateway)

#### 1.1 User Registration
**Endpoint**: `POST /api/users/register`

**Description**: Register a new user in the system

**Request Body**:
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1234567890"
}
```

**Response (Success - 201)**:
```json
{
  "success": true,
  "message": "User registered successfully",
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+1234567890",
    "createdAt": "2024-01-20T10:30:00",
    "status": "ACTIVE"
  }
}
```

**Response (Error - 400)**:
```json
{
  "success": false,
  "message": "Username already exists",
  "errors": ["Username 'john_doe' is already taken"]
}
```

#### 1.2 User Login
**Endpoint**: `POST /api/users/login`

**Description**: Authenticate user and return access token

**Request Body**:
```json
{
  "username": "john_doe",
  "password": "securePassword123"
}
```

**Response (Success - 200)**:
```json
{
  "success": true,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe"
  }
}
```

#### 1.3 Get User Profile
**Endpoint**: `GET /api/users/{userId}`

**Description**: Retrieve user profile information

**Path Parameters**:
- `userId` (Long): User ID

**Response (Success - 200)**:
```json
{
  "success": true,
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+1234567890",
    "createdAt": "2024-01-20T10:30:00",
    "status": "ACTIVE"
  }
}
```

---

## 2. Movie Service API (Port 8082)

### Base URL: `http://localhost:8090/api/movies` (via API Gateway)

#### 2.1 Create Movie
**Endpoint**: `POST /api/movies`

**Description**: Create a new movie project for investment

**Request Body**:
```json
{
  "title": "The Great Adventure",
  "description": "An epic adventure movie",
  "storyline": "A thrilling adventure across unknown territories",
  "genre": "Adventure",
  "directorName": "John Director",
  "producerId": 201,
  "producerName": "ABC Productions",
  "cast": "John Doe, Jane Smith, Mike Johnson",
  "budget": 1000000.00,
  "expectedReturnPercentage": 25.0,
  "releaseDate": "2024-12-25",
  "productionStartDate": "2024-06-01",
  "fundingDeadline": "2024-05-31",
  "status": "FUNDING",
  "posterUrl": "https://example.com/poster.jpg",
  "trailerUrl": "https://example.com/trailer.mp4"
}
```

**Response (Success - 201)**:
```json
{
  "success": true,
  "message": "Movie created successfully",
  "movie": {
    "id": 301,
    "title": "The Great Adventure",
    "description": "An epic adventure movie",
    "storyline": "A thrilling adventure across unknown territories",
    "budget": 1000000.00,
    "raisedAmount": 0,
    "expectedReturnPercentage": 25.0,
    "producerId": 201,
    "producerName": "ABC Productions",
    "directorName": "John Director",
    "cast": "John Doe, Jane Smith, Mike Johnson",
    "genre": "Adventure",
    "releaseDate": "2024-12-25",
    "productionStartDate": "2024-06-01",
    "fundingDeadline": "2024-05-31",
    "status": "FUNDING",
    "posterUrl": "https://example.com/poster.jpg",
    "trailerUrl": "https://example.com/trailer.mp4",
    "isActive": true,
    "createdAt": "2024-01-20T11:00:00",
    "updatedAt": "2024-01-20T11:00:00",
    "fundingProgress": 0.0,
    "fundingComplete": false
  }
}
```

#### 2.2 Get All Movies
**Endpoint**: `GET /api/movies`

**Description**: Retrieve all movies with optional filtering

**Query Parameters**:
- `status` (String, optional): Filter by movie status (FUNDING, PRODUCTION, RELEASED)
- `genre` (String, optional): Filter by genre
- `page` (Integer, optional): Page number (default: 0)
- `size` (Integer, optional): Page size (default: 10)

**Response (Success - 200)**:
```json
{
  "success": true,
  "movies": [
    {
      "id": 301,
      "title": "The Great Adventure",
      "description": "An epic adventure movie",
      "genre": "Adventure",
      "director": "John Director",
      "producerId": 201,
      "budget": 1000000.00,
      "expectedRevenue": 2500000.00,
      "releaseDate": "2024-12-25",
      "status": "FUNDING",
      "fundingProgress": 45.5,
      "totalInvestments": 455000.00,
      "raisedAmount": 455000.00,
      "expectedReturnPercentage": 25.0,
      "producerName": "ABC Productions",
      "directorName": "John Director",
      "cast": "John Doe, Jane Smith",
      "storyline": "An epic adventure story",
      "productionStartDate": "2024-06-01",
      "fundingDeadline": "2024-05-31",
      "posterUrl": "https://example.com/poster.jpg",
      "trailerUrl": "https://example.com/trailer.mp4",
      "isActive": true,
      "fundingComplete": false
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "currentPage": 0
}
```

#### 2.3 Get Movie by ID
**Endpoint**: `GET /api/movies/{movieId}`

**Description**: Retrieve detailed information about a specific movie

**Path Parameters**:
- `movieId` (Long): Movie ID

**Response (Success - 200)**:
```json
{
  "success": true,
  "movie": {
    "id": 301,
    "title": "The Great Adventure",
    "description": "An epic adventure movie",
    "genre": "Adventure",
    "director": "John Director",
    "producerId": 201,
    "budget": 1000000.00,
    "expectedRevenue": 2500000.00,
    "releaseDate": "2024-12-25",
    "status": "FUNDING",
    "fundingProgress": 45.5,
    "totalInvestments": 455000.00,
    "raisedAmount": 455000.00,
    "investorCount": 23,
    "expectedReturnPercentage": 25.0,
    "producerName": "ABC Productions",
    "directorName": "John Director",
    "cast": "John Doe, Jane Smith",
    "storyline": "An epic adventure story",
    "productionStartDate": "2024-06-01",
    "fundingDeadline": "2024-05-31",
    "posterUrl": "https://example.com/poster.jpg",
    "trailerUrl": "https://example.com/trailer.mp4",
    "isActive": true,
    "fundingComplete": false,
    "createdAt": "2024-01-20T11:00:00"
  }
}
```

#### 2.4 Update Movie
**Endpoint**: `PUT /api/movies/{movieId}`

**Description**: Update movie information (Producer only)

**Path Parameters**:
- `movieId` (Long): Movie ID

**Request Body**:
```json
{
  "title": "The Great Adventure - Updated",
  "description": "An epic adventure movie with updated plot",
  "status": "PRODUCTION",
  "releaseDate": "2024-11-15"
}
```

**Response (Success - 200)**:
```json
{
  "success": true,
  "message": "Movie updated successfully",
  "movie": {
    "id": 301,
    "title": "The Great Adventure - Updated",
    "description": "An epic adventure movie with updated plot",
    "status": "PRODUCTION",
    "releaseDate": "2024-11-15",
    "updatedAt": "2024-01-20T15:30:00"
  }
}
```

---

## 3. Funding Service API (Port 8083)

### Base URL: `http://localhost:8090/api/funding` (via API Gateway)

#### 3.1 Create Investment
**Endpoint**: `POST /api/funding/invest`

**Description**: Create a new investment in a movie project

**Request Body**:
```json
{
  "userId": 301,
  "movieId": 101,
  "producerId": 201,
  "amount": 5000.00,
  "currency": "INR",
  "paymentMethod": "UPI",
  "userName": "John Doe",
  "movieTitle": "The Great Adventure",
  "producerName": "ABC Productions",
  "expectedReturnPercentage": 15.0
}
```

**Response (Success - 201)**:
```json
{
  "success": true,
  "message": "Investment created successfully",
  "investment": {
    "id": 14,
    "userId": 301,
    "movieId": 101,
    "producerId": 201,
    "amount": 5000.00,
    "currency": "INR",
    "transactionId": "TXN_5E930004FD754552",
    "status": "PENDING",
    "userName": "John Doe",
    "movieTitle": "The Great Adventure",
    "producerName": "ABC Productions",
    "expectedReturnPercentage": 15.0,
    "actualReturnAmount": 0,
    "returnPaid": false,
    "returnPaymentDate": null,
    "investmentDate": "2025-08-26T05:17:10.0240953",
    "expectedReturn": 5750.00,
    "createdAt": "2025-08-26T05:17:10.242886",
    "updatedAt": "2025-08-26T05:17:10.242886"
  }
}
```

#### 3.2 Get User Investments
**Endpoint**: `GET /api/funding/user/{userId}`

**Description**: Retrieve all investments made by a specific user

**Path Parameters**:
- `userId` (Long): User ID

**Response (Success - 200)**:
```json
{
  "success": true,
  "investments": [
    {
      "id": 14,
      "userId": 301,
      "movieId": 101,
      "producerId": 201,
      "amount": 5000.00,
      "transactionId": "TXN_5E930004FD754552",
      "status": "CONFIRMED",
      "userName": "John Doe",
      "movieTitle": "The Great Adventure",
      "producerName": "ABC Productions",
      "expectedReturnPercentage": 15.0,
      "actualReturnAmount": 0,
      "returnPaid": false,
      "investmentDate": "2025-08-26T05:17:10.0240953",
      "expectedReturn": 5750.00
    }
  ],
  "totalInvestments": 1,
  "totalAmount": 5000.00
}
```

#### 3.3 Get Movie Investments
**Endpoint**: `GET /api/funding/movie/{movieId}`

**Description**: Retrieve all investments for a specific movie

**Path Parameters**:
- `movieId` (Long): Movie ID

**Response (Success - 200)**:
```json
{
  "success": true,
  "investments": [
    {
      "id": 14,
      "userId": 301,
      "movieId": 101,
      "amount": 5000.00,
      "userName": "John Doe",
      "status": "CONFIRMED",
      "investmentDate": "2025-08-26T05:17:10.0240953"
    }
  ],
  "totalAmount": 5000.00,
  "investorCount": 1
}
```

#### 3.4 Confirm Investment
**Endpoint**: `PUT /api/funding/confirm/{transactionId}`

**Description**: Confirm a pending investment (Admin/Producer action)

**Path Parameters**:
- `transactionId` (String): Transaction ID

**Response (Success - 200)**:
```json
{
  "success": true,
  "message": "Investment confirmed successfully",
  "investment": {
    "id": 14,
    "transactionId": "TXN_5E930004FD754552",
    "status": "CONFIRMED",
    "confirmedAt": "2025-08-26T05:20:00"
  }
}
```

#### 3.5 Cancel Investment
**Endpoint**: `PUT /api/funding/cancel/{transactionId}`

**Description**: Cancel a pending investment

**Path Parameters**:
- `transactionId` (String): Transaction ID

**Query Parameters**:
- `reason` (String, optional): Cancellation reason

**Response (Success - 200)**:
```json
{
  "success": true,
  "message": "Investment cancelled successfully",
  "investment": {
    "id": 14,
    "transactionId": "TXN_5E930004FD754552",
    "status": "CANCELLED",
    "cancelledAt": "2025-08-26T05:25:00",
    "cancellationReason": "User requested cancellation"
  }
}
```

#### 3.6 Update Movie Collection and Distribute Returns
**Endpoint**: `POST /api/funding/producer/{producerId}/movie/{movieId}/collection`

**Description**: Producer updates movie collection at specific date and automatically distributes returns if profitable

**Path Parameters**:
- `producerId` (Long): Producer ID
- `movieId` (Long): Movie ID

**Request Body**:
```json
{
  "collectionAmount": 250000.00,
  "collectionDate": "2024-01-20",
  "notes": "Box office collection after 2 weeks",
  "autoDistributeReturns": true
}
```

**Response (Profitable - Success - 200)**:
```json
{
  "success": true,
  "data": {
    "movieId": 101,
    "producerId": 201,
    "collectionAmount": 250000.00,
    "totalInvestment": 200000.00,
    "profit": 50000.00,
    "collectionDate": "2024-01-20",
    "notes": "Box office collection after 2 weeks",
    "returnsDistributed": true,
    "investmentsProcessed": 5,
    "message": "Collection updated and returns distributed successfully",
    "processedAt": "2024-01-20T16:30:00"
  }
}
```

**Response (Loss - Success - 200)**:
```json
{
  "success": true,
  "data": {
    "movieId": 101,
    "producerId": 201,
    "collectionAmount": 150000.00,
    "totalInvestment": 200000.00,
    "profit": -50000.00,
    "collectionDate": "2024-01-20",
    "returnsDistributed": false,
    "message": "Collection updated but no profit to distribute. Loss: 50000.00",
    "processedAt": "2024-01-20T16:30:00"
  }
}
```

#### 3.7 Process Returns for Producer's Movie
**Endpoint**: `POST /api/funding/producer/{producerId}/movie/{movieId}/returns`

**Description**: Producer processes returns for investors of their movie

**Path Parameters**:
- `producerId` (Long): Producer ID
- `movieId` (Long): Movie ID

**Request Body**:
```json
{
  "totalRevenue": 150000.00,
  "notes": "Q1 2024 returns distribution"
}
```

**Response (Success - 200)**:
```json
{
  "success": true,
  "message": "Returns processed successfully for all investors",
  "data": {
    "movieId": 101,
    "producerId": 201,
    "totalRevenue": 150000.00,
    "investmentsProcessed": 5,
    "notes": "Q1 2024 returns distribution",
    "processedAt": "2024-01-20T14:30:00"
  }
}
```

#### 3.8 Get Producer Investments
**Endpoint**: `GET /api/funding/producer/{producerId}`

**Description**: Get all investments for a producer's movies

**Path Parameters**:
- `producerId` (Long): Producer ID

**Response (Success - 200)**:
```json
{
  "success": true,
  "investments": [
    {
      "id": 14,
      "movieId": 101,
      "movieTitle": "The Great Adventure",
      "userId": 301,
      "userName": "John Doe",
      "amount": 5000.00,
      "status": "CONFIRMED",
      "investmentDate": "2025-08-26T05:17:10"
    }
  ],
  "totalInvestments": 1,
  "totalAmount": 5000.00,
  "uniqueInvestors": 1
}
```

#### 3.9 Get Producer Movie Investments
**Endpoint**: `GET /api/funding/producer/{producerId}/movie/{movieId}`

**Description**: Get investments for a specific producer's movie

**Path Parameters**:
- `producerId` (Long): Producer ID
- `movieId` (Long): Movie ID

**Response (Success - 200)**:
```json
{
  "success": true,
  "investments": [
    {
      "id": 14,
      "userId": 301,
      "userName": "John Doe",
      "amount": 5000.00,
      "status": "CONFIRMED",
      "investmentDate": "2025-08-26T05:17:10"
    }
  ],
  "count": 1,
  "totalAmount": 5000.00,
  "investorCount": 1
}
```

---

## 4. API Gateway Routes (Port 8090)

### Base URL: `http://localhost:8090`

The API Gateway routes requests to appropriate microservices:

#### 4.1 User Service Routes
- **Pattern**: `/api/users/**`
- **Target**: `http://localhost:8084`
- **Examples**:
  - `POST http://localhost:8090/api/users/register`
  - `POST http://localhost:8090/api/users/login`
  - `GET http://localhost:8090/api/users/{userId}`

#### 4.2 Movie Service Routes
- **Pattern**: `/api/movies/**`
- **Target**: `http://localhost:8082`
- **Examples**:
  - `GET http://localhost:8090/api/movies`
  - `POST http://localhost:8090/api/movies`
  - `GET http://localhost:8090/api/movies/{movieId}`

#### 4.3 Funding Service Routes
- **Pattern**: `/api/funding/**`
- **Target**: `http://localhost:8083`
- **Examples**:
  - `POST http://localhost:8090/api/funding/invest`
  - `GET http://localhost:8090/api/funding/user/{userId}`
  - `POST http://localhost:8090/api/funding/producer/{producerId}/movie/{movieId}/collection`

---

## 5. Data Models

### 5.1 User Entity
```java
{
  "id": Long,
  "username": String,
  "email": String,
  "password": String (encrypted),
  "firstName": String,
  "lastName": String,
  "phoneNumber": String,
  "status": UserStatus (ACTIVE, INACTIVE, SUSPENDED),
  "createdAt": LocalDateTime,
  "updatedAt": LocalDateTime
}
```

### 5.2 Movie Entity
```java
{
  "id": Long,
  "title": String,
  "description": String,
  "genre": String,
  "director": String,
  "producerId": Long,
  "budget": BigDecimal,
  "expectedRevenue": BigDecimal,
  "releaseDate": LocalDate,
  "status": MovieStatus (FUNDING, PRODUCTION, RELEASED, CANCELLED),
  "createdAt": LocalDateTime,
  "updatedAt": LocalDateTime
}
```

### 5.3 Investment Entity
```java
{
  "id": Long,
  "userId": Long,
  "movieId": Long,
  "producerId": Long,
  "amount": BigDecimal,
  "currency": String,
  "transactionId": String,
  "status": InvestmentStatus (PENDING, CONFIRMED, CANCELLED, REFUNDED, RETURN_PAID),
  "userName": String,
  "movieTitle": String,
  "producerName": String,
  "expectedReturnPercentage": BigDecimal,
  "actualReturnAmount": BigDecimal,
  "returnPaid": Boolean,
  "returnPaymentDate": LocalDateTime,
  "investmentDate": LocalDateTime,
  "createdAt": LocalDateTime,
  "updatedAt": LocalDateTime
}
```

### 5.4 Transaction Entity
```java
{
  "id": Long,
  "transactionId": String,
  "userId": Long,
  "movieId": Long,
  "producerId": Long,
  "amount": BigDecimal,
  "type": TransactionType (INVESTMENT, PAYOUT, REFUND),
  "status": TransactionStatus (PENDING, COMPLETED, FAILED),
  "paymentMethod": String,
  "transactionDate": LocalDateTime,
  "createdAt": LocalDateTime,
  "updatedAt": LocalDateTime
}
```

---

## 6. Status Enums

### 6.1 Investment Status Flow
```
PENDING → CONFIRMED → RETURN_PAID
   ↓         ↓
CANCELLED  REFUNDED
```

### 6.2 Movie Status Flow
```
FUNDING → PRODUCTION → RELEASED
   ↓
CANCELLED
```

### 6.3 Transaction Status Flow
```
PENDING → COMPLETED
   ↓
FAILED
```

---

## 7. Business Logic

### 7.1 Investment Process
1. **Create Investment**: User creates investment with PENDING status
2. **Confirmation**: Admin/Producer confirms investment → CONFIRMED status
3. **Return Distribution**: When movie is profitable, returns are distributed → RETURN_PAID status

### 7.2 Return Calculation
- **Proportional Distribution**: Each investor receives returns based on their investment proportion
- **Formula**: `Investor Return = (Investment Amount / Total Investment) × Profit + Investment Amount`
- **Automatic Distribution**: When collection > total investment, returns are automatically distributed

### 7.3 Collection Update Process
1. **Producer Updates Collection**: Specifies collection amount and date
2. **Profit Calculation**: System calculates profit = collection - total investment
3. **Conditional Distribution**: Returns distributed only if profitable
4. **Status Updates**: Investment status changes to RETURN_PAID
5. **Transaction Records**: Payout transactions created for audit trail

---

## 8. Error Handling

### 8.1 Common HTTP Status Codes
- **200 OK**: Successful operation
- **201 Created**: Resource created successfully
- **400 Bad Request**: Invalid request data
- **401 Unauthorized**: Authentication required
- **403 Forbidden**: Access denied
- **404 Not Found**: Resource not found
- **409 Conflict**: Resource conflict (e.g., duplicate username)
- **500 Internal Server Error**: Server error

### 8.2 Error Response Format
```json
{
  "success": false,
  "message": "Error description",
  "errors": ["Detailed error 1", "Detailed error 2"],
  "timestamp": "2024-01-20T16:30:00",
  "path": "/api/funding/invest"
}
```

---

## 9. Authentication & Security

### 9.1 Authentication Flow
1. **User Registration**: Create account with encrypted password
2. **Login**: Authenticate with username/password
3. **Token Generation**: JWT token issued on successful login
4. **Token Usage**: Include token in Authorization header for protected endpoints

### 9.2 Authorization Levels
- **Public**: Registration, login, movie listing
- **User**: Create investments, view own investments
- **Producer**: Manage own movies, process returns
- **Admin**: Confirm investments, manage all resources

---

## 10. Database Schema

### 10.1 Database Structure
- **cinefund_users**: User management data
- **cinefund_movies**: Movie catalog data
- **cinefund_funding**: Investment and transaction data

### 10.2 Key Relationships
- **User ↔ Investment**: One-to-Many (User can have multiple investments)
- **Movie ↔ Investment**: One-to-Many (Movie can have multiple investors)
- **Producer ↔ Movie**: One-to-Many (Producer can have multiple movies)
- **Investment ↔ Transaction**: One-to-Many (Investment can have multiple transactions)

---

## 11. Testing Examples

### 11.1 Complete Investment Flow Test
```bash
# 1. Register User
curl -X POST http://localhost:8090/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username": "investor1", "email": "investor1@example.com", "password": "password123", "firstName": "John", "lastName": "Investor"}'

# 2. Create Movie
curl -X POST http://localhost:8090/api/movies \
  -H "Content-Type: application/json" \
  -d '{"title": "Test Movie", "description": "Test Description", "genre": "Action", "director": "Test Director", "producerId": 201, "budget": 100000.00, "expectedRevenue": 250000.00, "releaseDate": "2024-12-25", "status": "FUNDING"}'

# 3. Create Investment
curl -X POST http://localhost:8090/api/funding/invest \
  -H "Content-Type: application/json" \
  -d '{"userId": 301, "movieId": 101, "producerId": 201, "amount": 5000.00, "currency": "INR", "paymentMethod": "UPI", "userName": "John Investor", "movieTitle": "Test Movie", "producerName": "Test Producer", "expectedReturnPercentage": 15.0}'

# 4. Confirm Investment
curl -X PUT "http://localhost:8090/api/funding/confirm/TXN_5E930004FD754552"

# 5. Update Collection and Distribute Returns
curl -X POST http://localhost:8090/api/funding/producer/201/movie/101/collection \
  -H "Content-Type: application/json" \
  -d '{"collectionAmount": 250000.00, "collectionDate": "2024-01-20", "notes": "Box office collection", "autoDistributeReturns": true}'

# 6. Check User Investments
curl -X GET http://localhost:8090/api/funding/user/301
```

---

## 12. Deployment Configuration

### 12.1 Service Ports
- **API Gateway**: 8090
- **User Service**: 8084
- **Movie Service**: 8082
- **Funding Service**: 8083

### 12.2 Database Configuration
- **Host**: localhost:5432
- **Username**: postgres
- **Password**: password
- **Databases**: cinefund_users, cinefund_movies, cinefund_funding

### 12.3 CORS Configuration
- **Allowed Origins**: All origins (*)
- **Allowed Methods**: GET, POST, PUT, DELETE, OPTIONS
- **Allowed Headers**: All headers (*)
- **Credentials**: Enabled

---

## 13. Conclusion

The CineFund API provides a comprehensive platform for movie investment management with the following key features:

1. **User Management**: Registration, authentication, and profile management
2. **Movie Catalog**: Movie creation, listing, and management
3. **Investment Processing**: Investment creation, confirmation, and tracking
4. **Return Distribution**: Automated return calculation and distribution based on movie performance
5. **Transaction Management**: Complete audit trail of all financial transactions
6. **Producer Tools**: Collection updates and return processing capabilities

The microservices architecture ensures scalability, maintainability, and separation of concerns, while the API Gateway provides a unified entry point for all client applications.

---

**Document Version**: 1.0  
**Last Updated**: August 26, 2025  
**Generated By**: CineFund Development Team
