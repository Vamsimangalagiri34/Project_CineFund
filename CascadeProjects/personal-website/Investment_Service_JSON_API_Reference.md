# Investment Service - JSON Data with URLs Reference

## Overview
This document provides examples of how to send JSON data with URLs in the CineFund Investment Service.

## API Endpoints and JSON Examples

### 1. Create Investment
**URL:** `POST http://localhost:8090/api/funding/invest`

**Request Body:**
```json
{
  "userId": 1,
  "movieId": 1,
  "producerId": 1,
  "amount": 5000.00,
  "currency": "INR",
  "paymentMethod": "UPI",
  "userName": "John Doe",
  "movieTitle": "Action Movie 2024",
  "producerName": "Producer Studios",
  "expectedReturnPercentage": 15.0
}
```

**Minimum Required Fields:**
```json
{
  "userId": 1,
  "movieId": 1,
  "producerId": 1,
  "amount": 5000.00
}
```

**Response JSON:**
```json
{
  "success": true,
  "message": "Investment created successfully",
  "investment": {
    "id": 1,
    "userId": 101,
    "movieId": 301,
    "producerId": 201,
    "amount": 5000.00,
    "transactionId": "TXN_ABC123DEF456",
    "status": "PENDING",
    "userName": "John Doe",
    "movieTitle": "The Great Adventure",
    "producerName": "ABC Productions",
    "expectedReturnPercentage": 15.0,
    "investmentDate": "2025-08-26T04:00:00",
    "createdAt": "2025-08-26T04:00:00"
  }
}
```

### 2. Get Investments by Movie
**URL:** `GET http://localhost:8090/api/funding/movie/301`

**Response JSON:**
```json
{
  "success": true,
  "investments": [
    {
      "id": 1,
      "userId": 101,
      "movieId": 301,
      "producerId": 201,
      "amount": 5000.00,
      "transactionId": "TXN_ABC123DEF456",
      "status": "CONFIRMED",
      "userName": "John Doe",
      "movieTitle": "The Great Adventure",
      "producerName": "ABC Productions",
      "expectedReturnPercentage": 15.0,
      "investmentDate": "2025-08-26T04:00:00"
    }
  ],
  "count": 1,
  "totalAmount": 5000.00,
  "investorCount": 1
}
```

### 3. Get Investors for Producer
**URL:** `GET http://localhost:8090/api/funding/producer/201/investors`

**Response JSON:**
```json
{
  "success": true,
  "investments": [
    {
      "id": 1,
      "userId": 101,
      "movieId": 301,
      "producerId": 201,
      "amount": 5000.00,
      "transactionId": "TXN_ABC123DEF456",
      "status": "CONFIRMED",
      "userName": "John Doe",
      "movieTitle": "The Great Adventure",
      "producerName": "ABC Productions",
      "expectedReturnPercentage": 15.0,
      "investmentDate": "2025-08-26T04:00:00"
    }
  ],
  "count": 1,
  "totalInvestmentAmount": 5000.00,
  "uniqueInvestorCount": 1
}
```

### 4. Cancel Investment
**URL:** `PUT http://localhost:8090/api/funding/cancel/TXN_ABC123DEF456?reason=Insufficient funds`

**Response JSON:**
```json
{
  "success": true,
  "message": "Investment cancelled successfully"
}
```

**Error Response JSON:**
```json
{
  "success": false,
  "message": "Investment with transaction ID 'TXN_ABC123DEF456' not found"
}
```

### 5. User Investment (via User Service)
**URL:** `POST http://localhost:8084/api/users/101/invest`

**Request JSON:**
```json
{
  "movieId": 301,
  "amount": 5000.00,
  "notes": "Investing in this promising project",
  "paymentMethod": "UPI"
}
```

**Response JSON:**
```json
{
  "success": true,
  "message": "Investment successful",
  "investment": {
    "transactionId": "TXN_ABC123DEF456",
    "amount": 5000.00,
    "movieTitle": "The Great Adventure",
    "status": "PENDING"
  },
  "walletBalance": 45000.00
}
```

