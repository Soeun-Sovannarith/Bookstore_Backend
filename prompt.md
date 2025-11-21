You are building a full frontend (React SPA) for an existing Bookstore backend API.

The backend is a Spring Boot application running at `http://localhost:8080`. Your job is to generate a complete, production-ready frontend that matches this backend EXACTLY in terms of routes, auth, and data models.

---

## High-Level Requirements

- Tech stack: **React** (preferably with TypeScript, React Router, and a simple state management solution like Context or a small store)
- Base API URL: `http://localhost:8080`
- CORS: Backend already allows `http://localhost:3000`
- Auth:
  - JWT-based authentication using `Authorization: Bearer <token>`
  - Some endpoints additionally require an API key header `X-API-Key`
- Data models: **User**, **Book**, **Order**, **Cart Item**, **Payment**
- Build a modern, clean Bookstore UI with:
  - Public catalog (list + details of books)
  - User auth (login, register)
  - Cart management
  - Checkout (order + payment)
  - Order history
  - Basic admin tools for users and books

The API specification below is the single source of truth. Wire all frontend functionality to these endpoints accordingly.

---

## Backend API Specification

### Base URL

- `http://localhost:8080`

### Authentication

#### JWT Token

Most protected endpoints require JWT authentication:

```http
Authorization: Bearer <jwt_token>
```

#### API Key

Some endpoints require an API key:

```http
X-API-Key: Jct6ISFPFCPTVN5Owb3zsf9j6CMWR3qADNrp9r18icxwkibA
```

### CORS

The backend allows CORS from:

- `http://localhost:3000`

---

## Authentication Endpoints

### 1. Register User

- **URL**: `POST /api/auth/register`
- **Auth**: None required
- **Description**: Register a new user account
- **Request Body**:

```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "role": "USER"
}
```

- **Response Success**:

```json
{
  "message": "User registered successfully"
}
```

- **Response Error**:

```json
{
  "error": "User with this email already exists"
}
```

### 2. Login User

- **URL**: `POST /api/auth/login`
- **Auth**: None required
- **Description**: Authenticate user and get JWT token
- **Request Body**:

```json
{
  "email": "admin@rith.codes",
  "password": "admin123"
}
```

- **Response Success**:

```json
{
  "token": "<jwt_token>",
  "user": {
    "id": 1,
    "name": "Admin User",
    "email": "admin@rith.codes",
    "role": "ADMIN"
  }
}
```

The frontend must store this token (e.g. localStorage) and add it to `Authorization` for all protected requests.

---

## User Endpoints

### 1. Get All Users (Admin only)

- **URL**: `GET /api/users`
- **Auth**: JWT + Role ADMIN required
- **Description**: Retrieve all users
- **Headers**:

```http
Authorization: Bearer <jwt_token>
```

- **Sample Response**:

```json
[
  {
    "id": 1,
    "name": "Admin User",
    "email": "admin@rith.codes",
    "password": "$2a$10$encrypted_password",
    "role": "ADMIN",
    "createdAt": "2025-11-20T10:30:00.000+00:00"
  }
]
```

### 2. Create User

- **URL**: `POST /api/users`
- **Auth**: JWT required
- **Description**: Create a new user
- **Request Body**:

```json
{
  "name": "Jane Smith",
  "email": "jane.smith@example.com",
  "password": "password123",
  "role": "USER"
}
```

### 3. Get User by ID

- **URL**: `GET /api/users/{id}`
- **Auth**: JWT required
- **Description**: Get user details by ID

