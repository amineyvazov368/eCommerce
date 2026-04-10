🛒 E-Commerce System — Spring Boot Project
📝 Description

This project is a full-featured E-Commerce web application built using Spring Boot and modern backend technologies.
It provides a structured system for managing products, categories, users, and orders.

🔍 What problem it solves:

For Businesses:

Manage products and categories efficiently
Handle customer orders and track purchase flow
Organize shopping cart operations

For Users:

Browse and search products easily
Add/remove items from cart
Control product quantities in cart
Experience a simple and intuitive shopping flow

Technically:

Demonstrates clean Layered Architecture (Controller → Service → Repository)
Shows best practices in Spring Boot backend development
Implements scalable and maintainable project structure
🛠️ Tech Stack

Backend:

Java 17
Spring Boot
Spring Data JPA
Hibernate

Database:
PostgreSQL

Tools & Libraries:

Lombok
ModelMapper

Build Tool:

Maven

⚙️ Setup Instructions

Follow these steps to run the project locally:

1. Clone the repository
git clone https://github.com/amineyvazov368/eCommerce.git
2. Configure Database

Create a PostgreSQL database (example: ecommerce_db)

Update your application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

3. Build the project
mvn clean install
4. Run the application
mvn spring-boot:run
🚀 Features
🔍 Product search by name
🛒 Add to cart functionality
➕➖ Quantity control in cart
📂 Filter products by category (Men, Women, Child, Smart)
📦 Order management system (basic)


🔑 API / Usage
📌 Main Endpoints:
/products → Get all products
/products/search?name= → Search product by name
/cart → View cart
/cart/add → Add product to cart
/cart/update → Update quantity
/cart/remove → Remove product

📌 Future Improvements
🔐 Authentication & Authorization (Spring Security)
💳 Payment integration
📊 Admin dashboard
⭐ Product reviews & ratings
📦 Advanced order tracking