## Frontend API Usage Examples

### JavaScript API Calls

```javascript
// 1. Create Investment
const investmentData = {
  movieId: 301,
  amount: 5000.00,
  notes: "Great project!",
  paymentMethod: "UPI"
};

const response = await apiService.investInMovie(101, investmentData);
console.log(response);

// 2. Get Movie Investments
const movieInvestments = await apiService.getInvestmentsByMovie(301);
console.log(movieInvestments);

// 3. Get Producer Investors
const producerInvestors = await apiService.getInvestorsForProducer(201);
console.log(producerInvestors);

// 4. Get User Investments
const userInvestments = await apiService.getUserInvestments(101);
console.log(userInvestments);
```

## URL Patterns

### Direct Service URLs
- **User Service:** `http://localhost:8084/api/users/*`
- **Movie Service:** `http://localhost:8082/api/movies/*`
- **Funding Service:** `http://localhost:8083/api/funding/*`

### API Gateway URLs
- **Gateway Base:** `http://localhost:8090`
- **Movies (via Gateway):** `http://localhost:8090/api/movies/*`
- **Funding (via Gateway):** `http://localhost:8090/api/funding/*`

## HTTP Methods and Status Codes

| Method | Endpoint | Success Code | Error Codes |
|--------|----------|--------------|-------------|
| POST | `/api/funding/invest` | 201 Created | 400 Bad Request |
| GET | `/api/funding/movie/{id}` | 200 OK | 404 Not Found |
| GET | `/api/funding/user/{id}` | 200 OK | 404 Not Found |
| PUT | `/api/funding/cancel/{txnId}` | 200 OK | 404 Not Found, 400 Bad Request |
| GET | `/api/funding/producer/{id}/investors` | 200 OK | 404 Not Found |

## Common JSON Response Structure

All API responses follow this structure:

```json
{
  "success": boolean,
  "message": "string (optional)",
  "data": object,
  "count": number (for lists),
  "totalAmount": number (for financial data)
}
```

## Error Handling Examples

### 404 Not Found
```json
{
  "success": false,
  "message": "Investment with transaction ID 'TXN_INVALID' not found"
}
```

### 400 Bad Request
```json
{
  "success": false,
  "message": "Cannot cancel a confirmed investment"
}
```

### 500 Internal Server Error
```json
{
  "success": false,
  "message": "Failed to process investment: Database connection error"
}
```

## Architecture Flow

```
Frontend (JavaScript) 
    ↓ HTTP Request with JSON
User Service (Port 8084)
    ↓ Internal API Call
Funding Service (Port 8083)
    ↓ Database Operation
PostgreSQL Database
    ↓ JSON Response
Frontend
```

## Key Points

1. **Authentication:** All requests should include user authentication
2. **Validation:** JSON payloads are validated on the server side
3. **Transaction IDs:** Generated automatically and returned in responses
4. **Status Tracking:** Investments have status: PENDING → CONFIRMED/CANCELLED → RETURN_PAID
5. **Error Handling:** Proper HTTP status codes with descriptive messages
6. **CORS:** Enabled for cross-origin requests from frontend

## 9. Process Returns for Producer's Single Movie

**Endpoint:** `POST /api/funding/producer/{producerId}/movie/{movieId}/returns`

**Description:** Allow producer to process returns for a specific movie

**URL:** `http://localhost:8090/api/funding/producer/1/movie/1/returns`

**Request Body:**
```json
{
  "totalRevenue": 150000.00,
  "notes": "First quarter returns distribution"
}
```

**Response:**
```json
{
  "movieId": 1,
  "producerId": 1,
  "totalRevenue": 150000.00,
  "investmentsProcessed": 5,
  "notes": "First quarter returns distribution",
  "processedAt": "2024-01-20T14:30:00"
}
```

**Note:** When returns are processed, the investment status changes from `CONFIRMED` to `RETURN_PAID`, and the following fields are updated:
- `status`: Changed to `RETURN_PAID`
- `returnPaid`: Set to `true`
- `actualReturnAmount`: Set to calculated return amount
- `returnPaymentDate`: Set to current timestamp

