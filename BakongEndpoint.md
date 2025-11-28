# Bakong KHQR Payment Integration - Frontend Implementation Guide

## Overview

You are implementing a **Bakong KHQR payment feature** for an existing React bookstore application. This feature allows customers in Cambodia to pay for their orders by scanning a QR code with their Bakong mobile banking app.

---

## Backend API Specification

### Base URL
```
http://localhost:8080
```

### Authentication
All Bakong endpoints require JWT authentication:
```http
Authorization: Bearer <jwt_token>
```

**How to get JWT token:**
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@rith.codes",
  "password": "admin123"
}

# Response:
{
  "token": "eyJhbGc...",
  "user": { "id": 1, "name": "Admin User", "email": "admin@rith.codes", "role": "ADMIN" }
}
```

---

## Bakong Payment Endpoints

### 1. Generate Bakong QR Code

**Endpoint:** `POST /api/payments/bakong/generate-qr`

**Headers:**
```http
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "orderId": 1,
  "currency": "USD"
}
```

**Supported Currencies:** `USD` or `KHR`

**Success Response (200):**
```json
{
  "qrCode": "00020101021229370016khqr.aba.com.kh01234567890123456789012345...",
  "md5": "abc123def456...",
  "billNumber": "ORDER-1-1732781234567",
  "amount": 50.0,
  "currency": "USD"
}
```

**Error Responses:**
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Order ID does not exist
- `500 Internal Server Error` - Failed to generate QR code

---

### 2. Verify Payment Status (Optional)

**Endpoint:** `POST /api/payments/bakong/verify-payment`

**Query Parameters:**
- `md5` - MD5 hash from QR generation response
- `orderId` - Order ID to verify

**Example:**
```
POST /api/payments/bakong/verify-payment?md5=abc123&orderId=1
Authorization: Bearer <jwt_token>
```

**Response:**
```json
{
  "status": "pending",
  "message": "Payment verification not yet implemented"
}
```

**Note:** This endpoint currently returns a placeholder. Full implementation requires Bakong webhook setup.

---

## Frontend Implementation Requirements

### 1. Install QR Code Library

```bash
npm install qrcode.react
```

or

```bash
yarn add qrcode.react
```

---

### 2. Create API Service Layer

Create or update your API service file (e.g., `src/services/bakongApi.ts` or `src/lib/api.ts`):

```typescript
// src/services/bakongApi.ts

export interface BakongQRRequest {
  orderId: number;
  currency: 'USD' | 'KHR';
}

export interface BakongQRResponse {
  qrCode: string;
  md5: string;
  billNumber: string;
  amount: number;
  currency: string;
}

export const bakongAPI = {
  /**
   * Generate Bakong QR code for payment
   * @param orderId - The order ID to generate payment for
   * @param currency - Payment currency (USD or KHR)
   * @returns Promise with QR code data
   */
  generateQR: async (
    orderId: number, 
    currency: 'USD' | 'KHR' = 'USD'
  ): Promise<BakongQRResponse> => {
    const token = localStorage.getItem('token'); // Adjust based on your auth implementation
    
    if (!token) {
      throw new Error('Authentication required');
    }
    
    const response = await fetch(
      'http://localhost:8080/api/payments/bakong/generate-qr',
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({ orderId, currency }),
      }
    );
    
    if (!response.ok) {
      if (response.status === 401) {
        throw new Error('Unauthorized - Please login again');
      }
      if (response.status === 404) {
        throw new Error('Order not found');
      }
      throw new Error('Failed to generate QR code');
    }
    
    return await response.json();
  },

  /**
   * Verify payment status (optional - currently placeholder)
   */
  verifyPayment: async (md5: string, orderId: number) => {
    const token = localStorage.getItem('token');
    
    const response = await fetch(
      `http://localhost:8080/api/payments/bakong/verify-payment?md5=${md5}&orderId=${orderId}`,
      {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      }
    );
    
    return await response.json();
  },
};
```

---

### 3. Create Bakong Payment Component

Create a new component file (e.g., `src/components/BakongPayment.tsx` or `src/pages/BakongPayment.jsx`):

```tsx
import React, { useState } from 'react';
import { QRCodeSVG } from 'qrcode.react';
import { bakongAPI } from '../services/bakongApi';

interface BakongPaymentProps {
  orderId: number;
  totalAmount: number;
  onSuccess?: () => void;
  onCancel?: () => void;
}

