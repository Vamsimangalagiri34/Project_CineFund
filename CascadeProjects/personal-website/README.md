# CineFund - Movie Investment Platform

CineFund is a comprehensive web-based platform that connects movie producers with investors, enabling transparent and secure film funding.

## 🎬 Features

- **Producer Registration & Movie Listing**: Producers can register and list their movie projects with detailed information
- **User Investment Portal**: Investors can browse movies, view details, and make secure investments
- **Investment Tracking**: Real-time tracking of investments and returns for both investors and producers
- **Secure Transactions**: Integrated payment gateway for safe money transfers
- **Transparency & Accountability**: Complete transaction history and funding details

## 🏗️ Architecture

This platform is built using Spring Boot microservices architecture:

- **User Service**: Handles user registration, authentication, and profile management
- **Producer/Movie Service**: Manages movie listings and producer details
- **Funding Service**: Handles investments, transactions, and payout mechanisms
- **API Gateway**: Routes requests and manages inter-service communication
- **Frontend**: Modern web interface built with HTML, CSS, and JavaScript

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12.0+
- Node.js (for frontend development)

### Running the Services

1. Start each microservice:
   ```bash
   # User Service (Port 8081)
   cd user-service
   mvn spring-boot:run

   # Movie Service (Port 8082)
   cd movie-service
   mvn spring-boot:run

   # Funding Service (Port 8083)
   cd funding-service
   mvn spring-boot:run

   # API Gateway (Port 8080)
   cd api-gateway
   mvn spring-boot:run
   ```

2. Open the frontend:
   ```bash
   cd frontend
   # Open index.html in your browser or serve with a local server
   ```

## 📊 Database Schema

The platform uses PostgreSQL with separate databases for each service to maintain microservice independence.

### Database Setup

1. **Install PostgreSQL:**
   - Download from https://www.postgresql.org/download/
   - Or use package manager: `winget install PostgreSQL.PostgreSQL`

2. **Create databases:**
   ```sql
   -- Connect to PostgreSQL as superuser
   psql -U postgres

   -- Create databases
   CREATE DATABASE cinefund_users;
   CREATE DATABASE cinefund_movies;
   CREATE DATABASE cinefund_funding;

   -- Create user (optional)
   CREATE USER cinefund_user WITH PASSWORD 'password';
   GRANT ALL PRIVILEGES ON DATABASE cinefund_users TO cinefund_user;
   GRANT ALL PRIVILEGES ON DATABASE cinefund_movies TO cinefund_user;
   GRANT ALL PRIVILEGES ON DATABASE cinefund_funding TO cinefund_user;
   ```

3. **Update application.yml files** (already configured):
   - User Service: `jdbc:postgresql://localhost:5432/cinefund_users`
   - Movie Service: `jdbc:postgresql://localhost:5432/cinefund_movies`
   - Funding Service: `jdbc:postgresql://localhost:5432/cinefund_funding`

## 🔐 Security

- JWT-based authentication
- Role-based access control (Investor, Producer, Admin)
- Secure payment processing
- Data encryption for sensitive information

## 📱 API Documentation

API documentation is available at:
- User Service: http://localhost:8081/swagger-ui.html
- Movie Service: http://localhost:8082/swagger-ui.html
- Funding Service: http://localhost:8083/swagger-ui.html

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.