## 10. Process Returns for All Producer Movies

**Endpoint:** `POST /api/funding/producer/{producerId}/returns/bulk`

**Description:** Allow producer to process returns for all their movies at once

**URL:** `http://localhost:8090/api/funding/producer/1/returns/bulk`

**Request Body:**
```json
{
  "movie_1": 150000.00,
  "movie_2": 200000.00,
  "movie_3": 75000.00
}
```

**Response:**
```json
{
  "producerId": 1,
  "moviesProcessed": [
    {
      "movieId": 1,
      "revenue": 150000.00,
      "investmentsProcessed": 5,
      "movieTitle": "Action Hero"
    },
    {
      "movieId": 2,
      "revenue": 200000.00,
      "investmentsProcessed": 8,
      "movieTitle": "Drama Queen"
    }
  ],
  "totalMovies": 2,
  "totalRevenueProcessed": 350000.00,
  "totalInvestmentsProcessed": 13,
  "processedAt": "2024-01-20T15:45:00"
}
```

## 11. Update Movie Collection and Auto-Distribute Returns

**Endpoint:** `POST /api/funding/producer/{producerId}/movie/{movieId}/collection`

**Description:** Producer updates movie collection at a specific date. If collection shows profit, automatically distributes returns to investors proportionally.

**URL:** `http://localhost:8090/api/funding/producer/201/movie/101/collection`

**Request Body:**
```json
{
  "collectionAmount": 250000.00,
  "collectionDate": "2024-01-20",
  "notes": "Box office collection after 2 weeks",
  "autoDistributeReturns": true
}
```

**Response (Profitable - Returns Distributed):**
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

**Response (Loss - No Returns):**
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

## 12. Get Producer Return Summary

**Endpoint:** `GET /api/funding/producer/{producerId}/returns/summary`

**Description:** Get comprehensive return payment summary for a producer

**URL:** `http://localhost:8090/api/funding/producer/1/returns/summary`

**Response:**
```json
{
  "producerId": 1,
  "producerName": "John Producer",
  "totalInvestments": 25,
  "totalInvestmentAmount": 500000.00,
  "paidReturns": 15,
  "unpaidReturns": 10,
  "totalReturnsPaid": 180000.00,
  "movieBreakdown": [
    {
      "movieId": 1,
      "movieTitle": "Action Hero",
      "totalInvestments": 5,
      "totalAmount": 100000.00,
      "paidReturns": 5,
      "unpaidReturns": 0
    },
    {
      "movieId": 2,
      "movieTitle": "Drama Queen",
      "totalInvestments": 8,
      "totalAmount": 200000.00,
      "paidReturns": 3,
      "unpaidReturns": 5
    }
  ],
  "generatedAt": "2024-01-20T16:00:00"
}
```

## Testing with cURL

```bash
# Create Investment
curl -X POST http://localhost:8090/api/funding/invest \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 101,
    "movieId": 301,
    "producerId": 201,
    "amount": 5000.00,
    "userName": "John Doe",
    "movieTitle": "The Great Adventure",
    "producerName": "ABC Productions",
    "expectedReturnPercentage": 15.0,
    "paymentMethod": "UPI"
  }'

# Get Movie Investments
curl -X GET http://localhost:8090/api/funding/movie/301

# Cancel Investment
curl -X PUT "http://localhost:8090/api/funding/cancel/TXN_ABC123DEF456?reason=Test"

# Process Returns for Single Movie
curl -X POST http://localhost:8090/api/funding/producer/1/movie/1/returns \
  -H "Content-Type: application/json" \
  -d '{
    "totalRevenue": 150000.00,
    "notes": "Q1 2024 returns"
  }'

# Process Returns for All Producer Movies
curl -X POST http://localhost:8090/api/funding/producer/1/returns/bulk \
  -H "Content-Type: application/json" \
  -d '{
    "movie_1": 150000.00,
    "movie_2": 200000.00
  }'

# Get Producer Return Summary
curl -X GET http://localhost:8090/api/funding/producer/1/returns/summary
```