const BakongPayment: React.FC<BakongPaymentProps> = ({
  orderId,
  totalAmount,
  onSuccess,
  onCancel,
}) => {
  const [qrData, setQrData] = useState<any>(null);
  const [currency, setCurrency] = useState<'USD' | 'KHR'>('USD');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleGenerateQR = async () => {
    setLoading(true);
    setError(null);
    
    try {
      const data = await bakongAPI.generateQR(orderId, currency);
      setQrData(data);
    } catch (err: any) {
      setError(err.message || 'Failed to generate QR code');
      console.error('Error generating QR:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setQrData(null);
    setError(null);
  };

  if (qrData) {
    return (
      <div className="bakong-payment-container">
        <div className="bakong-payment-card">
          <h2>Scan to Pay with Bakong</h2>
          
          {/* QR Code Display */}
          <div className="qr-code-wrapper">
            <QRCodeSVG 
              value={qrData.qrCode} 
              size={280}
              level="H"
              includeMargin={true}
            />
          </div>
          
          {/* Payment Information */}
          <div className="payment-info">
            <div className="info-row">
              <span className="label">Amount:</span>
              <span className="value">
                {qrData.amount} {qrData.currency}
              </span>
            </div>
            <div className="info-row">
              <span className="label">Bill Number:</span>
              <span className="value">{qrData.billNumber}</span>
            </div>
          </div>
          
          {/* Instructions */}
          <div className="payment-instructions">
            <h3>How to Pay:</h3>
            <ol>
              <li>Open your Bakong app on your phone</li>
              <li>Tap "Scan QR" or similar option</li>
              <li>Scan this QR code</li>
              <li>Confirm the payment amount</li>
              <li>Complete the payment</li>
            </ol>
          </div>

          {/* Khmer Instructions */}
          <div className="payment-instructions khmer">
            <h3>របៀបបង់ប្រាក់:</h3>
            <ol>
              <li>បើកកម្មវិធី Bakong របស់អ្នក</li>
              <li>ចុចប៊ូតុង "ស្កែន QR"</li>
              <li>ស្កែន QR code នេះ</li>
              <li>បញ្ជាក់ចំនួនទឹកប្រាក់</li>
              <li>បញ្ចប់ការបង់ប្រាក់</li>
            </ol>
          </div>
          
          {/* Action Buttons */}
          <div className="button-group">
            <button 
              onClick={handleReset}
              className="btn-secondary"
            >
              Generate New QR
            </button>
            {onSuccess && (
              <button 
                onClick={onSuccess}
                className="btn-primary"
              >
                I've Completed Payment
              </button>
            )}
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="bakong-payment-container">
      <div className="bakong-payment-card">
        <h2>Pay with Bakong</h2>
        
        {/* Order Summary */}
        <div className="order-summary">
          <h3>Order Summary</h3>
          <div className="summary-row">
            <span>Order ID:</span>
            <span>#{orderId}</span>
          </div>
          <div className="summary-row total">
            <span>Total Amount:</span>
            <span className="amount">${totalAmount.toFixed(2)}</span>
          </div>
        </div>
        
        {/* Currency Selector */}
        <div className="currency-selector">
          <label htmlFor="currency">Select Currency:</label>
          <select 
            id="currency"
            value={currency} 
            onChange={(e) => setCurrency(e.target.value as 'USD' | 'KHR')}
            disabled={loading}
          >
            <option value="USD">USD (US Dollar)</option>
            <option value="KHR">KHR (Cambodian Riel)</option>
          </select>
        </div>
        
        {/* Error Message */}
        {error && (
          <div className="error-message">
            <span className="error-icon">⚠️</span>
            <span>{error}</span>
          </div>
        )}
        
        {/* Action Buttons */}
        <div className="button-group">
          <button 
            onClick={handleGenerateQR}
            disabled={loading}
            className="btn-primary"
          >
            {loading ? 'Generating QR Code...' : 'Generate QR Code'}
          </button>
          {onCancel && (
            <button 
              onClick={onCancel}
              disabled={loading}
              className="btn-secondary"
            >
              Cancel
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default BakongPayment;
```

---

### 4. Add Styling (CSS)

Create a CSS file for the component (e.g., `src/components/BakongPayment.css`):

```css
/* BakongPayment.css */

.bakong-payment-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.bakong-payment-card {
  background: white;
  border-radius: 16px;
  padding: 40px;
  max-width: 500px;
  width: 100%;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.bakong-payment-card h2 {
  text-align: center;
  color: #1a202c;
  margin-bottom: 30px;
  font-size: 28px;
}

/* QR Code */
.qr-code-wrapper {
  display: flex;
  justify-content: center;
  padding: 20px;
  background: #f7fafc;
  border-radius: 12px;
  margin-bottom: 24px;
}

/* Payment Info */
.payment-info {
  margin-bottom: 24px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #e2e8f0;
}

.info-row .label {
  color: #718096;
  font-weight: 500;
}

.info-row .value {
  color: #1a202c;
  font-weight: 600;
}

/* Order Summary */
.order-summary {
  background: #f7fafc;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 24px;
}

.order-summary h3 {
  margin-bottom: 16px;
  color: #2d3748;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
}

.summary-row.total {
  border-top: 2px solid #cbd5e0;
  padding-top: 12px;
  margin-top: 12px;
  font-size: 18px;
  font-weight: 600;
}

.summary-row .amount {
  color: #d03f2a;
}

/* Currency Selector */
.currency-selector {
  margin-bottom: 24px;
}

.currency-selector label {
  display: block;
  margin-bottom: 8px;
  color: #4a5568;
  font-weight: 500;
}

.currency-selector select {
  width: 100%;
  padding: 12px;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 16px;
  color: #1a202c;
  background: white;
  cursor: pointer;
  transition: border-color 0.2s;
}

.currency-selector select:hover {
  border-color: #cbd5e0;
}

.currency-selector select:focus {
  outline: none;
  border-color: #667eea;
}

/* Instructions */
.payment-instructions {
  margin-bottom: 24px;
  padding: 20px;
  background: #edf2f7;
  border-radius: 8px;
}

.payment-instructions h3 {
  margin-bottom: 12px;
  color: #2d3748;
}

.payment-instructions ol {
  margin-left: 20px;
}

.payment-instructions li {
  margin-bottom: 8px;
  color: #4a5568;
  line-height: 1.6;
}

.payment-instructions.khmer {
  background: #e6fffa;
  border-left: 4px solid #38b2ac;
}

/* Buttons */
.button-group {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

.btn-primary,
.btn-secondary {
  flex: 1;
  padding: 14px 24px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-primary {
  background: #667eea;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background: #5a67d8;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.btn-primary:disabled {
  background: #cbd5e0;
  cursor: not-allowed;
}

.btn-secondary {
  background: white;
  color: #4a5568;
  border: 2px solid #e2e8f0;
}

.btn-secondary:hover:not(:disabled) {
  background: #f7fafc;
  border-color: #cbd5e0;
}

/* Error Message */
.error-message {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #fff5f5;
  border: 1px solid #fc8181;
  border-radius: 8px;
  color: #c53030;
  margin-bottom: 16px;
}

.error-icon {
  font-size: 20px;
}

/* Responsive */
@media (max-width: 640px) {
  .bakong-payment-card {
    padding: 24px;
  }
  
  .qr-code-wrapper svg {
    width: 220px !important;
    height: 220px !important;
  }
  
  .button-group {
    flex-direction: column;
  }
}
```

Don't forget to import the CSS in your component:
```tsx
import './BakongPayment.css';
```

---

### 5. Integrate into Your Checkout Flow

#### Option A: As a Payment Method Selection

In your checkout page:

```tsx
import React, { useState } from 'react';
import BakongPayment from './components/BakongPayment';

function CheckoutPage() {
  const [paymentMethod, setPaymentMethod] = useState('');
  const [orderId, setOrderId] = useState<number | null>(null);
  
  const handleCreateOrder = async () => {
    // Create order first
    const order = await createOrder({
      userId: currentUser.id,
      totalAmount: cartTotal,
      status: 'PENDING',
      paymentMethod: 'BAKONG',
      paymentStatus: 'PENDING',
      shippingAddress: shippingAddress,
    });
    
    setOrderId(order.id);
    setPaymentMethod('BAKONG');
  };
  
  if (paymentMethod === 'BAKONG' && orderId) {
    return (
      <BakongPayment 
        orderId={orderId}
        totalAmount={cartTotal}
        onSuccess={() => {
          // Redirect to order confirmation
          navigate(`/orders/${orderId}`);
        }}
        onCancel={() => {
          setPaymentMethod('');
          setOrderId(null);
        }}
      />
    );
  }
  
  return (
    <div className="checkout">
      {/* Your checkout form */}
      <button onClick={handleCreateOrder}>
        Pay with Bakong
      </button>
    </div>
  );
}
```

#### Option B: As a Dedicated Route

Add to your router:

```tsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import BakongPayment from './components/BakongPayment';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Other routes */}
        <Route 
          path="/payment/bakong/:orderId" 
          element={<BakongPaymentPage />} 
        />
      </Routes>
    </BrowserRouter>
  );
}

function BakongPaymentPage() {
  const { orderId } = useParams();
  const navigate = useNavigate();
  
  // Fetch order details
  const [order, setOrder] = useState(null);
  
  useEffect(() => {
    fetchOrder(orderId).then(setOrder);
  }, [orderId]);
  
  if (!order) return <div>Loading...</div>;
  
  return (
    <BakongPayment 
      orderId={Number(orderId)}
      totalAmount={order.totalAmount}
      onSuccess={() => navigate(`/orders/${orderId}/success`)}
      onCancel={() => navigate('/orders')}
    />
  );
}
```

---

## Testing Guide

### 1. Test with Postman First

Before testing in the frontend, verify the backend works:

```bash
# 1. Login to get token
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@rith.codes",
  "password": "admin123"
}

# Copy the token from response

# 2. Create a test order
POST http://localhost:8080/api/orders
Authorization: Bearer <your_token>
Content-Type: application/json

{
  "userId": 1,
  "totalAmount": 50.0,
  "status": "PENDING",
  "paymentMethod": "BAKONG",
  "paymentStatus": "PENDING",
  "shippingAddress": "123 Test Street, Phnom Penh"
}

# Note the order ID from response

# 3. Generate QR code
POST http://localhost:8080/api/payments/bakong/generate-qr
Authorization: Bearer <your_token>
Content-Type: application/json

{
  "orderId": 1,
  "currency": "USD"
}

# You should receive QR code data
```

### 2. Frontend Testing Checklist

- [ ] Component renders without errors
- [ ] Currency selector shows USD and KHR options
- [ ] "Generate QR Code" button is clickable
- [ ] Loading state shows during QR generation
- [ ] QR code displays correctly after generation
- [ ] Payment amount and bill number are shown
- [ ] Instructions are visible in English and Khmer
- [ ] Error messages display for invalid requests
- [ ] "Generate New QR" button resets the form
- [ ] Component works on mobile devices
- [ ] QR code is scannable with Bakong app

### 3. Test Scenarios

**Happy Path:**
1. User completes checkout
2. Order is created with BAKONG payment method
3. User is shown Bakong payment page
4. User selects currency (USD or KHR)
5. User clicks "Generate QR Code"
6. QR code is displayed
7. User scans with Bakong app
8. Payment is completed in Bakong app
9. User clicks "I've Completed Payment"
10. User is redirected to order confirmation

**Error Scenarios:**
- Invalid order ID → Show "Order not found" error
- Missing JWT token → Show "Authentication required" error
- Network failure → Show "Failed to generate QR code" error
- Backend error → Show appropriate error message

---

## Common Issues & Solutions

### Issue 1: CORS Error
```
Access to fetch at 'http://localhost:8080/api/payments/bakong/generate-qr' 
from origin 'http://localhost:3000' has been blocked by CORS policy
```

**Solution:** The backend already has CORS configured for `http://localhost:3000`. Make sure your frontend is running on this exact URL.

### Issue 2: 401 Unauthorized
```
Failed to generate QR code: Unauthorized - Please login again
```

**Solution:** 
- Check that JWT token is properly stored in localStorage
- Verify token is included in Authorization header
- Token might be expired - try logging in again

### Issue 3: QR Code Not Displaying
**Solution:**
- Verify `qrcode.react` is installed: `npm list qrcode.react`
- Check browser console for errors
- Ensure the `qrCode` string from API is valid

### Issue 4: Order Not Found (404)
**Solution:**
- Verify the order exists in the database
- Check the order ID being passed is correct
- Ensure order was created successfully before payment

---

## Next Steps (Optional Enhancements)

1. **Payment Verification Polling**
   - Poll `/api/payments/bakong/verify-payment` every 5 seconds
   - Automatically redirect on successful payment

2. **QR Code Download**
   - Add button to download QR code as PNG image
   - Use canvas API to convert SVG to image

3. **Timer Countdown**
   - Add 15-minute countdown timer
   - Show warning when time is running out
   - Auto-expire and generate new QR after timeout

4. **Payment History**
   - Show Bakong payments in user's payment history
   - Display payment status and timestamps

5. **Multi-language Support**
   - Full Khmer translation
   - Language switcher
   - Proper Khmer font rendering

6. **Mobile Optimization**
   - Larger QR code on mobile
   - Better touch targets
   - Simplified mobile UI

---

## Support & Resources

- **Backend API Documentation:** `/Users/ppc/Documents/Bookstore/API_DOC.md`
- **Bakong Official:** https://bakong.nbc.gov.kh
- **QR Code Library:** https://www.npmjs.com/package/qrcode.react

---

**Happy Coding! 🚀🇰🇭**

