
The backend is a Spring Boot application running at `http://localhost:8080`. Your job is to generate a complete, production-ready frontend that matches this backend EXACTLY in terms of routes, auth, and data models.
---

## High-Level Requirements
- Tech stack: **React** (preferably with TypeScript, React Router, and a simple state management solution like Context or a small store)
- Base API URL: `http://localhost:8080`
- CORS: Backend already allows `http://localhost:3000`
- Auth:
  - JWT-based authentication using `Authorization: Bearer <token>`
- Data models: **User**, **Book**, **Order**, **Cart Item**, **Payment**
- Build a modern, clean Bookstore UI with:
  - Public catalog (list + details of books)
### 3. Get Cart Items by User ID

---

### 4. Update Cart Item
### Base URL
- `http://localhost:8080`
The backend allows CORS from:
### 5. Delete Cart Item
---

## Authentication Endpoints

### 1. Register User
- **URL**: `POST /api/auth/register`
- **Auth**: None required
- **Request Body**:
{
  "name": "John Doe",
- **Request Body**:
```json
  "email": "admin@rith.codes",
  "password": "admin123"
  }
}

---

- **URL**: `POST /api/payments`
- **Auth**: JWT required
- **Description**: Process a new payment
- **Request Body**:

```json
{
   - Book list page with search and category filter
  "amount": 27.98,
  "paymentMethod": "CREDIT_CARD",
  "paymentStatus": "PENDING"
}
```
   - Persist login with JWT in localStorage

   - Add to cart from book pages
   - Checkout flow with Bakong payment option
   - Order history page

3. **Admin Area** (if `role === 'ADMIN'`)
   - User management
   - Book management
### 5. Get Payments by Status
4. **Error Handling**
   - Display friendly error messages
   - Redirect to login on 401

### 6. Get Payments by Method

## 🆕 BAKONG KHQR PAYMENT INTEGRATION
- **Auth**: JWT required
- **Description**: Get payments by method
The backend supports **Bakong KHQR** payment system for Cambodia. Customers can pay by scanning a QR code with their Bakong mobile app.

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
---

}
```

---

## Error Responses

#### 2. Create Bakong Payment Component

**400 Bad Request**:

```json
{
  "error": "Validation error message"
}
```
- Optional: Countdown timer (15 minutes)
**401 Unauthorized**:

```json
{
  "error": "Invalid credentials"
}
// In src/lib/api.ts

**403 Forbidden**:
    const token = localStorage.getItem("token");

```json
{
  "error": "Access denied"
        "Authorization": `Bearer ${token}`,
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
              size={256}
              level="H"
            />
          </div>
          
          <div className="payment-info">
            <p>Amount: {qrData.amount} {qrData.currency}</p>
            <p>Bill Number: {qrData.billNumber}</p>
          </div>
          
          <div className="instructions">
            <h3>How to Pay:</h3>
            <ol>
              <li>Open your Bakong app</li>
              <li>Scan this QR code</li>
              <li>Confirm the payment amount</li>
              <li>Complete the payment</li>
            </ol>
          </div>
          
          <button onClick={() => setQrData(null)}>
            Generate New QR Code
          </button>
        </>
      )}
      
      {error && <div className="error">{error}</div>}
    </div>
  );
}
```

#### 5. User Flow

1. **Checkout Page**: After user creates an order, show payment options
2. **Select Bakong Payment**: User clicks "Pay with Bakong"
3. **Generate QR**: System calls `/api/payments/bakong/generate-qr`
4. **Display QR**: Show QR code with instructions
5. **Customer Scans**: Customer scans QR with Bakong app
6. **Payment Completion**: Customer confirms payment in app
7. **Confirmation**: Show success message

#### 6. Integration Points

**Checkout Flow Update**:
- Add "Pay with Bakong" as a payment method option
- After order creation, redirect to Bakong payment page if selected
- Pass order ID to BakongPayment component

**Order Status**:
- Initially mark order as "PENDING" with payment status "PENDING"
- After successful Bakong payment, update to "COMPLETED"
- Show order in user's order history

#### 7. Styling Considerations

- Make QR code prominent and easy to scan
- Use Cambodian flag colors (red/blue) for branding
- Responsive design for mobile devices
- Clear, bilingual instructions (English/Khmer)
- Loading states during QR generation
- Error handling for failed generation

#### 8. Testing

**Test Scenarios**:
1. Generate QR code with USD currency
2. Generate QR code with KHR currency
3. Handle invalid order ID (404 error)
4. Handle authentication errors (401/403)
5. Test QR code scanning with actual Bakong app
6. Test on mobile devices (primary use case)

**Test Order Creation**:
```bash
POST http://localhost:8080/api/orders
Authorization: Bearer <token>
Body: {
  "userId": 1,
  "totalAmount": 50.0,
  "status": "PENDING",
  "paymentMethod": "BAKONG",
  "paymentStatus": "PENDING",
  "shippingAddress": "123 Test St"
}
```

#### 9. Optional Enhancements
  "quantity": 1,
- **Timer**: Show 15-minute countdown for QR validity
- **Payment Verification**: Poll `/api/payments/bakong/verify-payment` endpoint
- **QR Download**: Allow users to download QR code as image
- **Payment History**: Show Bakong payments in user's payment history
- **Multi-language**: Full Khmer language support
- **Mobile Optimization**: Optimize for mobile-first experience

---

## Summary of Implementation

### Required Actions:
1. ✅ Install `qrcode.react` package
2. ✅ Create `BakongPayment` component
3. ✅ Add Bakong API calls to API service layer
4. ✅ Update checkout flow to include Bakong payment option
5. ✅ Add "BAKONG" as payment method in Order model
6. ✅ Test QR generation and display
7. ✅ Style payment page with proper UX

### Priority:
**HIGH** - Core payment feature for Cambodia market

### Estimated Effort:
- 4-6 hours for basic implementation
- 2-3 hours for styling and UX polish
- 1-2 hours for testing

---

Build the frontend so that a developer can clone, `npm install`, `npm start`, and immediately talk to the backend at `http://localhost:8080` with all of the above behavior wired up, including the Bakong payment integration.
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

