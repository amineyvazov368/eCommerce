# 🎓 Student Registration API

Spring Boot REST API for managing student registration and exam stages.

---

## 🚀 Tech Stack
- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL (Aiven)
- Hibernate
- Maven
- Lombok
- QR Code (ZXing)

---

## 📌 Features
- Student registration
- Exam stage management
- Select students for second stage
- QR code generation for selected students
- PostgreSQL cloud database integration
- REST API architecture

---

## 🧠 Logic
- Students register for first exam
- System filters selected candidates (top results)
- Selected students move to second stage
- QR code URL generated and saved in DB

---

## 🔌 API
- POST /api/students
- GET /api/students
- POST /api/students/second-stage

---

## ▶️ Run Project
```bash
git clone https://github.com/amineyvazov368/student-registration-api
cd student-registration-api
mvn spring-boot:run
