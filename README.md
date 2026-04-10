🛒 E-Commerce API

Spring Boot REST API for managing products, categories, and shopping cart operations.

🚀 Tech Stack

Java 17
Spring Boot
Spring Data JPA
PostgreSQL
Hibernate
Maven
Lombok
ModelMapper

📌 Features

Product management
Category filtering (Men, Women, Child, Smart)
Search products by name
Add products to cart
Update product quantity in cart
Remove products from cart
Basic order structure

🧠 Logic

Users browse and search products
Products can be filtered by category
Users add products to cart
Cart manages quantity and total items
System handles basic shopping flow

🔌 API

GET /api/products
GET /api/products/search?name=
GET /api/products/category
POST /api/cart/add
PUT /api/cart/update
DELETE /api/cart/remove
GET /api/cart

▶️ Run Project
git clone https://github.com/amineyvazov368/eCommerce
cd eCommerce
mvn spring-boot:run