---

## NEW FEATURE: Bakong KHQR Payment Integration

### Overview
The backend now supports **Bakong KHQR** payment system for Cambodia. This allows customers to pay for orders by scanning a QR code with their Bakong mobile app.

### Backend Endpoints

#### 1. Generate Bakong QR Code
- **URL**: `POST /api/payments/bakong/generate-qr`
- **Auth**: JWT required
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

#### 2. Verify Payment Status (Optional)
- **URL**: `POST /api/payments/bakong/verify-payment?md5={md5}&orderId={orderId}`
- **Auth**: JWT required
- **Note**: Currently returns placeholder response

### Frontend Implementation Requirements

#### 1. Install QR Code Library
```bash
npm install qrcode.react
```

#### 2. Create Bakong Payment Page/Component

**Features needed**:
- Display order summary (order ID, total amount)
- Currency selector (USD or KHR)
- Button to "Generate QR Code"
- Display QR code after generation
- Show payment instructions in English/Khmer
- Payment amount and bill number
- Optional: Countdown timer (e.g., 15 minutes)
- Optional: Poll for payment confirmation
- Success/failure status handling

#### 3. API Integration Example

```typescript
// In src/lib/api.ts or similar
export const bakongAPI = {
  generateQR: async (orderId: number, currency: string = "USD") => {
    const response = await fetch("http://localhost:8080/api/payments/bakong/generate-qr", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${getAuthToken()}`,
      },
      body: JSON.stringify({ orderId, currency }),
    });
    
    if (!response.ok) {
      throw new Error("Failed to generate QR code");
    }
    
    return await response.json();
  },
};
```

#### 4. Component Structure Example

```tsx
import { QRCodeSVG } from 'qrcode.react';
import { useState } from 'react';

function BakongPayment({ orderId, totalAmount }) {
  const [qrData, setQrData] = useState(null);
  const [currency, setCurrency] = useState('USD');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const generateQR = async () => {
    setLoading(true);
    try {
      const data = await bakongAPI.generateQR(orderId, currency);
      setQrData(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bakong-payment">
      <h2>Pay with Bakong</h2>
      
      {!qrData ? (
        <>
          <div className="order-summary">
            <p>Order ID: {orderId}</p>
            <p>Total Amount: ${totalAmount}</p>
          </div>
          
          <div className="currency-selector">
            <label>Currency:</label>
            <select value={currency} onChange={(e) => setCurrency(e.target.value)}>
              <option value="USD">USD</option>
              <option value="KHR">KHR</option>
            </select>
          </div>
          
          <button onClick={generateQR} disabled={loading}>
            {loading ? 'Generating...' : 'Generate QR Code'}
          </button>
        </>
      ) : (
        <>
          <div className="qr-code-display">
            <QRCodeSVG 
              value={qrData.qrCode} 
