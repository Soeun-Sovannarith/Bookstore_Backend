# Bookstore API Documentation

## Base URL
```
http://localhost:8080
```

## Authentication
This API uses JWT (JSON Web Token) authentication. After logging in, include the JWT token in the Authorization header for protected endpoints:

```
Authorization: Bearer <your-jwt-token>
```

## Response Format
All responses are in JSON format. Error responses include an `error` field with a descriptive message.

---

## Authentication Endpoints

### Register User
**POST** `/api/auth/register`

**Description:** Register a new user account

**Authentication:** None required

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "USER"  // Optional, defaults to "USER". Can be "USER" or "ADMIN"
}
```

**Success Response (200):**
```json
{
  "message": "User registered successfully"
}
```

**Error Response (400):**
```json
{
  "error": "User with this email already exists"
}
```

---

### Login User
**POST** `/api/auth/login`

**Description:** Authenticate user and receive JWT token

**Authentication:** None required

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Success Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

**Error Response (400):**
```json
{
  "error": "Invalid email or password"
}
```

---

## Book Endpoints

### Get All Books
**GET** `/api/books`

**Description:** Retrieve all books

**Authentication:** None required

**Success Response (200):**
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

---

### Get Book by ID
**GET** `/api/books/{id}`

**Description:** Retrieve a specific book by ID

**Authentication:** None required

**Path Parameters:**
- `id` (integer) - Book ID

**Success Response (200):**
```json
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
```

---

### Get Books by Category
**GET** `/api/books/category/{category}`

**Description:** Retrieve all books in a specific category

**Authentication:** None required

**Path Parameters:**
- `category` (string) - Book category

**Success Response (200):**
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

---

### Search Books by Title
**GET** `/api/books/search/title?title={title}`

**Description:** Search books by title

**Authentication:** None required

**Query Parameters:**
- `title` (string) - Title to search for

**Success Response (200):**
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

---

### Search Books by Author
**GET** `/api/books/search/author?author={author}`

**Description:** Search books by author

**Authentication:** None required

**Query Parameters:**
- `author` (string) - Author name to search for

**Success Response (200):**
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

---

### Create Book
**POST** `/api/books`

**Description:** Create a new book (Admin only)

**Authentication:** Required (API Key)

**Request Body:**
```json
{
  "title": "New Book Title",
  "author": "Author Name",
  "published_date": "2024-01-01",
  "stock": 25,
  "category": "Fiction",
  "price": 15.99,
  "description": "Book description",
  "imageURL": "https://example.com/book.jpg"
}
```

**Success Response (200):**
```json
{
  "bookID": 2,
  "title": "New Book Title",
  "author": "Author Name",
  "published_date": "2024-01-01",
  "stock": 25,
  "category": "Fiction",
  "price": 15.99,
  "description": "Book description",
  "imageURL": "https://example.com/book.jpg"
}
```

---

### Update Book
**PUT** `/api/books/{id}`

**Description:** Update an existing book (Admin only)

**Authentication:** Required (API Key)

**Path Parameters:**
- `id` (integer) - Book ID

**Request Body:**
```json
{
  "title": "Updated Book Title",
  "author": "Updated Author",
  "published_date": "2024-01-01",
  "stock": 30,
  "category": "Fiction",
  "price": 16.99,
  "description": "Updated description",
  "imageURL": "https://example.com/updated-book.jpg"
}
```

**Success Response (200):**
```json
{
  "bookID": 1,
  "title": "Updated Book Title",
  "author": "Updated Author",
  "published_date": "2024-01-01",
  "stock": 30,
  "category": "Fiction",
  "price": 16.99,
  "description": "Updated description",
  "imageURL": "https://example.com/updated-book.jpg"
}
```

---

### Delete Book
**DELETE** `/api/books/{id}`

**Description:** Delete a book (Admin only)

**Authentication:** Required (API Key)

**Path Parameters:**
- `id` (integer) - Book ID

**Success Response (200):** No content

---

## User Endpoints

### Get All Users
**GET** `/api/users`

**Description:** Retrieve all users

**Authentication:** Required (JWT Token)

**Success Response (200):**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "password": "$2a$10$...",
    "role": "USER",
    "createdAt": "2024-01-01T10:00:00Z"
  }
]
```

---

### Get User by ID
**GET** `/api/users/{id}`

**Description:** Retrieve a specific user by ID

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `id` (integer) - User ID

**Success Response (200):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "password": "$2a$10$...",
  "role": "USER",
  "createdAt": "2024-01-01T10:00:00Z"
}
```

---

### Create User
**POST** `/api/users`

**Description:** Create a new user

**Authentication:** Required (JWT Token)

**Request Body:**
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "password123",
  "role": "USER"
}
```

