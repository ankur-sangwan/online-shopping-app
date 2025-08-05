Online Shopping Application


This is a Spring Boot-based backend application for an Online Shopping platform. It includes features like user authentication, role-based access control, cart and wishlist management, order processing, payments, and return handling.

---

##  Features

-  User Registration & Login (with JWT)
-  Role-Based Access Control (User/Admin)
-  Product Management (CRUD)
-  Wishlist & Cart Functionality
-  Place Order from Cart or Buy Now
-  Return (Full & Partial) with Refund Logic
-  Payment Integration
-  Real-time Delivery Status Updates (Webhooks)
-  Secure APIs with Rate Limiting (Redis)


##  Tech Stack

-  Java 17
- Spring Boot
- Spring Security + JWT
- Hibernate + JPA
- PostgreSQL
- Redis** (for rate limiting)
- Bucket4j** (for API limiting)
- Maven

## Setup DB
Create a PostgreSQL database (OnlineShoppingApplication)

Update application.properties with  DB credentials

## Run the App
./mvnw spring-boot:run
