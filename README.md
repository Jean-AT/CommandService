# Documentation

### General Description
A Backend system for restaurant management. It allows for the administration of users, product
inventory, and lifecycle of orders, from order taking to payment or cancellation.

### Tech Stack
* Java 17
* Spring Boot 3.5.9
* PostgreSQL
* Spring Data JPA
* SpringSecurity, JSON Web Token
* WebSocket
* Maven

### System Architecture
The project follows a classic three-layer architecture:
* Controllers (controller): Handle HTTP requests (REST API).
* Services (service): Contain business logic (validations, calculations).
* Repositories (repository): Interface with the database using JPA.
* Models/Entities (models): Representation of database tables.
* DTOs (dto): Objects for data transfer between client and server.

### Main Modules and Entities
#### A. Users (Users)
Management of restaurant staff.
* Roles: Defined in the UsersRol enum (WaiterUser, ChefUser, CheckerUser, Admin).
* Functionality: Employee registration, listing, and role changes. Passwords are encrypted (likely with BCrypt per config).
#### B. Products (Product)
Menu and stock management.
* Data: Name, price, stock, status (active/inactive), and category.
* Categories: Defined in ProductsCategory (Entrance, Salads, Background, Desserts, Drinks, Bar, Garnishes, Cafe, Promotions).
* Logic: Validation of duplicate names, stock control when ordering.
#### C. Orders (Order and OrderDetails)
The core of the business.
* Structure: An Order belongs to a table and a waiter (User). It contains a list of OrderDetails (products and quantities).
* Order Status: Defined in OrderStatus (Pending, Cooking, Delivered, Paid, Cancelled).
* Key Logic:
    * Automatic calculation of total price.
    * Stock validation before adding items.
    * Editing block if the order is "Paid" or "Cancelled".

### API Endpoints

* /user (User Management)

    * GET /user/list: List all users.
    * POST /user/create: Register a new employee.
    * GET /user/{id}: Get user by ID.
    * PUT /user/rol/{id}: Change a user's role.

* /product (Product Management)
    * POST /product/create: Create a new product.
    * GET /product/list: List active products.
    * GET /product/list/category: List by category and status.
    * PUT /product/update/{id}: Update product data.
    * PUT /product/stock/{id}: Manually modify stock.
    * PUT /product/status/{id}: Activate/Deactivate product.

* /order (Order Management)
    * POST /order: Create a new order (table, waiter, initial items).
    * PUT /order/{id}: Add items to an existing order.
    * DELETE /order/delete/{id}: Remove an item from the order (returns stock).
    * PUT /order/status/{id}: Change status (e.g., from Pending to Cooking).
    * GET /order/list/{id}: View details of a specific order.
    * GET /order/list/{user}/{status}: View active orders by user and status.

### Current Configuration
Database
* URL: jdbc:postgresql://localhost:5432/CommandService
* Credentials: User postgres, Password 302511.
* DDL Auto: update (Hibernate will automatically update the schema).

Security
* Security is currently configured in "permissive" mode for development:
    * CSRF disabled.
    * requestMatchers("/**").permitAll(): All routes are public without requiring a token (this should be changed for production).

### Developer Notes
* Security: The ConfigAcces class allows total access. JWT filters should be configured, and endpoints restricted by role (e.g., only ADMIN can create products).
* Error Handling: Generic RuntimeException with text messages are used. Implementing a @ControllerAdvice to handle errors in a structured way would be ideal.
* Testing: A test endpoint /test-db exists in prueba.java that verifies the connection by counting users.