**Success Response (200):**
```json
{
  "id": 2,
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "$2a$10$...",
  "role": "USER",
  "createdAt": "2024-01-01T10:00:00Z"
}
```

---

### Update User
**PUT** `/api/users/{id}`

**Description:** Update an existing user

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `id` (integer) - User ID

**Request Body:**
```json
{
  "name": "Updated Name",
  "email": "updated@example.com",
  "password": "newpassword123",
  "role": "USER"
}
```

**Success Response (200):**
```json
{
  "id": 1,
  "name": "Updated Name",
  "email": "updated@example.com",
  "password": "$2a$10$...",
  "role": "USER",
  "createdAt": "2024-01-01T10:00:00Z"
}
```

---

### Delete User
**DELETE** `/api/users/{id}`

**Description:** Delete a user

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `id` (integer) - User ID

**Success Response (200):** No content

---

## Cart Item Endpoints

### Get All Cart Items
**GET** `/api/cart-items`

**Description:** Retrieve all cart items

**Authentication:** Required (JWT Token)

**Success Response (200):**
```json
[
  {
    "id": 1,
    "userId": 1,
    "bookId": 1,
    "quantity": 2,
    "createdAt": "2024-01-01T10:00:00Z"
  }
]
```

---

### Get Cart Item by ID
**GET** `/api/cart-items/{id}`

**Description:** Retrieve a specific cart item by ID

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `id` (integer) - Cart item ID

**Success Response (200):**
```json
{
  "id": 1,
  "userId": 1,
  "bookId": 1,
  "quantity": 2,
  "createdAt": "2024-01-01T10:00:00Z"
}
```

---

### Get Cart Items by User ID
**GET** `/api/cart-items/user/{userId}`

**Description:** Retrieve all cart items for a specific user

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `userId` (integer) - User ID

**Success Response (200):**
```json
[
  {
    "id": 1,
    "userId": 1,
    "bookId": 1,
    "quantity": 2,
    "createdAt": "2024-01-01T10:00:00Z"
  }
]
```

---

### Create Cart Item
**POST** `/api/cart-items`

**Description:** Add an item to cart

**Authentication:** Required (JWT Token)

**Request Body:**
```json
{
  "userId": 1,
  "bookId": 1,
  "quantity": 2
}
```

**Success Response (200):**
```json
{
  "id": 1,
  "userId": 1,
  "bookId": 1,
  "quantity": 2,
  "createdAt": "2024-01-01T10:00:00Z"
}
```

---

### Update Cart Item
**PUT** `/api/cart-items/{id}`

**Description:** Update an existing cart item

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `id` (integer) - Cart item ID

**Request Body:**
```json
{
  "userId": 1,
  "bookId": 1,
  "quantity": 3
}
```

**Success Response (200):**
```json
{
  "id": 1,
  "userId": 1,
  "bookId": 1,
  "quantity": 3,
  "createdAt": "2024-01-01T10:00:00Z"
}
```

---

### Delete Cart Item
**DELETE** `/api/cart-items/{id}`

**Description:** Remove an item from cart

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `id` (integer) - Cart item ID

**Success Response (200):** No content

---

## Order Endpoints

### Get All Orders
**GET** `/api/orders`

**Description:** Retrieve all orders

**Authentication:** Required (JWT Token)

**Success Response (200):**
```json
[
  {
    "id": 1,
    "userId": 1,
    "totalAmount": 25.98,
    "status": "Processing",
    "paymentMethod": "Credit Card",
    "paymentStatus": "Paid",
    "shippingAddress": "123 Main St, City, State",
    "createdAt": "2024-01-01T10:00:00Z"
  }
]
```

---

### Get Order by ID
**GET** `/api/orders/{id}`

**Description:** Retrieve a specific order by ID

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `id` (integer) - Order ID

**Success Response (200):**
```json
{
  "id": 1,
  "userId": 1,
  "totalAmount": 25.98,
  "status": "Processing",
  "paymentMethod": "Credit Card",
  "paymentStatus": "Paid",
  "shippingAddress": "123 Main St, City, State",
  "createdAt": "2024-01-01T10:00:00Z"
}
```

---

### Get Orders by User ID
**GET** `/api/orders/user/{userId}`

**Description:** Retrieve all orders for a specific user

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `userId` (integer) - User ID

**Success Response (200):**
```json
[
  {
    "id": 1,
    "userId": 1,
    "totalAmount": 25.98,
    "status": "Processing",
    "paymentMethod": "Credit Card",
    "paymentStatus": "Paid",
    "shippingAddress": "123 Main St, City, State",
    "createdAt": "2024-01-01T10:00:00Z"
  }
]
```

