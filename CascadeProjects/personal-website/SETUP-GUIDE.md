# CineFund Setup Guide

## Quick Start Instructions

### Step 1: Install Prerequisites

#### Install PostgreSQL
1. Download from: https://www.postgresql.org/download/windows/
2. Run installer, set password for `postgres` user
3. Keep default port 5432

#### Install Maven
1. Download from: https://maven.apache.org/download.cgi
2. Extract `apache-maven-3.9.5-bin.zip` to `C:\Program Files\Apache\maven`
3. Add `C:\Program Files\Apache\maven\bin` to system PATH
4. Restart terminal/IDE

### Step 2: Setup Databases

After PostgreSQL installation:

```bash
# Open PostgreSQL command line
psql -U postgres

# Run the setup script
\i setup-databases.sql

# Or manually create databases:
CREATE DATABASE cinefund_users;
CREATE DATABASE cinefund_movies;
CREATE DATABASE cinefund_funding;
```

### Step 3: Run the Application

#### Option A: Use the batch script
```bash
# Double-click or run:
run-services.bat
```

#### Option B: Manual startup
Open 4 separate terminals and run:

```bash
# Terminal 1 - User Service
cd user-service
mvn spring-boot:run

# Terminal 2 - Movie Service  
cd movie-service
mvn spring-boot:run

# Terminal 3 - Funding Service
cd funding-service
mvn spring-boot:run

# Terminal 4 - API Gateway
cd api-gateway
mvn spring-boot:run
```

### Step 4: Access the Application

- **Frontend**: Open `frontend/index.html` in browser
- **API Gateway**: http://localhost:8080
- **API Documentation**:
  - User Service: http://localhost:8081/swagger-ui.html
  - Movie Service: http://localhost:8082/swagger-ui.html
  - Funding Service: http://localhost:8083/swagger-ui.html

## Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL is running
- Check username/password in `application.yml` files
- Verify databases exist: `\l` in psql

### Maven Issues
- Verify Maven is in PATH: `mvn --version`
- Check Java version: `java --version` (needs Java 17+)

### Port Conflicts
- Check if ports 8080-8083 are available
- Modify `server.port` in `application.yml` if needed

## Database Configuration

Current settings (can be modified in `application.yml`):
- **Username**: `postgres`
- **Password**: `password`
- **Host**: `localhost:5432`
- **Databases**: `cinefund_users`, `cinefund_movies`, `cinefund_funding`
