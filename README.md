# 🔗 Clean URL Shortener

A **URL shortener service** built with **Java 21, Spring Boot, and Clean Architecture (Uncle Bob)**.  
The project demonstrates modular boundaries (Domain, Application, Adapters, Frameworks) with Gradle multi-module setup.

---

## 🚀 Features
- [x] Create a shortlink from a long URL
- [] Retrieve original URL from shortlink
- [] Toggle/disable a shortlink

---

## 🛠️ Tech stack
- **Java 21**
- **Gradle 8+ (KTS)**
- **Spring Boot 3**
- **JUnit 5, Mockito** for tests
- **Docker** for containerization

---

## ⚙️ Build & Run

### 1. Clone
```bash
git clone https://github.com/Daryl-03/clean-url-shortener.git
cd clean-url-shortener
```

### 2. Run tests
```bash
./gradlew clean test
```

### 3. Run locally (Spring Boot)
```bash
./gradlew :bootstrap-spring:bootRun
```

App will be available at:  
👉 http://localhost:8080/api/shortlinks

---

## 🐳 Docker

### Build image
```bash
./gradlew :bootstrap-spring:bootJar
docker build -t shortlink-app:latest .
```

### Run container
```bash
docker run -p 8080:8080 shortlink-app:latest
```