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

Product management system
Category-based filtering (Men, Women, Child, Smart)
Search products by name
Add products to cart
Update product quantity
Remove products from cart
Cart management system
REST API architecture

🧠 Logic

Products are stored in database
Users can browse and search products
Products can be filtered by category
Users add products to cart
Cart manages quantity and items
System handles basic shopping workflow

🔌 API

GET /api/products
GET /api/products/search?name=
GET /api/products/category/{category}
POST /api/cart/add
PUT /api/cart/update
DELETE /api/cart/remove
GET /api/cart

▶️ Run Project
git clone https://github.com/amineyvazov368/eCommerce
cd eCommerce
mvn spring-boot:run
