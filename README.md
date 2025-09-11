# TTTN - Hệ Thống Quản Lý Người Dùng với AI

## 📋 Mô Tả Dự Án

TTTN là một ứng dụng web backend được xây dựng bằng Spring Boot, cung cấp hệ thống xác thực người dùng hoàn chỉnh với tích hợp AI để đề xuất mật khẩu thông minh. Dự án sử dụng JWT cho xác thực, Redis cho cache, PostgreSQL làm cơ sở dữ liệu chính và tích hợp Google Gemini AI.

## 🚀 Tính Năng Chính

### 🔐 Xác Thực & Bảo Mật
- **Đăng ký/Đăng nhập** với JWT Authentication
- **Refresh Token** tự động gia hạn phiên làm việc
- **Logout** với blacklist token trong Redis
- **Quên mật khẩu** qua email với template HTML
- **Đổi mật khẩu** an toàn cho người dùng đã đăng nhập

### 🤖 Tích Hợp AI
- **Đề xuất mật khẩu thông minh** sử dụng Google Gemini AI
- Tạo mật khẩu mạnh dựa trên yêu cầu bảo mật

### 📧 Gửi Email
- Template email HTML với Thymeleaf
- Gửi email reset mật khẩu tự động

### 📚 API Documentation
- Swagger UI tích hợp sẵn
- Tài liệu API tự động sinh

## 🛠️ Công Nghệ Sử Dụng

### Backend Framework
- **Spring Boot 3.5.5** - Framework chính
- **Spring Security** - Bảo mật ứng dụng
- **Spring Data JPA** - ORM và database access
- **Spring Data Redis** - Cache và session management
- **Spring Mail** - Gửi email

### Database & Cache
- **PostgreSQL** - Cơ sở dữ liệu chính
- **Redis** - Cache và token blacklist

### Authentication & Security
- **JWT (JSON Web Token)** - Xác thực stateless
- **BCrypt** - Mã hóa mật khẩu

### AI Integration
- **Google Gemini AI** - Đề xuất mật khẩu thông minh
- **Spring AI** - Framework tích hợp AI

### Development Tools
- **Lombok** - Giảm boilerplate code
- **MapStruct** - Object mapping
- **Swagger/OpenAPI** - API documentation
- **Maven** - Build tool

## 📁 Cấu Trúc Dự Án

```
src/main/java/org/example/tttn/
├── config/           # Cấu hình ứng dụng
│   ├── ApiResponse.java
│   └── SecurityConfig.java
├── controller/       # REST Controllers
│   ├── AuthController.java
│   └── UserController.java
├── dto/             # Data Transfer Objects
├── entity/          # JPA Entities
├── security/        # JWT & Security components
│   ├── JwtUtil.java
│   └── JwtAuthenticationFilter.java
├── service/         # Business Logic
│   ├── interfaces/
│   └── impl/
├── util/           # Utility classes
│   └── SecurityUtil.java
└── constants/      # Constants & Message codes
```

## ⚙️ Cài Đặt và Chạy Dự Án

### Yêu Cầu Hệ Thống
- **Java 21+**
- **Maven 3.6+**
- **PostgreSQL 12+**
- **Redis 6+**
- **Docker & Docker Compose** (tùy chọn)

### 1. Clone Repository
```bash
git clone <repository-url>
cd tttn
```

### 2. Cấu Hình Environment Variables
Tạo file `.env` hoặc set environment variables:
```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=tttn
DB_USERNAME=your_username
DB_PASSWORD=your_password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT
JWT_SECRET=your-super-secret-jwt-key-here
JWT_EXPIRATION=86400000

# Email
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# AI
GEMINI_KEY=your-gemini-api-key
```

### 3. Chạy với Docker Compose
```bash
# Chạy PostgreSQL và Redis
docker-compose up -d

# Chạy ứng dụng
./mvnw spring-boot:run
```

### 4. Chạy Manual
```bash
# Cài đặt dependencies
./mvnw clean install

# Chạy ứng dụng
./mvnw spring-boot:run
```

## 📖 API Documentation

Sau khi chạy ứng dụng, truy cập:
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **API Docs**: http://localhost:8081/v3/api-docs

### 🔑 Authentication Endpoints

| Method | Endpoint | Mô Tả |
|--------|----------|-------|
| POST | `/api/v1/auth/register` | Đăng ký tài khoản mới |
| POST | `/api/v1/auth/login` | Đăng nhập |
| POST | `/api/v1/auth/logout` | Đăng xuất |
| POST | `/api/v1/auth/refresh` | Refresh token |
| POST | `/api/v1/auth/forgot-password` | Quên mật kh��u |

### 👤 User Endpoints (Yêu cầu Authentication)

| Method | Endpoint | Mô Tả |
|--------|----------|-------|
| POST | `/api/v1/user/change-password` | Đổi mật khẩu |
| GET | `/api/v1/user/password-suggestions` | Đề xuất mật khẩu AI |

## 🔧 Cấu Hình

### Database Configuration
```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:tttn}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
```

### Redis Configuration
```yaml
spring:
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
```

### AI Configuration
```yaml
spring:
  ai:
    openai:
      api-key: ${GEMINI_KEY}
      chat:
        base-url: https://generativelanguage.googleapis.com
        completions-path: /v1beta/openai/chat/completions
        options:
          model: gemini-2.0-flash
```

## 🧪 Testing

### Chạy Unit Tests
```bash
./mvnw test
```

### Test với Postman
Import collection từ `/docs/postman/` để test API endpoints.

## 🚀 Deployment

### Docker Production
```bash
# Build image
docker build -t tttn-app .

# Run container
docker run -p 8081:8081 --env-file .env tttn-app
```

### JAR Deployment
```bash
# Build JAR
./mvnw clean package -DskipTests

# Run JAR
java -jar target/tttn-0.0.1-SNAPSHOT.jar
```

## 🔒 Bảo Mật

- **JWT Token** với thời gian hết hạn có thể cấu hình
- **Password Encryption** với BCrypt
- **Token Blacklisting** trong Redis khi logout
- **CORS Configuration** cho frontend integration
- **Input Validation** với Bean Validation

## 🤝 Đóng Góp

1. Fork repository
2. Tạo feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

## 📝 License

Distributed under the MIT License. See `LICENSE` for more information.

## 📞 Liên Hệ

- **Email**: your-email@example.com
- **GitHub**: [your-github-username](https://github.com/your-github-username)

## 🙏 Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Google Gemini AI](https://ai.google.dev/)
- [JWT.io](https://jwt.io/)
- [Swagger](https://swagger.io/)