---

### Get Orders by Status
**GET** `/api/orders/status/{status}`

**Description:** Retrieve all orders with a specific status

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `status` (string) - Order status (e.g., "Processing", "Shipped", "Delivered")

**Success Response (200):**
```json
[
  {
    "id": 1,
    "userId": 1,
    "totalAmount": 25.98,
    "status": "Processing",
    "paymentMethod": "Credit Card",
    "paymentStatus": "Paid",
    "shippingAddress": "123 Main St, City, State",
    "createdAt": "2024-01-01T10:00:00Z"
  }
]
```

---

### Get Orders by Payment Status
**GET** `/api/orders/payment-status/{paymentStatus}`

**Description:** Retrieve all orders with a specific payment status

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `paymentStatus` (string) - Payment status (e.g., "Pending", "Paid", "Failed")

**Success Response (200):**
```json
[
  {
    "id": 1,
    "userId": 1,
    "totalAmount": 25.98,
    "status": "Processing",
    "paymentMethod": "Credit Card",
    "paymentStatus": "Paid",
    "shippingAddress": "123 Main St, City, State",
    "createdAt": "2024-01-01T10:00:00Z"
  }
]
```

---

### Create Order
**POST** `/api/orders`

**Description:** Create a new order

**Authentication:** Required (JWT Token)

**Request Body:**
```json
{
  "userId": 1,
  "totalAmount": 25.98,
  "status": "Processing",
  "paymentMethod": "Credit Card",
  "paymentStatus": "Paid",
  "shippingAddress": "123 Main St, City, State"
}
```

**Success Response (200):**
```json
{
  "id": 1,
  "userId": 1,
  "totalAmount": 25.98,
  "status": "Processing",
  "paymentMethod": "Credit Card",
  "paymentStatus": "Paid",
  "shippingAddress": "123 Main St, City, State",
  "createdAt": "2024-01-01T10:00:00Z"
}
```

---

### Update Order
**PUT** `/api/orders/{id}`

**Description:** Update an existing order

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `id` (integer) - Order ID

**Request Body:**
```json
{
  "userId": 1,
  "totalAmount": 30.98,
  "status": "Shipped",
  "paymentMethod": "Credit Card",
  "paymentStatus": "Paid",
  "shippingAddress": "123 Main St, City, State"
}
```

**Success Response (200):**
```json
{
  "id": 1,
  "userId": 1,
  "totalAmount": 30.98,
  "status": "Shipped",
  "paymentMethod": "Credit Card",
  "paymentStatus": "Paid",
  "shippingAddress": "123 Main St, City, State",
  "createdAt": "2024-01-01T10:00:00Z"
}
```

---

### Delete Order
**DELETE** `/api/orders/{id}`

**Description:** Delete an order

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `id` (integer) - Order ID

**Success Response (200):** No content

---

## Payment Endpoints

### Get All Payments
**GET** `/api/payments`

**Description:** Retrieve all payments

**Authentication:** Required (JWT Token)

**Success Response (200):**
```json
[
  {
    "id": 1,
    "orderId": 1,
    "amount": 25.98,
    "paymentMethod": "Credit Card",
    "paymentStatus": "Completed",
    "createdAt": "2024-01-01T10:00:00Z"
  }
]
```

---

### Get Payment by ID
**GET** `/api/payments/{id}`

**Description:** Retrieve a specific payment by ID

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `id` (integer) - Payment ID

**Success Response (200):**
```json
{
  "id": 1,
  "orderId": 1,
  "amount": 25.98,
  "paymentMethod": "Credit Card",
  "paymentStatus": "Completed",
  "createdAt": "2024-01-01T10:00:00Z"
}
```

---

### Get Payments by Order ID
**GET** `/api/payments/order/{orderId}`

**Description:** Retrieve all payments for a specific order

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `orderId` (integer) - Order ID

**Success Response (200):**
```json
[
  {
    "id": 1,
    "orderId": 1,
    "amount": 25.98,
    "paymentMethod": "Credit Card",
    "paymentStatus": "Completed",
    "createdAt": "2024-01-01T10:00:00Z"
  }
]
```

---

### Get Payments by Status
**GET** `/api/payments/status/{status}`

**Description:** Retrieve all payments with a specific status

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `status` (string) - Payment status (e.g., "Pending", "Completed", "Failed")

