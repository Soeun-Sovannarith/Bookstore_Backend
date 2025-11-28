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
- **Example**: `GET /api/payments/status/COMPLETED`

### 6. Get Payments by Method
- **URL**: `GET /api/payments/method/{method}`
- **Auth**: JWT required
- **Description**: Get payments by method
- **Example**: `GET /api/payments/method/CREDIT_CARD`

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
- **Response**:
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

### Common Error Formats

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

## Data Models

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
- **Example**: `GET /api/payments/status/COMPLETED`

### 6. Get Payments by Method
- **URL**: `GET /api/payments/method/{method}`
- **Auth**: JWT required
- **Description**: Get payments by method
- **Example**: `GET /api/payments/method/CREDIT_CARD`

### 7. Update Payment
- **URL**: `PUT /api/payments/{id}`
- **Auth**: JWT required
- **Description**: Update payment information

### 8. Delete Payment
- **URL**: `DELETE /api/payments/{id}`
- **Auth**: JWT required
- **Description**: Delete a payment record

---

## Bakong KHQR Payment Endpoints

### 1. Generate Bakong QR Code
- **URL**: `POST /api/payments/bakong/generate-qr`
- **Auth**: JWT required
- **Description**: Generate a Bakong KHQR code for payment
- **Request Body**:
```json
{
  "orderId": 1,
  "currency": "USD"
}
```
- **Response**:
```json
{
  "qrCode": "00020101021229370016khqr.aba.com.kh0123456789...",
  "md5": "abc123def456...",
  "billNumber": "ORDER-1-1732781234567",
  "amount": 50.0,
  "currency": "USD"
}
```
- **Supported Currencies**: `USD`, `KHR`
- **Note**: The `qrCode` field contains the KHQR string that should be encoded as a QR image for customers to scan

### 2. Verify Payment Status
- **URL**: `POST /api/payments/bakong/verify-payment`
- **Auth**: JWT required
- **Description**: Verify if a Bakong payment has been completed
- **Query Parameters**:
  - `md5`: MD5 hash from QR generation
  - `orderId`: Order ID to verify
- **Example**: `POST /api/payments/bakong/verify-payment?md5=abc123&orderId=1`
- **Response**:
```json
{
  "status": "pending",
  "message": "Payment verification not yet implemented"
}
```
- **Note**: This endpoint is a placeholder. Full implementation requires Bakong webhook setup or API polling

### 3. Bakong Webhook (Optional)
- **URL**: `POST /api/payments/bakong/webhook`
- **Auth**: None (Bakong server callback)
- **Description**: Receives payment notifications from Bakong
- **Note**: This endpoint is for Bakong system callbacks only

---

## How to Use Bakong KHQR Payment

### Payment Flow:

1. **Customer proceeds to checkout** → Creates an order

2. **Frontend requests QR code**:
   ```bash
   POST /api/payments/bakong/generate-qr
   Headers: Authorization: Bearer {jwt_token}
   Body: {"orderId": 1, "currency": "USD"}
   ```

3. **Backend generates and returns KHQR data**

4. **Frontend displays QR code** using the `qrCode` string

5. **Customer scans QR** with Bakong app on their phone

6. **Customer confirms payment** in Bakong app

7. **Payment is processed** by Bakong

8. **Order status updated** (via webhook or manual verification)

### Testing the QR Code:

1. Use the `/generate-qr` endpoint to get the KHQR string
2. Use an online QR generator to create a QR image from the string
3. Scan the QR code with Bakong app (sandbox or production)
4. Complete the payment in the app

---
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

## Notes for Frontend Team

1. **Authentication Flow**:
   - First login via `/api/auth/login` to get JWT token
   - Include JWT token in Authorization header for protected endpoints
   - For API key protected endpoints (book creation/updates/deletion), include API key in X-API-Key header

2. **Admin Access**:
   - Use `admin@rith.codes` / `admin123` for admin testing
   - Admin role required for user management endpoints

3. **CORS**:
   - Frontend running on `http://localhost:3000` is allowed
   - Other origins will be blocked

4. **Date Format**:
   - All timestamps are in ISO 8601 format with timezone
   - Published dates are in YYYY-MM-DD format

5. **Error Handling**:
   - Always check response status codes
   - Error messages are returned in `error` field of response body
# Bookstore API Documentation

## Base URL
```
http://localhost:8080
```

## Authentication

