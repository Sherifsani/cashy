# Cashy - Personal Finance Management API

A comprehensive Spring Boot REST API for personal finance management with transaction tracking, budgeting, analytics, and more.

## 🚀 Quick Start

### Prerequisites
- Java 17+
- PostgreSQL
- Maven

### Setup & Run

1. **Clone and navigate**
```bash
git clone <repository-url>
cd cashy
```

2. **Configure database**
```properties
# src/main/resources/application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cashy_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. **Start the application**
```bash
./mvnw spring-boot:run
```

API available at: `http://localhost:8080`

## 📊 Analytics Dashboard Endpoint

### Get Dashboard Analytics
```http
GET /api/users/{userId}/analytics/dashboard
Authorization: Bearer {jwt_token}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "monthlySpending": [
      {"month": "OCTOBER", "year": 2024, "amount": 1250.50}
    ],
    "categoryBreakdown": [
      {"category": "Food", "amount": 450.00, "percentage": 36.0}
    ],
    "incomeVsExpense": {
      "income": 5000.00,
      "expenses": 3200.50,
      "netAmount": 1799.50
    },
    "summary": {
      "totalIncome": 5000.00,
      "totalExpenses": 3200.50,
      "transactionCount": 45,
      "averageTransaction": 182.22
    }
  }
}
```

## 🔑 Authentication

### Register
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username": "john_doe", "email": "john@example.com", "password": "password123"}'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "john@example.com", "password": "password123"}'
```

## 💰 Core Features

### Transactions
```bash
# Create transaction
POST /api/users/{userId}/transactions
{
  "amount": 50.00,
  "description": "Grocery shopping",
  "type": "EXPENSE",
  "categoryId": 1
}

# Get transactions (paginated)
GET /api/users/{userId}/transactions?page=0&size=20

# Search transactions
GET /api/users/{userId}/transactions/search?query=grocery

# Filter transactions
GET /api/users/{userId}/transactions/filter?type=EXPENSE&startDate=2024-01-01
```

### Categories
```bash
# Create category
POST /api/users/{userId}/categories
{"name": "Food & Dining", "description": "Restaurant and grocery expenses"}

# Get all categories
GET /api/users/{userId}/categories
```

### Budgets
```bash
# Create budget
POST /api/users/{userId}/budgets
{
  "name": "Monthly Food Budget",
  "amount": 500.00,
  "categoryId": 1,
  "period": "MONTHLY"
}

# Get budgets with spending status
GET /api/users/{userId}/budgets
```

## 📈 Analytics Endpoints

| Endpoint | Description |
|----------|-------------|
| `GET /analytics/dashboard` | Complete dashboard data for graphs |
| `GET /analytics/total` | Total income/expenses |
| `GET /analytics/spending-by-category` | Category breakdown |
| `GET /analytics/net-cash-flow` | Monthly cash flow |
| `GET /analytics/budget-performance` | Budget vs actual spending |

## 🛠 Tech Stack

- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT
- **Build**: Maven
- **Java**: 17+

## 📁 Project Structure

```
src/main/java/com/cashy/cashy/
├── auth/           # Authentication & user management
├── transaction/    # Transaction CRUD operations
├── category/       # Category management
├── budget/         # Budget tracking
├── analytics/      # Dashboard analytics
├── notification/   # User notifications
├── settings/       # User preferences
└── export/         # Data import/export
```

## 🔒 Security

- JWT-based authentication
- CORS enabled for frontend integration
- Password encryption with BCrypt
- Role-based access control

## 🚦 API Status Codes

- `200` - Success
- `201` - Created
- `400` - Bad Request
- `401` - Unauthorized
- `404` - Not Found
- `500` - Server Error

## 📝 Development

### Build
```bash
./mvnw clean package
```

### Run Tests
```bash
./mvnw test
```

### Production
```bash
java -jar target/cashy-*.jar
```

## 📊 Frontend Integration

The `/analytics/dashboard` endpoint provides ready-to-use data for:

- **Line Charts**: Monthly spending trends
- **Pie Charts**: Category breakdowns with percentages
- **Bar Charts**: Income vs expense comparisons
- **Summary Cards**: Key financial metrics

Perfect for Chart.js, D3.js, Recharts, or any charting library.

## 📄 License

MIT License
