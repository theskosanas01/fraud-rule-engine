# Fraud Rule Engine Service

A production-grade fraud detection system built with Spring Boot 3.3.0 and Java 21. This service processes categorized transaction events and flags potential fraud based on configurable rules, storing results in a MySQL database with full REST API access and monitoring capabilities.

## Features

- **Rule-Based Fraud Detection**: Six built-in fraud detection rules with extensible architecture
- **REST API**: Complete API for transaction submission and retrieval with pagination and filtering
- **OpenAPI/Swagger**: Full API documentation with interactive UI
- **Monitoring**: Prometheus metrics, Grafana dashboards, and health checks
- **Database**: MySQL 8.0 with indexed queries
- **Production-Ready**: Docker containerization, comprehensive logging, error handling
- **Testing**: Unit tests with JUnit 5

## Fraud Detection Rules

1. **HIGH_AMOUNT_RULE** - Flags transactions exceeding ZAR 5,000 (Risk Score: 25.0)
2. **UNUSUAL_TIME_RULE** - Flags transactions between 2 AM - 5 AM (Risk Score: 15.0)
3. **MULTIPLE_TRANSACTIONS_RULE** - Flags 5+ transactions/hour from same customer (Risk Score: 20.0)
4. **INTERNATIONAL_TRANSACTION_RULE** - Flags international transactions (Risk Score: 10.0)
5. **UNUSUAL_MERCHANT_RULE** - Flags new merchants not previously used (Risk Score: 18.0)
6. **CRYPTOCURRENCY_RULE** - Flags cryptocurrency transactions (Risk Score: 30.0)

Risk scores are cumulative (max 100). Transactions are flagged if risk score ≥ 50.

## Quick Start

### Prerequisites

- Java 21 or later
- Maven 3.6.0 or later
- MySQL 8.0

### Local Development Setup

#### 1. Database Setup

```bash
mysql -u root -p
CREATE DATABASE fraud CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'skosanas';
GRANT ALL PRIVILEGES ON fraud.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

#### 2. Build and Run

```bash
mvn clean package -DskipTests
java -jar target/fraud-rule-engine-1.0.0.jar
```

The application starts on `http://localhost:8080`

#### 3. Access Swagger UI

Open browser to: `http://localhost:8080/api/swagger-ui.html`


## API Endpoints

### Transaction Management (Base: /api/v1/transactions)

- `POST /process` - Submit transaction for fraud analysis
- `GET /{id}` - Get transaction by database ID
- `GET /transaction-id/{transactionId}` - Get by transaction ID
- `GET /flagged` - Get flagged transactions (paginated)
- `GET /customer/{customerId}` - Get customer transactions (paginated)
- `GET /category/{category}` - Get by category (paginated)
- `GET /flagged/{category}` - Flagged by category (paginated)

### Statistics (Base: /api/v1/stats)

- `GET /fraud-statistics` - Get fraud detection statistics

### Monitoring (Base: /api/actuator)

- `GET /health` - Service health status
- `GET /metrics` - Available metrics
- `GET /prometheus` - Prometheus metrics endpoint

## Query Parameters

All list endpoints support:
- **page**: Page number (0-indexed, default: 0)
- **size**: Page size (default: 20)
- **sort**: Sort field (default: createdAt)
- **direction**: ASC or DESC (default: DESC)

## Transaction Categories

- `ONLINE_PURCHASE`
- `ATM_WITHDRAWAL`
- `BILL_PAYMENT`
- `TRANSFER`
- `CASH_DEPOSIT`
- `SUBSCRIPTION`
- `RETAIL`
- `CRYPTOCURRENCY`
- `INTERNATIONAL`
- `OTHER`

## Testing

```bash
mvn test
```

## Configuration

Configuration is in `src/main/resources/application.yml`. Key settings:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fraud
    username: root
    password: skosanas
  jpa:
    hibernate:
      ddl-auto: update
```
For internal use only- contact Samukelisiwe Skosana (theskosanas@gmail.com)