### JWT Token
Most endpoints require JWT authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <jwt_token>
```

### API Key
Some endpoints require API Key authentication. Include the API key in the X-API-Key header:
```
X-API-Key: Jct6ISFPFCPTVN5Owb3zsf9j6CMWR3qADNrp9r18icxwkibA
```

## CORS
The API supports CORS for: `http://localhost:3000`

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
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWIiOiJhZG1pbkByaXRoLmNvZGVzIiwiaWF0IjoxNzYzNjI2NTc5LCJleHAiOjE3NjM3MTI5Nzl9.Ygw-q4BLPbCPu7uMx2tw-Mc-fuX2WcrdqN8yQnIeEPDHUHycCbKuBwAQMF5j2znVYwZJR_W30W7BDoD5XSEUpw",
  "user": {
    "id": 1,
    "name": "Admin User",
    "email": "admin@rith.codes",
    "role": "ADMIN"
  }
}
```

---

## User Endpoints

### 1. Get All Users
- **URL**: `GET /api/users`
- **Auth**: JWT + Role ADMIN required
- **Description**: Retrieve all users (Admin only)
- **Headers**:
```
Authorization: Bearer <jwt_token>
```
- **Response**:
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
- **Response**:
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

### 5. Delete User
- **URL**: `DELETE /api/users/{id}`
- **Auth**: JWT + Role ADMIN required
- **Description**: Delete user (Admin only)

---

## Book Endpoints

### 1. Get All Books
- **URL**: `GET /api/books`
- **Auth**: None required
- **Description**: Retrieve all books
- **Response**:
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

### 2. Create Book
- **URL**: `POST /api/books`
- **Auth**: API Key required
- **Description**: Create a new book
- **Headers**:
```
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
- **Description**: Get book details by ID

### 4. Get Books by Category
- **URL**: `GET /api/books/category/{category}`
- **Auth**: None required
- **Description**: Get books by category
- **Example**: `GET /api/books/category/Fiction`

### 5. Search Books by Title
- **URL**: `GET /api/books/search/title?title={title}`
- **Auth**: None required
- **Description**: Search books by title
- **Example**: `GET /api/books/search/title?title=Great`

### 6. Search Books by Author
- **URL**: `GET /api/books/search/author?author={author}`
- **Auth**: None required
- **Description**: Search books by author
- **Example**: `GET /api/books/search/author?author=Fitzgerald`

### 7. Update Book
- **URL**: `PUT /api/books/{id}`
- **Auth**: API Key required
- **Description**: Update book information
- **Request Body**: Same as create book

### 8. Delete Book
- **URL**: `DELETE /api/books/{id}`
- **Auth**: API Key required
- **Description**: Delete a book

---

## Order Endpoints

### 1. Get All Orders
- **URL**: `GET /api/orders`
- **Auth**: JWT required
- **Description**: Retrieve all orders
- **Response**:
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
- **Description**: Get order details by ID

### 4. Get Orders by User ID
- **URL**: `GET /api/orders/user/{userId}`
- **Auth**: JWT required
- **Description**: Get all orders for a specific user

### 5. Get Orders by Status
- **URL**: `GET /api/orders/status/{status}`
- **Auth**: JWT required
- **Description**: Get orders by status
- **Example**: `GET /api/orders/status/PENDING`

### 6. Get Orders by Payment Status
- **URL**: `GET /api/orders/payment-status/{paymentStatus}`
- **Auth**: JWT required
- **Description**: Get orders by payment status
- **Example**: `GET /api/orders/payment-status/COMPLETED`

### 7. Update Order
- **URL**: `PUT /api/orders/{id}`
- **Auth**: JWT required
- **Description**: Update order information

### 8. Delete Order
- **URL**: `DELETE /api/orders/{id}`
- **Auth**: JWT required
- **Description**: Delete an order

---

## Cart Item Endpoints

### 1. Get All Cart Items
- **URL**: `GET /api/cart-items`
- **Auth**: JWT required
- **Description**: Retrieve all cart items
- **Response**:
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

### 2. Create Cart Item
- **URL**: `POST /api/cart-items`
- **Auth**: JWT required
- **Description**: Add item to cart
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
- **Description**: Get cart item details by ID

### 4. Get Cart Items by User ID
- **URL**: `GET /api/cart-items/user/{userId}`
- **Auth**: JWT required
- **Description**: Get all cart items for a specific user

### 5. Update Cart Item
- **URL**: `PUT /api/cart-items/{id}`
- **Auth**: JWT required
- **Description**: Update cart item quantity
- **Request Body**:
```json
{
  "quantity": 3
}
```

### 6. Delete Cart Item
- **URL**: `DELETE /api/cart-items/{id}`
- **Auth**: JWT required
- **Description**: Remove item from cart

---

## Payment Endpoints

### 1. Get All Payments
- **URL**: `GET /api/payments`
- **Auth**: JWT required
- **Description**: Retrieve all payments
- **Response**:
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