**Success Response (200):**
```json
[
  {
    "id": 1,
    "orderId": 1,
    "amount": 25.98,
    "paymentMethod": "Credit Card",
    "paymentStatus": "Completed",
    "createdAt": "2024-01-01T10:00:00Z"
  }
]
```

---

### Get Payments by Method
**GET** `/api/payments/method/{method}`

**Description:** Retrieve all payments using a specific payment method

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `method` (string) - Payment method (e.g., "Credit Card", "PayPal", "Bank Transfer")

**Success Response (200):**
```json
[
  {
    "id": 1,
    "orderId": 1,
    "amount": 25.98,
    "paymentMethod": "Credit Card",
    "paymentStatus": "Completed",
    "createdAt": "2024-01-01T10:00:00Z"
  }
]
```

---

### Create Payment
**POST** `/api/payments`

**Description:** Create a new payment

**Authentication:** Required (JWT Token)

**Request Body:**
```json
{
  "orderId": 1,
  "amount": 25.98,
  "paymentMethod": "Credit Card",
  "paymentStatus": "Completed"
}
```

**Success Response (200):**
```json
{
  "id": 1,
  "orderId": 1,
  "amount": 25.98,
  "paymentMethod": "Credit Card",
  "paymentStatus": "Completed",
  "createdAt": "2024-01-01T10:00:00Z"
}
```

---

### Update Payment
**PUT** `/api/payments/{id}`

**Description:** Update an existing payment

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `id` (integer) - Payment ID

**Request Body:**
```json
{
  "orderId": 1,
  "amount": 25.98,
  "paymentMethod": "Credit Card",
  "paymentStatus": "Refunded"
}
```

**Success Response (200):**
```json
{
  "id": 1,
  "orderId": 1,
  "amount": 25.98,
  "paymentMethod": "Credit Card",
  "paymentStatus": "Refunded",
  "createdAt": "2024-01-01T10:00:00Z"
}
```

---

### Delete Payment
**DELETE** `/api/payments/{id}`

**Description:** Delete a payment

**Authentication:** Required (JWT Token)

**Path Parameters:**
- `id` (integer) - Payment ID

**Success Response (200):** No content

---

## Error Responses

### Common HTTP Status Codes

- **200 OK** - Request successful
- **400 Bad Request** - Invalid request data
- **401 Unauthorized** - Missing or invalid JWT token
- **403 Forbidden** - Insufficient permissions
- **404 Not Found** - Resource not found
- **500 Internal Server Error** - Server error

### Error Response Format
```json
{
  "error": "Descriptive error message"
}
```

---

## Authentication Notes

### JWT Token Usage
1. **Get Token:** Use the `/api/auth/login` endpoint
2. **Use Token:** Include in Authorization header: `Authorization: Bearer <token>`
3. **Token Expiration:** Tokens expire after 24 hours (86400000 ms)

### API Key Usage (for Book Management)
- Some book management endpoints (POST, PUT, DELETE) require an API key
- Include in request header: `X-API-Key: Jct6ISFPFCPTVN5Owb3zsf9j6CMWR3qADNrp9r18icxwkibA`

### User Roles
- **USER:** Standard user privileges
- **ADMIN:** Administrative privileges

### Public Endpoints (No Authentication Required)
- `/api/auth/*` - Authentication endpoints
- `/api/books/*` (GET methods) - Book browsing and searching

### Protected Endpoints (JWT Required)
- `/api/users/*` - User management
- `/api/cart-items/*` - Cart management
- `/api/orders/*` - Order management  
- `/api/payments/*` - Payment management

---

## Data Models

### User Model
```json
{
  "id": "integer",
  "name": "string",
  "email": "string",
  "password": "string (encrypted)",
  "role": "string (USER/ADMIN)",
  "createdAt": "datetime"
}
```

### Book Model
```json
{
  "bookID": "integer",
  "title": "string",
  "author": "string", 
  "published_date": "string (YYYY-MM-DD)",
  "stock": "integer",
  "category": "string",
  "price": "double",
  "description": "string",
  "imageURL": "string"
}
```

### Cart Item Model
```json
{
  "id": "integer",
  "userId": "integer",
  "bookId": "integer", 
  "quantity": "integer",
  "createdAt": "datetime"
}
```

### Order Model
```json
{
  "id": "integer",
  "userId": "integer",
  "totalAmount": "float",
  "status": "string",
  "paymentMethod": "string",
  "paymentStatus": "string", 
  "shippingAddress": "string",
  "createdAt": "datetime"
}
```

### Payment Model
```json
{
  "id": "integer",
  "orderId": "integer",
  "amount": "float",
  "paymentMethod": "string",
  "paymentStatus": "string",
  "createdAt": "datetime"
}
```