- **Sample Response**:

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "$2a$10$encrypted_password",
  "role": "USER",
  "createdAt": "2025-11-20T10:30:00.000+00:00"
}
```

### 4. Update User

- **URL**: `PUT /api/users/{id}`
- **Auth**: JWT required
- **Description**: Update user information
- **Request Body**:

```json
{
  "name": "John Updated",
  "email": "john.updated@example.com",
  "role": "USER"
}
```

### 5. Delete User (Admin only)

- **URL**: `DELETE /api/users/{id}`
- **Auth**: JWT + Role ADMIN required
- **Description**: Delete user

---

## Book Endpoints (Catalog)

### 1. Get All Books

- **URL**: `GET /api/books`
- **Auth**: None required
- **Description**: Retrieve all books
- **Sample Response**:

```json
[
  {
    "bookID": 1,
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "published_date": "1925-04-10",
    "stock": 50,
    "category": "Fiction",
    "price": 12.99,
    "description": "A classic American novel",
    "imageURL": "https://example.com/gatsby.jpg"
  }
]
```

### 2. Create Book (requires API key)

- **URL**: `POST /api/books`
- **Auth**: API Key required
- **Description**: Create a new book
- **Headers**:

```http
X-API-Key: Jct6ISFPFCPTVN5Owb3zsf9j6CMWR3qADNrp9r18icxwkibA
```

- **Request Body**:

```json
{
  "title": "To Kill a Mockingbird",
  "author": "Harper Lee",
  "published_date": "1960-07-11",
  "stock": 30,
  "category": "Fiction",
  "price": 14.99,
  "description": "A novel about racial injustice and childhood",
  "imageURL": "https://example.com/mockingbird.jpg"
}
```

### 3. Get Book by ID

- **URL**: `GET /api/books/{id}`
- **Auth**: None required

### 4. Get Books by Category

- **URL**: `GET /api/books/category/{category}`
- **Auth**: None required
- **Example**: `/api/books/category/Fiction`

### 5. Search Books by Title

- **URL**: `GET /api/books/search/title?title={title}`
- **Auth**: None required
- **Example**: `/api/books/search/title?title=Great`

### 6. Search Books by Author

- **URL**: `GET /api/books/search/author?author={author}`
- **Auth**: None required
- **Example**: `/api/books/search/author?author=Fitzgerald`

### 7. Update Book (requires API key)

- **URL**: `PUT /api/books/{id}`
- **Auth**: API Key required
- **Description**: Update book info

### 8. Delete Book (requires API key)

- **URL**: `DELETE /api/books/{id}`
- **Auth**: API Key required

---

## Order Endpoints (Checkout / Orders)

### 1. Get All Orders

- **URL**: `GET /api/orders`
- **Auth**: JWT required
- **Description**: Retrieve all orders
- **Sample Response**:

```json
[
  {
    "id": 1,
    "userId": 1,
    "totalAmount": 27.98,
    "status": "PENDING",
    "paymentMethod": "CREDIT_CARD",
    "paymentStatus": "PENDING",
    "shippingAddress": "123 Main St, City, State, 12345",
    "createdAt": "2025-11-20T10:30:00.000+00:00"
  }
]
```

### 2. Create Order

- **URL**: `POST /api/orders`
- **Auth**: JWT required
- **Description**: Create a new order
- **Request Body**:

```json
{
  "userId": 1,
  "totalAmount": 27.98,
  "status": "PENDING",
  "paymentMethod": "CREDIT_CARD",
  "paymentStatus": "PENDING",
  "shippingAddress": "123 Main St, City, State, 12345"
}
```

### 3. Get Order by ID

- **URL**: `GET /api/orders/{id}`
- **Auth**: JWT required

### 4. Get Orders by User ID

- **URL**: `GET /api/orders/user/{userId}`
- **Auth**: JWT required

### 5. Get Orders by Status

- **URL**: `GET /api/orders/status/{status}`
- **Auth**: JWT required
- **Example**: `/api/orders/status/PENDING`

### 6. Get Orders by Payment Status

- **URL**: `GET /api/orders/payment-status/{paymentStatus}`
- **Auth**: JWT required
- **Example**: `/api/orders/payment-status/COMPLETED`

### 7. Update Order

- **URL**: `PUT /api/orders/{id}`
- **Auth**: JWT required

### 8. Delete Order

- **URL**: `DELETE /api/orders/{id}`
- **Auth**: JWT required

---

## Cart Item Endpoints (Shopping Cart)

### 1. Get All Cart Items

- **URL**: `GET /api/cart-items`
- **Auth**: JWT required
- **Description**: Retrieve all cart items
- **Sample Response**:

```json
[
  {
    "id": 1,
    "userId": 1,
    "bookId": 1,
    "quantity": 2,
    "createdAt": "2025-11-20T10:30:00.000+00:00"
  }
]
```

### 2. Create Cart Item (Add to Cart)

- **URL**: `POST /api/cart-items`
- **Auth**: JWT required
- **Request Body**:

```json
{
  "userId": 1,
  "bookId": 1,
  "quantity": 2
}
```

### 3. Get Cart Item by ID

- **URL**: `GET /api/cart-items/{id}`
- **Auth**: JWT required

### 4. Get Cart Items by User ID

- **URL**: `GET /api/cart-items/user/{userId}`
- **Auth**: JWT required

### 5. Update Cart Item

- **URL**: `PUT /api/cart-items/{id}`
- **Auth**: JWT required
- **Request Body**:

```json
{
  "id": 1,
  "userId": 1,
  "bookId": 1,
  "quantity": 3
}
```

### 6. Delete Cart Item

- **URL**: `DELETE /api/cart-items/{id}`
- **Auth**: JWT required

---

## Payment Endpoints

### 1. Get All Payments

- **URL**: `GET /api/payments`
- **Auth**: JWT required
- **Description**: Retrieve all payments
- **Sample Response**:

```json
[
  {
    "id": 1,
    "orderId": 1,
    "amount": 27.98,
    "paymentMethod": "CREDIT_CARD",
    "paymentStatus": "COMPLETED",
    "createdAt": "2025-11-20T10:30:00.000+00:00"
  }
]
```

### 2. Create Payment

- **URL**: `POST /api/payments`
- **Auth**: JWT required
- **Description**: Process a new payment
- **Request Body**:

```json
{
  "orderId": 1,
  "amount": 27.98,
  "paymentMethod": "CREDIT_CARD",
  "paymentStatus": "PENDING"
}
```

### 3. Get Payment by ID

- **URL**: `GET /api/payments/{id}`
- **Auth**: JWT required
- **Description**: Get payment details by ID

### 4. Get Payments by Order ID

- **URL**: `GET /api/payments/order/{orderId}`
- **Auth**: JWT required
- **Description**: Get all payments for a specific order

### 5. Get Payments by Status

- **URL**: `GET /api/payments/status/{status}`
- **Auth**: JWT required
- **Description**: Get payments by status
- **Example**: `/api/payments/status/COMPLETED`

### 6. Get Payments by Method

- **URL**: `GET /api/payments/method/{method}`
- **Auth**: JWT required
- **Description**: Get payments by method
- **Example**: `/api/payments/method/CREDIT_CARD`

### 7. Update Payment

- **URL**: `PUT /api/payments/{id}`
- **Auth**: JWT required
- **Description**: Update payment information

### 8. Delete Payment

- **URL**: `DELETE /api/payments/{id}`
- **Auth**: JWT required
- **Description**: Delete a payment record

---

## Security Debug Endpoint

### Get Authentication Info

- **URL**: `GET /api/secure/auth`
- **Auth**: JWT required
- **Description**: Get current authentication details (for debugging)
- **Sample Response**:

```json
{
  "authenticated": true,
  "name": "admin@rith.codes",
  "principal_class": "org.springframework.security.core.userdetails.User",
  "authorities": ["ROLE_ADMIN"]
}
```

---

## Error Responses

Common error formats:

**400 Bad Request**:

```json
{
  "error": "Validation error message"
}
```

**401 Unauthorized**:

```json
{
  "error": "Invalid credentials"
}
```

**403 Forbidden**:

```json
{
  "error": "Access denied"
}
```

**404 Not Found**:

```json
{
  "error": "Resource not found"
}
```

---

## Data Models (for Frontend Types)

### User

```json
{
  "id": 1,
  "name": "string",
  "email": "string",
  "password": "string (hashed)",
  "role": "USER | ADMIN",
  "createdAt": "2025-11-20T10:30:00.000+00:00"
}
```

### Book

```json
{
  "bookID": 1,
  "title": "string",
  "author": "string",
  "published_date": "YYYY-MM-DD",
  "stock": 0,
  "category": "string",
  "price": 0.0,
  "description": "string",
  "imageURL": "string"
}
```

### Order

```json
{
  "id": 1,
  "userId": 1,
  "totalAmount": 0.0,
  "status": "PENDING | CONFIRMED | SHIPPED | DELIVERED | CANCELLED",
  "paymentMethod": "CREDIT_CARD | DEBIT_CARD | PAYPAL | BANK_TRANSFER",
  "paymentStatus": "PENDING | COMPLETED | FAILED",
  "shippingAddress": "string",
  "createdAt": "2025-11-20T10:30:00.000+00:00"
}
```

### Cart Item

```json
{
  "id": 1,
  "userId": 1,
  "bookId": 1,
  "quantity": 1,
  "createdAt": "2025-11-20T10:30:00.000+00:00"
}
```

### Payment

```json
{
  "id": 1,
  "orderId": 1,
  "amount": 0.0,
  "paymentMethod": "CREDIT_CARD | DEBIT_CARD | PAYPAL | BANK_TRANSFER",
  "paymentStatus": "PENDING | COMPLETED | FAILED",
  "createdAt": "2025-11-20T10:30:00.000+00:00"
}
```

---

## Frontend Feature Requirements

Use the above API to build the following UX:

1. **Public Area**
   - Book list page (with category and search by title/author using the corresponding endpoints)
   - Book detail page
   - Login page
   - Register page

2. **Authenticated User Area**
   - Persist login with JWT
   - Attach `Authorization: Bearer <token>` automatically for protected routes
   - Cart page connected to `/api/cart-items` endpoints
   - Add to cart from book list/detail pages
   - Checkout flow:
     - Create order using `/api/orders`
     - Create payment using `/api/payments`
   - Order history page using `/api/orders/user/{userId}`

3. **Admin Area** (only if `role === 'ADMIN'`)
   - Admin dashboard
   - User management using `/api/users` endpoints
   - Book management (CRUD) using `/api/books` endpoints + API key header

4. **Error Handling & UX**
   - Display human-friendly messages for common error responses (`400`, `401`, `403`, `404`)
   - Redirect to login on `401` when appropriate

Build the frontend so that a developer can clone, `npm install`, `npm start`, and immediately talk to the backend at `http://localhost:8080` with all of the above behavior wired up.
